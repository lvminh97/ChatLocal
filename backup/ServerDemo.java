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
        
public class ServerDemo extends Thread{
    
    private int mPort = 2018;
    private ServerSocket mServer;
    private Socket mSocket;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private Scanner mScan = null;
    
    private boolean isClose = true;
    private boolean isDiscon = true;
    
    
    public synchronized void setClose(boolean bool){
        this.isClose = bool;
        if(mServer != null && bool == true){
            try{
                mServer.close();
            }catch(IOException e){
            }
        }
    }
    
    public synchronized void setDisconnect(boolean bool){
        this.isDiscon = bool;
        if(mSocket != null && bool == true){
            try{
                mSocket.close();
            }catch(IOException e){
            }
        }
        
    }
    
    private void createServer(){
        setClose(true);
        try{
            this.mServer = new ServerSocket(mPort);
        }catch(IOException e){
            System.err.println("Can't init the server at port " + mPort);
        }
        setClose(false);
    }
    
    public ServerDemo(int port){
        this.mPort = port;
        createServer();
    }
    
    @Override
    public void run(){
        
        while(!isClose){
            //show the IP Server
            if(isDiscon){
                if(mServer.isBound()){
                    System.out.println("Server IP: " 
                            + mServer.getInetAddress()
                            +":"+mServer.getLocalPort());
                }
                else{
                    createServer();
                }
                setDisconnect(false);
            }
            //listening the connect req
            try{
                mSocket = mServer.accept();
                }catch(IOException e){
                    if(!isClose){
                        System.err.println("Can't accept connection");
                        setClose(true);
                        setDisconnect(true);
                    }
                    continue;
            }
            //begin receive from socket
            startSocketRx();
        }
    }
    
    private void startSocketRx(){
        //show the IP client
        System.out.println("New Connection: "
                + mSocket.getRemoteSocketAddress());
        //init the data stream
        try{
            in = new DataInputStream(mSocket.getInputStream());
            out = new DataOutputStream(mSocket.getOutputStream());
            mScan = new Scanner(in);
        }catch(IOException e){
        }
        //receive data
        while(!isDiscon){
            try{
                int c = in.read();
                if(c == -1){
                    setDisconnect(true);
                    System.out.println("Disconnected");
                }
                else if(mScan.hasNextLine()){
                    String s = "" + (char)c;
                    s += mScan.nextLine();
                    System.out.println(s);
                    send("received\n");
                }
            }catch(IOException e){
                if(!isDiscon){
                    System.out.println("Client Connection Failed");
                }else{
                    
                }
                setDisconnect(true);
            }
        }
    }
    
    public void send(String msg){
        if(mSocket.isConnected()){
            try{
                out.writeUTF(msg);
                out.flush();
            }catch(IOException e){
                System.out.println("Failed to send");
            }
        }
        else{
            setDisconnect(true);
            System.out.println("No Client");
        }
    }
    
   
}
