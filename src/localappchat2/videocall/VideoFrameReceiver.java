/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.videocall;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import javax.imageio.ImageIO;
import localappchat2.network.UdpSocketReceiver;
import localappchat2.commons.*;

/**
 *
 * @author TV.Tinh
 */
public class VideoFrameReceiver extends Thread{
    
    VideoCall parent;
    UdpSocketReceiver udp;
    BufferedImage outIm;
    byte[] receiBuf;
    boolean isOpen;
    static public String ID = "VideoFrameReceiver";
    
    public VideoFrameReceiver(VideoCall p,InetAddress inet, int port, Dimension size){
        
        this.parent = p;
        this.outIm = new BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR);
        
        this.receiBuf =  new byte[outIm.getWidth()*outIm.getHeight()*4/2];     // rgb type unknow buffer size after compress
//         this.receiBuf = new byte[15000];
//        System.out.println(receiBuf.length);

        this.udp = new UdpSocketReceiver(inet, port, receiBuf.length);
        this.isOpen = udp.isOpen;
        if(isOpen){
            udp.start();
        }
    }
    @Override
    public void run(){
        while(isOpen){
            if(udp.available()){
                try {
                    receiBuf = udp.getData();
                    ByteArrayInputStream bais = new ByteArrayInputStream(receiBuf);
                    outIm = ImageIO.read(bais);
                    handle();
                } catch (IOException ex) {
                    System.out.println("Error to receive (FrameReceiver)\n"+ex.getMessage());
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
        System.out.println("Close (VideoFrameReceiver)");
    }
}

