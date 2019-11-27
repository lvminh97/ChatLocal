/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import localappchat2.AudioVideoCall;
import localappchat2.videocall.VideoCall;

/**
 *
 * @author TV.Tinh
 */
public class AudioVideoCallFrame{
    
    AudioVideoCall parent;
    public Frame window;
    public Panel videoFrame;
    public JProgressBar fromMicLevel, fromNetLevel;
    public JSlider     volume;
    public Graphics graph;
    public int event=0;
    
    public static final String ID = "AudioVideoCallFrame";
    public static final int EVT_CLS = 1;
    public static final int EVT_SET_VOL = 2;
    
    public AudioVideoCallFrame(AudioVideoCall callClass, String title, int TYPE){
        this.parent = callClass;
        if(TYPE == AudioVideoCall.TYP_VOICE_CALL){
            initVoiceCall(title);
        }else if (TYPE == AudioVideoCall.TYP_AUDIO_VIDEO_CALL){
            initAvCall(title);
        }
    }
    
    public void handle(){
        parent.handle(ID);
    }
    
    public void close(){
        window.dispose();
    }
    
    private void initVoiceCall(String title){ 
        Dimension sizeVolandLev = new Dimension(250, 20);
        Dimension sizeWindow = new Dimension(sizeVolandLev.width, sizeVolandLev.height+30);
        float sw = ThemeManager.getScaleWidth();
        float sh = ThemeManager.getScaleHeight();
        sizeVolandLev = new Dimension((int)(sizeVolandLev.width*sw), (int)(sizeVolandLev.height*sh));
        sizeWindow = new Dimension((int)(sizeWindow.width*sw), (int)(sizeWindow.height*sh));
        
        window = new Frame(title);
        window.setLayout(null);
        window.setLocation(200,200);
        window.setSize(sizeWindow);
        window.setResizable(false);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                event = EVT_CLS;
                handle();
            }
        });
        
        initVoiceGui(sizeVolandLev, sizeWindow);
        
        window.add(volume);
        window.add(fromMicLevel); 
        window.add(fromNetLevel);
        
        window.setVisible(true);
    }
    
    private void initAvCall(String title){
        Dimension sizeWindow = new Dimension(VideoCall.sizeWindow.width, VideoCall.sizeWindow.height); 
        Dimension sizeVolandLev = new Dimension(sizeWindow.width, 20);
        float sw = ThemeManager.getScaleWidth();
        float sh = ThemeManager.getScaleHeight();
        sizeVolandLev = new Dimension((int)(sizeVolandLev.width), (int)(sizeVolandLev.height*sh));
        sizeWindow = new Dimension(sizeWindow.width, sizeWindow.height+sizeVolandLev.height);
        
        window = new Frame(title);
        window.setLayout(null);
        window.setLocation(200,200);
        window.setSize(sizeWindow);
        window.setResizable(false);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                event = EVT_CLS;
                handle();
            }
        });
        
        initVoiceGui(sizeVolandLev, sizeWindow);
        
        videoFrame = new Panel();
        videoFrame.setLocation(0,0);
        videoFrame.setSize(VideoCall.sizeWindow);
        
        window.add(videoFrame);
        window.add(volume);
        window.add(fromMicLevel); 
        window.add(fromNetLevel);
        
        window.setVisible(true);
        graph = (Graphics) videoFrame.getGraphics();
    }
    
    private void initVoiceGui(Dimension sizeVolandLev, Dimension sizeWindow){
        int hLev = sizeVolandLev.height; int wLev = sizeVolandLev.width/4;
        int hVol = sizeVolandLev.height; int wVol = sizeVolandLev.width/2;
        
        volume = new JSlider();
        volume.setLocation(0,sizeWindow.height-hVol);
        volume.setSize(wVol, hVol);
        volume.setBackground(Color.LIGHT_GRAY);
        volume.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                event = EVT_SET_VOL;
                handle();
            }
        });  
        
        fromNetLevel = new JProgressBar();
        fromNetLevel.setLocation(wVol,sizeWindow.height-hLev);
        fromNetLevel.setSize(wLev,hLev);
        fromNetLevel.setForeground(Color.ORANGE);
        
        fromMicLevel = new JProgressBar();
        fromMicLevel.setLocation(wVol+wLev, sizeWindow.height-hLev);
        fromMicLevel.setSize(wLev, hLev);
    }
    
}
