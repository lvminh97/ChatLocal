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
import java.io.*;
import java.net.*;
        
public class ServerDemo extends Thread{
    
    private ServerManagerDemo mServer;
    private Socket mSocket;
    public int threadID;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean isClosed = true;
    private String addrClient = null;
    
    
    public ServerDemo(Socket soc, int id){
        this.mSocket = soc;
        this.threadID = id;
        connect();
    }
    
    public ServerDemo(ServerManagerDemo server, Socket soc, int id){
        this.mServer = server;
        this.mSocket = soc;
        this.threadID = id;
        connect();
    }
    
    public final void connect(){
        try{
            mSocket.setSoTimeout(5000);
            in = new DataInputStream(mSocket.getInputStream());
            out = new DataOutputStream(mSocket.getOutputStream());
            String client = mSocket.getRemoteSocketAddress().toString();
            addrClient = client.substring(1, client.indexOf(':'));
            System.out.println(threadID + ", Client: " + addrClient);
            isClosed = false;
        }catch(IOException e){
            System.err.println("Failed to create socket server");
        }
    }
    
    public void setID(int id){        this.threadID = id;
    }
    
    public synchronized void close(){
        isClosed = true;
        try{
            if(mSocket != null){
                mSocket.close();
                mSocket = null;
                System.out.println("Disconnected: "+addrClient);
            }
            if(in != null){
                in.close();
                in = null;
            }
            if(out != null){
                out.close();
                out = null;
            }
        }catch(IOException e){
                System.err.println("Can't Close Socket "+addrClient);
        }
    }
    
    public synchronized boolean isActive(){
        return !isClosed;
    }
    
    public synchronized void send(String s){
        try{
            out.writeUTF(s);
            out.flush();
        }catch(IOException e){
            System.err.println("Error to send to " + addrClient);
        }
    }
    
    @Override
    public void run(){
        
        while(!isClosed){
            try{
                int c = in.read();
                int len = in.available();
                if(c == -1){
                    close();
                }else if(len>0){
                    byte[] msg = new byte[len];
                    in.read(msg);
                    
                    String message = new String(msg);
                    message = message.substring(1);
//                    System.out.println("received: " + s);
                    send("received\n");
                    
                    String show = mSocket.getRemoteSocketAddress().toString();
                    int end = show.indexOf(':');
                    show = show.substring(1,end) + ": " + message;
                    mServer.handle(show);
                    close();
                }
            }catch(IOException e){
            }
        }
    }
    
    public void printMsg(String msg){
        //Scanner sc = new Scanner(in);
        String s = "Client " + Integer.toString(threadID) + " Receiced\n";
        send(s); 
        System.out.print("Client "+threadID+": ");
        System.out.println(msg);
    } 
    
   
}
