package com.effective.common.netty.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class TimeServer {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
    }


    static class AsyncTimeServerHandler implements Runnable {


        private CountDownLatch latch;
        private AsynchronousServerSocketChannel asynchronousServerSocketChannel;

        public AsyncTimeServerHandler(int port) {
            try {
                asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
                asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
                System.out.println("The time server is start in port : " + port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            latch = new CountDownLatch(1);
            asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    static class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {

        @Override
        public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
            attachment.asynchronousServerSocketChannel.accept(attachment, this);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            result.read(buffer, buffer, new ReadCompletionHandler(result));
        }

        @Override
        public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
            exc.printStackTrace();
            attachment.latch.countDown();
        }

    }


    static class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

        private AsynchronousSocketChannel channel;

        public ReadCompletionHandler(AsynchronousSocketChannel channel) {
            if (this.channel == null)
                this.channel = channel;
        }

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            attachment.flip();
            byte[] body = new byte[attachment.remaining()];
            attachment.get(body);
            try {
                String req = new String(body, "UTF-8");
                System.out.println("The time server receive order : " + req);
                String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req) ? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                doWrite(currentTime);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        private void doWrite(String currentTime) {
            if (currentTime != null && currentTime.trim().length() > 0) {
                byte[] bytes = (currentTime).getBytes();
                ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                writeBuffer.put(bytes);
                writeBuffer.flip();
                channel.write(writeBuffer, writeBuffer,
                        new CompletionHandler<Integer, ByteBuffer>() {
                            @Override
                            public void completed(Integer result, ByteBuffer buffer) {
                                // 如果没有发送完成，继续发送
                                if (buffer.hasRemaining())
                                    channel.write(buffer, buffer, this);
                            }

                            @Override
                            public void failed(Throwable exc, ByteBuffer attachment) {
                                try {
                                    channel.close();
                                } catch (IOException e) {
                                    // ingnore on close
                                }
                            }
                        });
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            try {
                this.channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
