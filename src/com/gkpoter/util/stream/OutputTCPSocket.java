package com.gkpoter.util.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class OutputTCPSocket{

    private Socket socket;
    private byte[] bytes;

    private TCPSocketListener listener;

    public OutputTCPSocket(Socket socket,byte[] bytes,TCPSocketListener listener) {
        this.socket = socket;
        this.bytes = bytes;
        this.listener = listener;
    }

    public void start() {
        OutputStream os =null;
        try {
            os = socket.getOutputStream();
            os.write((bytes.length+"").getBytes());
            os.flush();
            Thread.sleep(10);
            os.write(bytes);
            os.flush();
            listener.call(true,null);
        } catch (Exception e) {
            listener.call(false,e);
            //e.printStackTrace();
        }
    }

    public interface TCPSocketListener{
        void call(boolean isOut,Exception e);
    }
}
