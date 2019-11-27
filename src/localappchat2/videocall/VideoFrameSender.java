/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.videocall;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import javax.imageio.ImageIO;
import localappchat2.network.UdpSocketSender;
import org.opencv.core.Mat;
import localappchat2.commons.*;

/**
 *
 * @author TV.Tinh
 */
public class VideoFrameSender extends Thread{
    
    Mat in;
    UdpSocketSender udp;
    byte[] byteBuf;
    BufferedImage imBuf;
    boolean hasData = false;
    boolean isOpen;
    
    public VideoFrameSender(InetAddress inet, int port, Dimension s){
            udp = new UdpSocketSender(inet, port);
            isOpen = udp.isOpen;
            if(isOpen){
                udp.start();
                in = new Mat();
                imBuf = new BufferedImage(s.width, s.height, BufferedImage.TYPE_3BYTE_BGR);
                byteBuf = ((DataBufferByte)(imBuf.getRaster().getDataBuffer())).getData();
            }
    }
    public void send(Mat frame){
        in = frame;
        hasData = true;
    }
    
    @Override
    public void run(){
        while(isOpen){
            synchronized(this){
                if(hasData){
                    try {
                        in.get(0, 0, byteBuf);      // bufferedimage covert
                        
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(imBuf, "jpg", baos);
                        baos.flush();
                        byte buf[] = baos.toByteArray();
                        udp.send(buf);
                        
//                        System.out.println(buf.length);
                    } catch (IOException ex) {
                        System.out.println("Error to compress (FrameSender)\n"+ex.getMessage());
                    }
                    hasData = false;
                }
                else{
                    Utils.sleep(1);
                }
            }
        }
    }
    
    public void close(){
        if(isOpen){
            isOpen = false;
            this.stop();
            in.release();
            udp.close();
        }
         System.out.println("Close (VideoFrameSender)");
    }
    
}
