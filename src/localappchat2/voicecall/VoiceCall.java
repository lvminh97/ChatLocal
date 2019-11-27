/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.voicecall;

import java.net.InetAddress;
import localappchat2.network.InfoNet;

/**
 *
 * @author TV.Tinh
 */
public class VoiceCall {
    
    MicRecorder micPcm;
    VoiceFrameReceiver voiceReceiver;
    VoiceFrameSender voiceSender;
    SpeakerPlayer speaker;
    float levelPcmFromMic;
    float levelPcmFromNet;
    float amplification = 1.0f;
    public boolean isOpen;
    
    public VoiceCall(InetAddress target, int port){
        micPcm = new MicRecorder(this);
        voiceSender =  new VoiceFrameSender(this,target,port);
        voiceReceiver = new VoiceFrameReceiver(this, InfoNet.getLocalInet(), port, VoicePacket.size);
        speaker = new SpeakerPlayer();
        isOpen = micPcm.isOpen & voiceReceiver.isOpen & speaker.isOpen;
    }
    
    public void start(){
        if(isOpen){
            voiceReceiver.start();
            voiceSender.start();
            micPcm.start();
            speaker.start();
        }
        else{
            System.out.println("Error to create (VoiceCall)");
        }
    }
    
    public void handle(String source){
        if(source.equals(MicRecorder.ID)){
            byte[] buf = micPcm.getData();
            levelPcmFromMic = getAmplitude(buf);
            voiceSender.send(buf);
//            speaker.write(micPcm.pcmFromMic);
        }
        if(source.equals(VoiceFrameReceiver.ID)){
            levelPcmFromNet = controlAmplitude(voiceReceiver.pcmFromNet, amplification);
            speaker.write(voiceReceiver.pcmFromNet);
        }
    }
    
    public float getMicLevel(){
        return levelPcmFromMic;
    }
    
    public float getFromNetLevel(){
        return levelPcmFromNet;
    }
    
    public float getVolume(){
        return amplification;
    }
    
    public void setVolume(float amplification){
        this.amplification = 
                (amplification < 0.0f)? (0.0f) : ((amplification > 2.0f )? 2.0f : amplification);
//        System.out.println(this.amplification);
    }
    
    public float controlAmplitude(byte[] pcm, float amplifi){
        float ret=0;
        for(int i = 0; i<pcm.length; i++){
            pcm[i] *= amplifi;
            ret = (pcm[i]<0)?(ret-pcm[i]):(ret+pcm[i]);
        }
        ret = 2.0f*ret/pcm.length;
        return ret;
    }
    
    public float getAmplitude(byte[] pcm){
        float ret=0;
        for(int i = 0; i<pcm.length; i++){
            ret = (pcm[i]<0)?(ret-pcm[i]):(ret+pcm[i]);
        }
        ret = 2.0f*ret/pcm.length;
        return ret;
    }
    
    public void close(){
        if(isOpen){
            isOpen = false;
            micPcm.close();
            speaker.close();
            voiceSender.close();
            voiceReceiver.close();
        }
    }
    
}
