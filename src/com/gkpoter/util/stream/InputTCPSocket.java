package com.gkpoter.util.stream;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class InputTCPSocket extends Thread {

    private Socket socket;
    private TCPSocketListener listener;
    private boolean RUN_TIMES = true;

    private InputStream is;

    public InputTCPSocket(Socket socket, TCPSocketListener listener) {
        this.socket = socket;
        this.listener = listener;
    }

    public interface TCPSocketListener {
        void call(byte[] bytes);
    }

    @Override
    public void run() {
        while (RUN_TIMES) {
            try {
                is = socket.getInputStream();
                byte bytes[] = new byte[1024];
                int len = is.read(bytes);
                if (len != -1) {
                    int all = new Integer(new String(bytes, 0, len));
                    byte bs[] = new byte[all];
                    int l = 0;
                    while (l < all) {
                        byte mbs[] = new byte[1024];
                        int to = is.read(mbs);
                        if (to != -1) {
                            System.arraycopy(mbs, 0, bs, l, to);
                            l += to;
                        }
                    }
                    listener.call(bs);
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    public void close() {
        this.RUN_TIMES = false;
        try {
            is.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
