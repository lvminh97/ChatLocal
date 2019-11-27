/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import localappchat2.commons.Utils;

/**
 *
 * @author TV.Tinh
 */
public class MulticastSender extends Thread{
    
    MulticastSocket mcSocket;
    InetAddress group;
    int port;
    byte[] data;
    boolean hasData = false;
    public boolean isOpen;
    
    public MulticastSender(InetAddress inet, int port, int ttl){
         try{
            this.group = inet;
            this.port = port;
            mcSocket = new MulticastSocket();
            mcSocket.setTimeToLive(ttl);
            
//            NetworkInterface netItf = NetworkInterface.getByInetAddress(InfoNet.getLocalInet());
//            SocketAddress sa = new InetSocketAddress(inet, port);
//            if(netItf != null){
//                mcSocket.joinGroup(sa, netItf);
//                System.out.println(mcSocket.getLocalAddress());
//            }else{
//                mcSocket.joinGroup(group);
//            }
            
            isOpen = true;
            System.out.println("Joined in: "+this.group.getHostAddress()+":"+port);
        }catch(IOException e){
            System.out.println("Error to create (MulticastSender)\n"+e.getMessage());
        }
    }
    
    public void send(byte[] data){
        this.data = data;
        hasData = true;
    }
    
    @Override
    public void run(){
        while(isOpen){
            if(hasData){
                try {
                    DatagramPacket pack = new DatagramPacket(data, data.length, group, port);
                    mcSocket.send(pack);
//                    System.out.println("MT send:"+pack.getLength());
                } catch (IOException ex) {
                    System.out.println("Error to Send (MulticastSender)\n" + ex.getMessage());
                    close();
                }
                hasData = false;
                
            }else{
                Utils.sleep(1);
            }
        }
    }
    
    public void close(){
        while(hasData){}
        if(isOpen){
            isOpen = false;
            this.stop();
            mcSocket.close();
        }
    }
    
}
