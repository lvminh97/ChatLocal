/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.voicecall;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import localappchat2.commons.*;

/**
 *
 * @author TV.Tinh
 */
public class MicRecorder extends Thread{
    
    VoiceCall parent;                   // using handle type
    TargetDataLine mic;
    public AudioFormat format;
    byte[] pcmOut;
    byte[] pcmNull;
//    ArrayList<ByteArrayOutputStream> pcmQueue = new ArrayList<>();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//    boolean hasData = false;          // using  avalable check type
    public boolean isOpen;
    static public String ID = "MicRecorder";
    
    public MicRecorder(VoiceCall p){
        this.parent = p;
        format = VoicePacket.defaultAudioFormat;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        isOpen = AudioSystem.isLineSupported(info);
        if(isOpen){
            try {
                mic = (TargetDataLine) AudioSystem.getLine(info);
                mic.open(format);
                pcmOut = new byte[VoicePacket.size];
                pcmNull = new byte[VoicePacket.size];
            } catch (LineUnavailableException ex) {
                System.out.println("Error to create line (MicRecorder)"+ex.getMessage());
                isOpen = false;
            }
        }
        else{
            System.out.println("No valid harware (MicRecorder)");
        }
    }
    
    public byte[] getData(){
//        byte[] buf = pcmQueue.get(0).toByteArray();
//        pcmQueue.remove(0);
//        return buf;
        byte[] buf = baos.toByteArray();
        return buf;
        
    }
    
    @Override
    public void run(){
        if(isOpen)
            mic.start();
        while(isOpen){
            if(mic.available() >= VoicePacket.size){
                while(mic.available() >= VoicePacket.size){
                    mic.read(pcmOut, 0, VoicePacket.size);
                }
                try{
                    baos.reset();
                    baos.write(pcmOut);
//                    pcmQueue.add(baos);
                    handle();
                }catch(IOException e){}
//                hasData = true;
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
            mic.flush();
            mic.close();
        }
        mic = null;
    }
//    public synchronized boolean available(){
//        return hasData;
//    }
    
//    public byte[] getData(){
//        if(available()){
//            hasData = false;
//            return dataOut;
//        }
//        else{
//            return new byte[VoicePacket.size];
//        }
//    }
    
    static public AudioFormat getDefaultFormat(){
        float sampleRate = 11025f;
        int sampleSizeInBits = 8;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        return af;
    }
}
