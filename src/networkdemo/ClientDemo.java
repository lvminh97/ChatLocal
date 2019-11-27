/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkdemo;

/**
 *
 * @author Windows 7
 */

import java.net.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class ClientDemo extends Thread{
    
    private ChatPanel parent;
    private static ClientDemo mClient;
    private String servAdd;
    private int servPort = 2018;
    private Socket mSocket;
    private DataOutputStream out;
    private DataInputStream in;
    private Scanner sc;
    private boolean isClosed = true;
    
    public ClientDemo(String server, int port){
        this.servAdd = server;
        this.servPort = port;  
        connect();
    }
    
    public ClientDemo(ChatPanel panel ,String server, int port){
        this.parent = panel;
        this.servAdd = server;
        this.servPort = port;  
        connect();
    }
    
    public synchronized ClientDemo handle(ChatPanel panel, String server, int port){
        this.parent = panel;
        this.servAdd = server;
        this.servPort = port;
        close();
        mClient = new ClientDemo(servAdd, servPort);
        connect();
        return mClient;
    }
    
    public final void connect() {
        try{
            mSocket = new Socket();
            mSocket.setSoTimeout(3000);
            mSocket.connect(new InetSocketAddress(servAdd, servPort), 2000);
            in = new DataInputStream(mSocket.getInputStream());
            out = new DataOutputStream(mSocket.getOutputStream());
            isClosed = false;
        }catch(IOException e){
            System.out.println("Can't connect to this server");
            parent.mesage.add("Failed");
        }
    }
    
    @Override
    public void run(){
        while(!isClosed){
            try{
                int c = in.read();
                int len = in.available();
                if(c == -1){
                    parent.mesage.add("Failed");
                    close();
                }else if(len>0){
//                    byte[] msg = new byte[len];
//                    in.read(msg);
//                    String s = new String(msg);
//                    s = "" + (char)c + s;                    
                    parent.mesage.add("Sent");
                    close();
                }
            }catch(IOException e){
            }
        }
    }
    
    public void send(String msg){
        try{
            out.writeUTF(msg);
            out.flush();
        }catch(IOException e){
            System.err.println("Failed to socket send");
        }
    }
    
    public synchronized void close(){
        isClosed = true;
        try{
            if(mClient != null){
                mClient.close();
                mSocket = null;
            }
            if(mSocket != null){
                mSocket.close();
                mSocket = null;
            }
            if(in!=null){
                in.close();
                in = null;
            }
            if(out!=null){
                out.close();
                out = null;
            }
        }catch(IOException e){
        }
    }
}
