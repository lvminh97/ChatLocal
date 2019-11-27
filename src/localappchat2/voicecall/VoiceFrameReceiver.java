/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.voicecall;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.zip.GZIPInputStream;
import localappchat2.network.UdpSocketReceiver;
import localappchat2.commons.*;

/**
 *
 * @author TV.Tinh
 */
public class VoiceFrameReceiver extends Thread{
    
    VoiceCall parent;
    UdpSocketReceiver udp;
    byte[] pcmFromNet;
    public boolean isOpen;
    public boolean hasData=false;
    final static public String ID = "VoiceFrameReceiver";
    
    public VoiceFrameReceiver(VoiceCall p, InetAddress inet, int port, int lenght){
        this.parent = p;
        udp = new UdpSocketReceiver(inet, port, lenght);
        isOpen = udp.isOpen();
    }
    @Override
    public void run(){
        if(isOpen){
            udp.start();
        }
//        long time = System.currentTimeMillis();
        while(isOpen){
            if(udp.available()){
                try {
                    ByteArrayInputStream bis = new ByteArrayInputStream(udp.getData());
                    GZIPInputStream gis = new GZIPInputStream(bis);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buf= new byte[1024];
                    int len;
                    while((len=gis.read(buf,0,buf.length))!= -1){
                        baos.write(buf,0,len);
                    }
                    pcmFromNet = baos.toByteArray();
                    handle();
                } catch (IOException ex) {
                    System.out.println("Error to receiver (VoiceFrameReceiver)\n"+ex.getMessage());
                }
            }
            else{
                Utils.sleep(1);
            }
        }
    }
    
    public void handle(){
        parent.handle(ID);
    }
    
    public void close(){
        if(isOpen){
            isOpen = false;
            this.stop();
            udp.close();
        }
        System.out.println("Close (VoiceFrameReceiver)");
    }
    
}