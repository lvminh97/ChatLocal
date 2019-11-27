/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.voicecall;

import java.io.Serializable;
import javax.sound.sampled.AudioFormat;

/**
 *
 * @author TV.Tinh
 */
public class VoicePacket implements Serializable{
    static public AudioFormat defaultAudioFormat=new AudioFormat(11025f, 8, 1, true, true); //11.025khz, 8bit, mono, signed, big endian (changes nothing in 8 bit) ~8kb/s
    static public int size = 4096;
    private byte[] data;

    public VoicePacket(byte[] d){
        this.data = d;
    }
    public byte[] getData(){
        return data;
    }
}
