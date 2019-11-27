/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.codec;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import localappchat2.commons.Utils;
import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;

/**
 *
 * @author TV.Tinh
 */
public class JCodecVideo extends Thread{
    
    AWTSequenceEncoder enc;
    SeekableByteChannel outFile = null;
    BufferedImage frame;
    Dimension size;
    int timeInterval;
    boolean hasFrame = false;
    public boolean isOpen;
    
    public JCodecVideo(String link, int fps, Dimension size){
        this.timeInterval = 1000/fps;
        this.size = size;
        this.frame = new BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR);
        Rational _fps = new Rational(fps, 1);
        try {
            outFile = NIOUtils.writableFileChannel(link);
            enc = new AWTSequenceEncoder(outFile, _fps);
            isOpen = true;
        } catch (IOException ex) {
            System.out.println("Error to create (JCodecVideo)\n" + ex.getMessage());
        }
    }
    @Override
    public void run(){
        long time = System.currentTimeMillis() + timeInterval;
        while(isOpen){
            if(System.currentTimeMillis() >=time){
                time = System.currentTimeMillis() + timeInterval;
                if(frame == null){
                    frame = new BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR);
                    System.out.println("Null Frame");
                }
                if(!write(frame))
                    finish();
            }
            else{
                Utils.sleep(1);
            }
        }
    }
    
    public boolean write(BufferedImage frame){
        try {
            enc.encodeImage(frame);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    public void setFrameBuffer(BufferedImage im){
        this.frame = im;
    }
    public void setFrameBuffer(Frame window){
        BufferedImage im = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = im.createGraphics();
        window.paint(g);
        g.dispose();
        this.frame = im;
    }
    public void setFrameBuffer(Panel window){
        BufferedImage im = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = im.createGraphics();
        window.paint(g);
        g.dispose();
        this.frame = im;
    }
    public void finish(){
        if(isOpen){
            isOpen = false;
            this.stop();
            try {
                enc.finish();
            } catch (IOException ex) {
            }
            NIOUtils.closeQuietly(outFile);
        }
        System.out.println("Closed (JCodecVideo)");
    }
    
}
