package com.effective.common.netty.base.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 传统的BIO模型
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
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket;
            while (true) {
                socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }
        } finally {
            if (server != null) {
                System.out.println("The time server close");
                server.close();
            }
        }
    }


    public static class TimeServerHandler implements Runnable {

        private Socket socket;

        public TimeServerHandler(Socket socket) {
            this.socket = socket;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                in = new BufferedReader(
                        new InputStreamReader(
                                this.socket.getInputStream()
                        )
                );
                out = new PrintWriter(this.socket.getOutputStream(), true);
                String currentTime;
                String body;
                while (true) {
                    body = in.readLine();
                    if (body == null) break;
                    System.out.println(Thread.currentThread().getName()+ " The time server receive order : " + body);
                    currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                    out.println(currentTime);
                }

            } catch (Exception e) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (out != null) {
                    out.close();
                }
                if (this.socket != null) {
                    try {
                        this.socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    this.socket = null;
                }
            }
        }
    }
}
