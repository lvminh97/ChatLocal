/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.voicecall;

import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author TV.Tinh
 */
public class SpeakerPlayer extends Thread{
    
    SourceDataLine  speaker;
    byte[] pcm;
    byte[] noise = new byte[10];
    int timeOfAFrame;
    boolean hasData;
    public boolean isOpen;
    
    public SpeakerPlayer(){
        AudioFormat format = VoicePacket.defaultAudioFormat;
//        for(int i = 0; i< noise.length;i++) noise[i]=(byte)(Math.random()*3-1);
        timeOfAFrame = (1000*VoicePacket.size)/(int)VoicePacket.defaultAudioFormat.getSampleRate();
        
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        isOpen = AudioSystem.isLineSupported(info);
        if(isOpen){
            try {
                speaker = AudioSystem.getSourceDataLine(format);
                speaker.open(format);
            } catch (LineUnavailableException ex) {
                System.out.println("Error to create line (SpeakerPlayer)"+ex.getMessage());
                isOpen = false;
            }
        }
        else{
           System.out.println("No valid harware (SpeakerPlayer)"); 
        }
    }
    public void write(byte[] pcm){;
        this.pcm = pcm;
        this.hasData = true;
        
    }
    @Override
    public void run(){
        if(isOpen){
            speaker.start();
        }
        long timeStamp = System.currentTimeMillis()+timeOfAFrame;
        while(isOpen){
            if(System.currentTimeMillis() > timeStamp){
                timeStamp = System.currentTimeMillis()+timeOfAFrame;
                    if(hasData){
                        speaker.write(pcm, 0, pcm.length);
                    }
            }
            else{
                writeNosie();
            }
        }
    }
    public void writeNosie(){
        speaker.write(noise, 0, noise.length);
    }
    public synchronized float getLevel(){
        return speaker.getLevel();
    }
    public void close(){
        if(isOpen){
            this.stop();
            speaker.flush();
            speaker.close();
        }
        speaker = null;
        isOpen = false;
    }
    
}