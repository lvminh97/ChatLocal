/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.videocall;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import localappchat2.network.InfoNet;

/**
 *
 * @author TV.Tinh
 */
public class VideoCall {
    
    BufferedImage frameVideo;
    VideoFrameProcess mCam;
    VideoFrameSender sender;
    VideoFrameReceiver receiver;
    static public Dimension sizeWindow = new Dimension(480, 360);
    static public Dimension sizeCam = new Dimension(120, 90);
    boolean hasFrame = false;
    public boolean isOpen;
    
    public VideoCall(InetAddress target, int port){
        mCam = new VideoFrameProcess(this,sizeWindow,sizeCam);   // resize frame to send and to show
        receiver = new VideoFrameReceiver(this, InfoNet.getLocalInet(), port, sizeWindow);
        sender = new VideoFrameSender(target, port, sizeWindow);
        isOpen = mCam.isOpen & receiver.isOpen & sender.isOpen;
    }
    
    public void start(){
        if(isOpen){
            frameVideo = new BufferedImage(480, 360, BufferedImage.TYPE_3BYTE_BGR);
            mCam.start();
            sender.start();
            receiver.start();
        }
    }
    
    public boolean available(){
        return hasFrame;
    }
    
    public BufferedImage getFrame(){
        if(available()){
            hasFrame = false;
            return frameVideo;
        }
        else{
            return null;
        }
    }
    
    public void handle(String source){
        if(source.equals(VideoFrameProcess.ID)){
            sender.send(mCam.outMatForSend);
            Graphics g = receiver.outIm.getGraphics();
            g.drawImage(mCam.outIm
                    , sizeWindow.width-sizeCam.width
                    , sizeWindow.height-sizeCam.height
                    , null);
            frameVideo = receiver.outIm;
            hasFrame = true;
            g.dispose();
        }
//        if(source.equals(VideoFrameReceiver.ID)){
//            Graphics g = receiver.outIm.getGraphics();
//            g.drawImage(mCam.outIm
//                    , sizeWindow.width-sizeCam.width
//                    , sizeWindow.height-sizeCam.height
//                    , null);
//            frameVideo = receiver.outIm;
//            hasFrame = true;
//            g.dispose();
//        }s
    }
    
     public void close(){
         if(isOpen){
            isOpen = false;
            mCam.close();
            sender.close();
            receiver.close(); 
            
        }
    }
     
}
