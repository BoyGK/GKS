package com.gkpoter.util.server;

import com.gkpoter.util.manager.SocketManager;
import com.gkpoter.util.stream.InputTCPSocket;

import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class ServerSocket extends Thread {

    private boolean RUN_STATE = true;

    private SocketManager.Way SOCKET_WAY = SocketManager.Way.TCP;

    private ServerSocketListener listener;

    private int PORT;

    private Vector<InputTCPSocket> tcps = new Vector<>();

    private java.net.ServerSocket serverSocket = null;

    public ServerSocket(int port, ServerSocketListener listener) {
        this.PORT = port;
        this.listener = listener;
    }

    public ServerSocket(int port, SocketManager.Way socket_way,ServerSocketListener listener) {
        this.PORT = port;
        this.SOCKET_WAY = socket_way;
        this.listener = listener;
    }


    public interface ServerSocketListener {
        void call(byte[] bytes);
        void connected(Socket socket);
    }

    @Override
    public void run() {
        switch (SOCKET_WAY) {
            case TCP:
                openTCP();
                break;
            case UDP:
                openUDP();
                break;
            default:
                break;
        }
    }

    private void openTCP() {
        try {
            serverSocket = new java.net.ServerSocket(PORT);
            while (RUN_STATE) {
                Socket socket = serverSocket.accept();
                SocketManager.sockets.add(socket);
                listener.connected(socket);
                InputTCPSocket tcp = new InputTCPSocket(socket, bytes -> listener.call(bytes));
                tcp.start();
                tcps.add(tcp);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void openUDP() {
    }

    public void close(){
        this.RUN_STATE = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        for (InputTCPSocket tcp : tcps) {
            tcp.close();
        }
    }
}
