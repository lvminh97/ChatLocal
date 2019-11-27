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

/**
 *
 * @author TV.Tinh
 */
public class UdpSocketReceiver extends Thread{
    
    DatagramSocket mUdpSoc;
    InetAddress targetAddr;
    int port;
    byte[] data, dataOut;
    boolean hasData = false;
    public boolean isOpen = true;
    
    public UdpSocketReceiver(InetAddress inet, int port, int size){
        try{
            this.targetAddr = inet;
//            String s = targetAddr.getHostAddress();
            this.port = port;
            this.mUdpSoc = new DatagramSocket(this.port, this.targetAddr);
            this.data = new byte[size];
        }catch(IOException e){
            System.out.println("Error to init (UdpSocketReceiver)\n"+e.getMessage());
            System.out.println(inet.getHostAddress());
            isOpen = false;
        }
    }
    
    public boolean isOpen(){
        return isOpen;
    }
    public synchronized boolean available(){
        return hasData;
    }
    public byte[] getData(){
        if(available()){
            hasData = false;
            return dataOut;
        }
        else
            return null;
    }
    @Override
    public void run(){
        while(isOpen){
            try {
                DatagramPacket pack = new DatagramPacket(data, data.length);
                mUdpSoc.receive(pack);
                dataOut = java.util.Arrays.copyOf(data, pack.getLength());
//                System.out.println("recei "+ dataOut.length);
                hasData = true;
            } catch (IOException ex) {
                 System.out.println("Error to receive (UdpSocketReceiver)\n"+ex.getMessage());
            }
        }
    }
    public void close(){
        if(isOpen){
            isOpen = false;
            this.stop();
            mUdpSoc.close();
        }
        mUdpSoc = null;
//        SymUdpSoc = null;stem.out.println("Closed (UdpSocketReceiver)");
    }
        
}
