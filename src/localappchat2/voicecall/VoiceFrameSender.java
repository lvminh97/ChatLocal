/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.voicecall;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.zip.GZIPOutputStream;
import localappchat2.commons.Utils;
import localappchat2.network.UdpSocketSender;

/**
 *
 * @author TV.Tinh
 */
public class VoiceFrameSender extends Thread{
    
    VoiceCall parent;
    UdpSocketSender udp;
    byte[] pcmFromMic;
    boolean hasData = false;
    public boolean isOpen;
    final static public String ID = "VoiceFrameSender";
    
    public VoiceFrameSender(VoiceCall p, InetAddress inet, int port){
        this.parent = p; 
        udp = new UdpSocketSender(inet, port);
        isOpen = udp.isOpen;
    }
    
    public void send(byte[] pcm){
        this.pcmFromMic = pcm;
        hasData = true;
    }
    
    @Override
    public void run(){
        if(isOpen){
            udp.start();
        }
        while(isOpen){
            if(hasData){
                GZIPOutputStream gos = null;
                try {
                    ByteArrayOutputStream zipbaos = new ByteArrayOutputStream();
                    gos = new GZIPOutputStream(zipbaos);
                    gos.write(pcmFromMic);
                    gos.flush();
                    gos.close();
                    zipbaos.flush();
                    zipbaos.close();
                    udp.send(zipbaos.toByteArray());
//                    System.out.println("send:" + zipbaos.size());
                } catch (IOException ex) {
                    System.out.println("Error to send (VoiceFrameSender)\n"+ex.getMessage());
                    close();
                }
                hasData = false;
            }
            else{
                Utils.sleep(1);
            }
        }
    }
    
    public void close(){
        if(isOpen){
            isOpen = false;
            this.stop();
            udp.close();
        }
        System.out.println("Close (VoiceFrameSender)");
    }
}