package com.gkpoter.util;

import com.gkpoter.util.client.ClientSocket;
import com.gkpoter.util.server.ServerSocket;
import com.gkpoter.util.stream.OutputTCPSocket;

import java.net.Socket;

public class Main {

    static Socket socket;

    public static void main(String... args) {
        ServerSocket serverSocket = new ServerSocket(6789, new ServerSocket.ServerSocketListener() {
            @Override
            public void call(byte[] bytes) {
                System.out.println("result : " + new String(bytes));
                new OutputTCPSocket(Main.socket, "BayBay".getBytes(), (isOut, e) -> {
                    if(!isOut){
                        e.printStackTrace();
                    }
                }).start();
            }

            @Override
            public void connected(Socket socket) {
                System.out.println(socket.getInetAddress().getHostAddress());
                Main.socket = socket;
            }
        });
        serverSocket.start();

        ClientSocket clientSocket = new ClientSocket("127.0.0.1",6789);
        clientSocket.start();

        clientSocket.send("HelloWorld");
        clientSocket.getByte(bytes -> System.out.println(new String(bytes)));

        //serverSocket.close();

    }
}
