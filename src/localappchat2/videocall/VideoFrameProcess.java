/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.videocall;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author TV.Tinh
 */
public class VideoFrameProcess extends Thread{
    
    VideoCall parent;
    static String ID = "FrameProcess";
    BufferedImage outIm;
    Mat outMatForShow, outMatForSend;
    Mat in;
    CamCapture cam;
    Size sizeForSend, sizeForShow;
    byte[] dataBuf;
    boolean isOpen;
    
    public VideoFrameProcess(VideoCall p, Dimension sfse, Dimension sfsh){
        this.parent = p;
        cam = new CamCapture();
        isOpen = cam.isOpen;
        sizeForSend = new Size(sfse.width, sfse.height);
        sizeForShow = new Size(sfsh.width, sfsh.height);
        if(isOpen){
            in = cam.capture();
            outMatForShow = new Mat();
            outMatForSend = new Mat();
            outIm = new BufferedImage(sfsh.width, sfsh.height, 
                    (in.channels()==3)?BufferedImage.TYPE_3BYTE_BGR:BufferedImage.TYPE_BYTE_GRAY);
            dataBuf = ((DataBufferByte)(outIm.getRaster().getDataBuffer())).getData();
        }
    }
    @Override
    public void run(){
        while(isOpen){
            in = cam.capture();
            Imgproc.resize(in, outMatForSend, sizeForSend);
            Imgproc.resize(outMatForSend, outMatForShow, sizeForShow);
            outMatForShow.get(0, 0, dataBuf);
            handle();
        }
    }
    private void handle(){
        parent.handle(ID);
    }
    
    public void close(){
        if(isOpen){
            this.stop();
            in.release();
            outMatForSend.release();
            outMatForShow.release();
            cam.close();
        }
        isOpen = false;
    }
}
