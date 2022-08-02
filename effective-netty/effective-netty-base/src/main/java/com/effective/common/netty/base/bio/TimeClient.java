package com.effective.common.netty.base.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 */
public class TimeClient {

    /**
     * @param args
     */
    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                startup(args);
            }).start();
        }
    }

    private static void startup(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {

            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        Socket socket = null;
        BufferedReader in = null;//读取缓冲区
        PrintWriter out = null;  //写入流
        try {
            socket = new Socket("127.0.0.1", port);
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );
            out = new PrintWriter(socket.getOutputStream(), true);

            for (int i = 0; i < 100; i++) {
                out.println("QUERY TIME ORDER");
                String resp = in.readLine();
                System.out.println(Thread.currentThread().getName()+ " Now is : " + resp);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
