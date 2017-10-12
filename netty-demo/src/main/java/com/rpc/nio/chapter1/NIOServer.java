package com.rpc.nio.chapter1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * NIO服务端
 *
 */
public class NIOServer {

	/**
	 * 通道管理器
	 */
	private Selector selector;

	/**
	 * 获得一个ServerSocket通道，并对该通道做一些初始化的工作
	 * 
	 * @param port 绑定的端口号
	 * @throws IOException
	 */
	public void initServer(int port) throws IOException {

		ServerSocketChannel serverChannel = ServerSocketChannel.open(); // 获得一个ServerSocket通道

		serverChannel.configureBlocking(false); // 设置通道为非阻塞

		serverChannel.socket().bind(new InetSocketAddress(port)); // 将该通道对应的ServerSocket绑定到port端口

		this.selector = Selector.open(); // 获得一个通道管理器

		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	}

	/**
	 * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
	 * 
	 * @throws IOException
	 */
	public void listen() throws IOException {
		System.out.println("服务端启动成功！");
		while (true) {
			selector.select(); // 当注册的事件到达时，方法返回；否则,该方法会一直阻塞
			Iterator<?> ite = this.selector.selectedKeys().iterator(); // 获得selector中选中的项的迭代器，选中的项为注册的事件
			while (ite.hasNext()) {
				SelectionKey key = (SelectionKey) ite.next();
				ite.remove(); // 删除已选的key,以防重复处理
				handler(key);
			}
		}
	}

	/**
	 * 处理请求
	 * 
	 * @param key
	 * @throws IOException
	 */
	public void handler(SelectionKey key) throws IOException {
		if (key.isAcceptable()) {
			handlerAccept(key);
		} else if (key.isReadable()) {
			handlerRead(key);
		}
	}

	/**
	 * 处理连接请求
	 * 
	 * @param key
	 * @throws IOException
	 */
	public void handlerAccept(SelectionKey key) throws IOException {
		ServerSocketChannel server = (ServerSocketChannel) key.channel();
		SocketChannel channel = server.accept();
		channel.configureBlocking(false);
		System.out.println("新的客户端连接");
		channel.register(this.selector, SelectionKey.OP_READ); // 在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
	}

	/**
	 * 处理读的事件
	 * 
	 * @param key
	 * @throws IOException
	 */
	public void handlerRead(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int read = channel.read(buffer);
		if(read > 0){
			byte[] data = buffer.array();
			String msg = new String(data).trim();
			System.out.println("服务端收到信息：" + msg);
			ByteBuffer outBuffer = ByteBuffer.wrap("好的".getBytes());
			channel.write(outBuffer);
		}else{
			System.out.println("客户端关闭");
			key.cancel();
		}
	}

	/**
	 * 启动服务端测试
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		NIOServer server = new NIOServer();
		server.initServer(8000);
		server.listen();
	}

}
