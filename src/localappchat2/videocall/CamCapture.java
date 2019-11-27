/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.videocall;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author TV.Tinh
 */
public class CamCapture {
    
    VideoCapture cam;
    Mat frame;
    boolean isOpen;
    
    public CamCapture(){
        frame = new Mat();
        cam = new VideoCapture(0);
        isOpen = cam.read(frame);
        if(isOpen){
            
        }
    }
    public Mat capture(){
        if(isOpen){
            cam.read(frame);
            return frame;
        }
        else return null;
    }
    public void close(){
        if(isOpen){
            cam.release();
            frame.release();
        }
    }

}
