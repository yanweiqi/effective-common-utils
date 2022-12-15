package com.effective.common.netty.pio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 伪IO模型，
 *
 * 采用线程池和任务队列的方式实现
 * 当有新的客户端接入，将客户端的socket封装成一个Runable的Task，放入线程池中，JDK的线程池维护一个消息队列和N个活跃的线程，对消息队列
 * 的任务进行处理，由于线程池可以设置消息队列的大小和最大线程数，因此资源可控，无论多少个并发都不会导致资源耗尽和宕机
 *
 * 当对Socket的输入流进行读取操作时候，他会一直阻塞下去，直到发生三种事件
 * > 有数据可读
 * > 可用数据已经读取完毕
 * > 发生空指针或者I/O异常
 * 这意味着当对方发送请求或者应答消息比较缓慢，或者网络传输慢时，读取输入流一方的通信线程将被长时间阻塞，如果对方要60s才能将数据发送完毕，读取
 * 一方的I/O线程也将会被同步阻塞60s,在此期间其它消息队列只能在消息队列中排队
 *
 */
public class TimeServer {

    static int maxPoolSize = 50;
    static int queueSize = 100;

    static ExecutorService executor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            maxPoolSize,
            120L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(queueSize));


    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length > 0) {

            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }

        }
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket;

            while (true) {
                socket = server.accept();
                executor.execute(new com.effective.common.netty.bio.TimeServer.TimeServerHandler(socket));
            }
        } finally {
            if (server != null) {
                System.out.println("The time server close");
                server.close();
            }
        }
    }

}
