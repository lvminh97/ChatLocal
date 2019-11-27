/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

/**
 *
 * @author TV.Tinh
 */
public class MulticastReceiver extends Thread {
    
    MulticastSocket mcSocket;
    InetAddress group;
    public static int sizeBuf = 20;
    ArrayList<FromNetworkPacket> queueDataOut = new ArrayList<>();
    public static int sizeQueue = 20;
    String fromAddr;
    byte[] byteDataOut;
    boolean hasData = false;
    public boolean isOpen = false;    
    
    public MulticastReceiver(InetAddress inet, int port){
        this.group = inet;
        try{
            mcSocket = new MulticastSocket(port);
            mcSocket.joinGroup(group);
            isOpen = true;
        }catch(IOException ei){
            System.out.println("Error to create (MulticastReceiver)\n"+ei.getMessage());
        }
    }
    
    public synchronized boolean available(){
        return hasData;
    }
    public byte[] getData(){
        if(available()){
            hasData = false;
            return byteDataOut;
        }else{
            return null;
        }
    }
    public String getFromAddress(){
        return fromAddr;
    }
    
    @Override
    public void run(){
        while(isOpen){
            try{
                byte[] buffer = new byte[sizeBuf];
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                mcSocket.receive(dp);
                byteDataOut = java.util.Arrays.copyOf(buffer, dp.getLength());
                fromAddr = dp.getAddress().getHostAddress();
                hasData = true;
                addToQueue(fromAddr, byteDataOut);
//                System.out.println(new String(byteDataOut)); 

            }catch(IOException e){
                System.err.println(e.getMessage());
                close();
            }
        }
    }
    public synchronized boolean queueAvailable(){
        return(queueDataOut.size()>0);
    }
    public FromNetworkPacket getPacket(){
        FromNetworkPacket fwp = queueDataOut.get(0);
        queueDataOut.remove(0);
        return fwp;
    }
    public void setQueueData(ArrayList queue){
        this.queueDataOut = queue;
    }
    private void procQueueOverFlow(){
        if(queueDataOut.size() > sizeQueue){
            queueDataOut.clear();
        }
    }
    private void addToQueue(String addr, byte[] data){
        procQueueOverFlow();
        FromNetworkPacket fwPacket = new FromNetworkPacket(addr, data);
        queueDataOut.add(fwPacket);
    }
    public void close(){
        if(isOpen){
            isOpen = false;
            this.stop();
            try{
                mcSocket.leaveGroup(group);
                mcSocket.close();
            }catch(IOException e){
                System.err.println(e.getMessage());
            }
        } 
    }
    
}