/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2;

import java.awt.image.BufferedImage;
import java.net.InetAddress;
import localappchat2.codec.JCodecVideo;
import localappchat2.commons.*;
import localappchat2.gui.AudioVideoCallFrame;
import localappchat2.network.InfoNet;
import localappchat2.videocall.VideoCall;
import localappchat2.voicecall.VoiceCall;

/**
 *
 * @author TV.Tinh
 */
public class AudioVideoCall extends Thread{
    
    AudioVideoCallFrame gui;
    VideoCall videocall;
    VoiceCall voicecall;
    JCodecVideo recorder;
    boolean isOpen;
    
    public static int TYP_AUDIO_VIDEO_CALL = 1;
    public static int TYP_VOICE_CALL = 2;
    
    public AudioVideoCall(InetAddress targetAddr, int type){
        String targetIp = targetAddr.getHostAddress();
        String title = "Audio Call - "+targetIp;
        voicecall = new VoiceCall(targetAddr, InfoNet.PORT_VOICE_CALL);
        isOpen = voicecall.isOpen;
        if(type==TYP_AUDIO_VIDEO_CALL){
            videocall = new VideoCall(targetAddr, InfoNet.PORT_VIDEO_CALL);
            isOpen = isOpen&videocall.isOpen;
            title = "Audio & Video Call - " + targetIp;
        }
        if(isOpen){
            System.out.println("Your IP: "+InfoNet.getLocalInet().getHostAddress()
                    +"\n"+title);
            gui = new AudioVideoCallFrame(this, title, type);
        }
    }

    @Override
    public void run(){
        if(isOpen){
            if(videocall != null){
                videocall.start();
                recorder = new JCodecVideo("E:\\NetworkDemo\\test"+Utils.getTime()+".mp4", 8, gui.videoFrame.getSize());
                recorder.start();
            }
            voicecall.start();
        }else{
            System.out.println("Error to create (AudioVideoCall)");
            close();
        }
        
        long time = System.currentTimeMillis()+50;
        while(isOpen){
//            isOpen = (videocall!=null)?(videocall.isOpen&voicecall.isOpen):(voicecall.isOpen);
//            if(isOpen == false) close();
            if(System.currentTimeMillis()>=time){
                float level = voicecall.getFromNetLevel();
                gui.fromMicLevel.setValue((int) voicecall.getMicLevel());
                gui.fromNetLevel.setValue((int) level);
            }
            else{
                Utils.sleep(1);
            }
            if((videocall != null)&&videocall.available()){
                BufferedImage video = videocall.getFrame();
                gui.graph.drawImage(video, 0, 0, gui.videoFrame);
                recorder.setFrameBuffer(video);
            }
            else{
                Utils.sleep(1);
            }
        }
    }
    
    public void handle(String source){
        if(source.equals(AudioVideoCallFrame.ID)){
            if(gui.event == AudioVideoCallFrame.EVT_CLS){
                close();
            }
            if(gui.event == AudioVideoCallFrame.EVT_SET_VOL){
                setVolume();
            }
        }
    }
    
    private void close(){
        if(isOpen){
            isOpen = false;
            this.stop();
            voicecall.close();
            if(videocall != null)
                videocall.close();
            if(recorder != null)
                recorder.finish();
            gui.close();
            
        }
    }
    
    private void setVolume(){
        float level = (float)(2.0*(gui.volume.getValue())/gui.volume.getMaximum());
        voicecall.setVolume(level);
    }
    
//    public AudioVideoCall(int type){
//        String title = "Audio Call";
//        voicecall = new VoiceCall(InfoNet.getLoopBackInet(), InfoNet.PORT_VOICE_CALL);
//        isOpen = voicecall.isOpen;
//        if(type==TYP_AUDIO_VIDEO_CALL){
//            videocall = new VideoCall(InfoNet.getLoopBackInet(), InfoNet.PORT_VIDEO_CALL);
//            isOpen = isOpen&videocall.isOpen;
//            title = "Audio & Video Call";
//        }
//        if(isOpen){
//            System.out.println("Your IP: "+InfoNet.getLocalInet().getHostAddress()
//                    +"  Target IP: "+InfoNet.getLoopBackInet().getHostAddress());
//            gui = new AudioVideoCallFrame(this, title, type);
//        }
//    }
}
