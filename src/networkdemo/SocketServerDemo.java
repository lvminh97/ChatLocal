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
import java.util.Scanner;

public class SocketServerDemo extends Thread{
    
    private Socket mSocket;
    public int threadID;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean isClosed = true;
    private String addClient = null;
    
    public SocketServerDemo(Socket soc, int id){
        this.mSocket = soc;
        this.threadID = id;
        if(mSocket.isConnected()){
            try{
                in = new DataInputStream(mSocket.getInputStream());
                out = new DataOutputStream(mSocket.getOutputStream());
                addClient = mSocket.getLocalAddress().toString();
                System.out.println(id + ", New Client: " + addClient);
                isClosed = false;
            }catch(IOException e){            
            }
        }
        else{
            System.out.println("Failed to connect to " 
                    + mSocket.getLocalSocketAddress());
        }
    }
    public void setID(int id){
        this.threadID = id;
    }
    public synchronized void close(){
        isClosed = true;
        if(mSocket != null){
            try{
                mSocket.close();
                System.out.println("Disconnected: "+addClient);
            }catch(IOException e){
                System.err.println("Can't Close Socket "+addClient);
            }
        }
    }
    public synchronized boolean isActive(){
        return !isClosed;
    }
    public synchronized void send(String s){
        if(mSocket.isConnected() && !isClosed){
            try{
                out.writeUTF(s);
                out.flush();
            }catch(IOException e){
                System.err.println("Error to send to"+addClient);
            }
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
                    String s = new String(msg);
                    s = "" + (char)c + s;
                    printMsg(s);
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
