package com.effective.common.netty.pio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
