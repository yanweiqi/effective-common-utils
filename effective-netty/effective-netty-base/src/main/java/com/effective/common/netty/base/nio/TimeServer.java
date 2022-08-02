package com.effective.common.netty.base.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by yanweiqi on 2018/12/4.
 *
 * 1.缓冲区Buffer
 *   Buffer是一个对象，它包含一些要写入和读出的数据
 *   在面向I/O中，可以将数据写入或者将数据直接读到Stream对象中，在NIO库中，所有数据都是用缓冲区处理的，读数据是它是直接读到缓冲区中的，写入
 *   数据写到缓冲区中，缓冲区实质是一个数组，它通常是一个字节数组(ByteBuffer),也可以使用其他种类的数组，但是一个缓冲区不仅仅是一个数组，缓
 *   冲区提供了对数据的结构化访问以及维护读写位置的limit
 *   >ByteBuffer   字节缓冲区         1个字节
 *   >CharBuffer   字符缓冲区         2个字节
 *   >ShortBuffer  短整型缓冲区       2个字节
 *   >IntBuffer    整型缓冲区         4个字节
 *   >LongBuffer   长整型缓冲区       8个字节
 *   >FloatBuffer  浮点型缓冲区       4个字节
 *   >DoubleBuffer 双精度浮点型缓冲区  8个字节
 *
 * 2.通道Channel
 *   通道与流不同之处在于通道是双向的，流志在一个方向上移动，而通道可以读写同时进行
 *
 * 3.多路复用
 *   多路复用器提供选择已经就绪的任务，selector会不断地轮训注册channel,如果某个channel上面发生读或者写事件，这个channel就处于就绪状态
 *   会被select轮训到，然后通过selectionKey可以获取就绪Channel的集合，进行后续的IO操作.
 *   一个多路复用器可以轮训多个channel，JDK使用了epoll，来代替select，所以它并没有最大连接句柄1024/2048的限制。这意味着一个线程负责
 *   selector的轮训就可以接入成千上万的客户端
 */
public class TimeServer {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);

        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
    }


    static class MultiplexerTimeServer implements Runnable {

        /**
         * 多路复用器，轮训就绪的channel
         */
        private Selector selector;

        /**
         * 用于监听客户端的连接，它是所有客户端的父通道
         */
        private ServerSocketChannel servChannel;

        private volatile boolean stop;

        /**
         * 初始化多路复用器、绑定监听端口
         *
         * @param port
         */
        public MultiplexerTimeServer(int port) {
            try {
                selector = Selector.open();//通过静态方法赋值
                servChannel = ServerSocketChannel.open();
                servChannel.socket().bind(new InetSocketAddress(port), 1024);
                servChannel.configureBlocking(false);;//非阻塞模式
                //把多路复用器注册到serverSocketChannel中
                servChannel.register(
                        selector,
                        SelectionKey.OP_ACCEPT
                );
                System.out.println("The time server is start in port : " + port);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        public void stop() {
            this.stop = true;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            while (!stop) {
                try {
                    selector.select();
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectedKeys.iterator();
                    SelectionKey key = null;
                    while (it.hasNext()) {
                        try {
                            key = it.next();
                            it.remove();
                            handleInput(key);
                        } catch (Exception e) {
                            if (key != null) {
                                key.cancel();
                                if (key.channel() != null)
                                    key.channel().close();
                            }
                        }
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            // 多路复用器关闭后，所有注册在上面的Channel和Pipe等资源都会被自动去注册并关闭，所以不需要重复释放资源
            if (selector != null)
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        private void handleInput(SelectionKey key) throws IOException {

            if (key.isValid()) {
                // 处理新接入的请求消息
                if (key.isAcceptable()) {
                    // Accept the new connection
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    // Add the new connection to the selector
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
                if (key.isReadable()) {
                    // Read the data
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                    int readBytes = sc.read(readBuffer);
                    if (readBytes > 0  ) {
                        readBuffer.flip();
                        byte[] bytes = new byte[readBuffer.remaining()];
                        readBuffer.get(bytes);
                        String body = new String(bytes, "UTF-8");
                        System.out.println("The time server receive order : " + body);
                        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                        doWrite(sc, currentTime);
                    } else if (readBytes < 0) {
                        // 对端链路关闭
                        key.cancel();
                        sc.close();
                    } else
                        ; // 读到0字节，忽略

                }
            }
        }

        private void doWrite(SocketChannel channel, String response) throws IOException {
            if (response != null && response.trim().length() > 0) {
                byte[] bytes = response.getBytes();
                ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                writeBuffer.put(bytes);
                writeBuffer.flip();
                channel.write(writeBuffer);
            }
        }
    }
}
