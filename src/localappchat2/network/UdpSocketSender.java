/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import localappchat2.commons.*;

/**
 *
 * @author TV.Tinh
 */
public class UdpSocketSender extends Thread{
    
    DatagramSocket mUdpSoc;
    InetAddress targetAddr;
    int port;
    byte[] data;
    boolean hasReq = false;
    public boolean isOpen = true;
    
    public UdpSocketSender(InetAddress inet, int port){
        try{
            mUdpSoc = new DatagramSocket();
            this.targetAddr = inet;
            this.port = port;
        }catch(IOException e){
            System.out.println("Error to init (UdpSocketSender)\n" + e.getMessage());
            isOpen = false;
        }
    }
    
    public void send(byte[] data){
        this.data = data;
        hasReq = true;
    }
    
    @Override
    public void run(){
        while(isOpen){
            if(hasReq){
                DatagramPacket pack = new DatagramPacket(data, data.length, targetAddr, port);
                try{
                    mUdpSoc.send(pack);
//                        System.out.println(data.length);
                }catch(IOException e){
                    System.out.println("Error to Send (UdpSocketSender)\n" + e.getMessage());
                    close();
                }
                hasReq = false;
            }
            else{
                Utils.sleep(1);
            }
        }
    }
    
    public void close(){
        while(hasReq){}
        if(isOpen){
            isOpen = false;
            this.stop();
            mUdpSoc.close();
        }
        mUdpSoc = null;
//        System.out.println("Closed (UdpSocketSender)");
    }
    
}
