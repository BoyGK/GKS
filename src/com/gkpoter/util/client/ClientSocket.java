package com.gkpoter.util.client;

import com.gkpoter.util.manager.SocketManager;
import com.gkpoter.util.stream.InputTCPSocket;
import com.gkpoter.util.stream.OutputTCPSocket;

import java.io.IOException;
import java.net.Socket;

public class ClientSocket {

    private String URL;
    private int PORT;

    private byte[] bytes;

    private Socket socket = null;

    private SocketManager.Way SOCKET_WAY = SocketManager.Way.TCP;

    public ClientSocket(String url, int port) {
        this(url, port, SocketManager.Way.TCP);
    }

    public ClientSocket(String url, int port, SocketManager.Way socket_way) {
        this.URL = url;
        this.PORT = port;
        this.SOCKET_WAY = socket_way;
    }

    public void start() {
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
            socket = new Socket(URL, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openUDP() {
    }

    public void send(byte[] bytes) {
        switch (SOCKET_WAY) {
            case TCP:
                sendByTCP(bytes);
                break;
            case UDP:
                openUDP();
                break;
            default:
                break;
        }
    }

    public void send(int value) {
        send(String.valueOf(value).getBytes());
    }

    public void send(String str) {
        send(str.getBytes());
    }

    public void sendByTCP(byte[] bytes){
        new OutputTCPSocket(socket, bytes, (isOut, e) -> {
            if(!isOut){
                e.printStackTrace();
            }
        }).start();
    }

    public void getByte(ClientSocketListener listener){
        InputTCPSocket tcp = new InputTCPSocket(socket, bytes -> listener.call(bytes));
        tcp.start();
    }

    public void close() {
        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public interface ClientSocketListener{
        void call(byte[] bytes);
    }

}
