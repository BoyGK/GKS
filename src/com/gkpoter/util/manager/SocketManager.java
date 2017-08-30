package com.gkpoter.util.manager;

import java.net.Socket;
import java.util.Vector;

public class SocketManager{

    public static Vector<Socket>sockets = new Vector<>();

    public enum Way{
        TCP,
        UDP
    }
}
