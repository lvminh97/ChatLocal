/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import localappchat2.commons.Utils;

/**
 *
 * @author TV.Tinh
 */
public class TcpSender extends Thread{
    
    Socket mSocket;
    InetAddress targetAddr;
    int port;
    DataOutputStream dos;
    DataInputStream dis;
    byte[] data;
    boolean hasData;
    boolean hasDataToSend = false;
    public boolean isOpen;
    
    public TcpSender(InetAddress inet, int port){
        mSocket = new Socket();
        this.targetAddr = inet;
        this.port = port;
        isOpen = true;
    }
    public synchronized boolean available(){
        return hasData;
    }
    public void send(byte[] data){
        hasDataToSend = true;
        this.data = data;
    }
    @Override
    public void run(){
        if(isOpen){
            try {
                mSocket.connect(new InetSocketAddress(targetAddr,port));
                dos = new DataOutputStream(mSocket.getOutputStream());
                dis = new DataInputStream(mSocket.getInputStream());
            } catch (IOException ex) {
                System.out.println("Error to create (TcpSender)\n"+ex.getMessage());
            }
        }
        while(isOpen){
            try {
                if(dis.available()>0){
                    dis.read();
                }
                else if(hasData){
                    dos.write(data);
                    dos.flush();
                    System.out.println(data.length);
                    hasData = false;
                }else{
                    Utils.sleep(1);
                } 
            } catch (IOException ex) {
                close();
            }  
        }
    }
    
    public void close(){
        if(isOpen){
            isOpen = false;
            this.stop();
            try {
                mSocket.close();
                dos.close();
                dis.close();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
