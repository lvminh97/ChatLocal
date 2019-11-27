/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkdemo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Windows 7
 */
public class PingDemo {
    
    private String hostName = "Unknown";
    private String hostAddress = "Error";
    private String rt = "timeout";
    
    public PingDemo(String host, boolean debug){
        try {
            if(debug == true){
                System.out.println("Send req to " + host);
            }
            
            long time = System.currentTimeMillis();
            InetAddress inet = InetAddress.getByName(host);
            time = (System.currentTimeMillis()-time);
            
            this.rt = Long.toString(time);
            this.hostAddress = inet.getHostAddress();
            this.hostName = inet.getHostName();
            
            if(debug == true){
                System.out.println("Host Reachable: "+ inet.getHostName() 
                        + " | " + inet.getHostAddress());
                System.out.println("routime: "+rt);
                //System.out.format(" time: %.2f\n", time/1000000.0);
            }
        } catch (UnknownHostException ex) {
            //Logger.getLogger(PingDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public PingDemo(String host){
        
        try {
            long time = System.nanoTime();
            InetAddress inet = InetAddress.getByName(host);
            time = (System.nanoTime()-time);
            int t = (int)time;
            this.rt = Integer.toString(t);
            this.hostAddress = inet.getHostAddress();
            this.hostName = inet.getHostName();
        } catch (UnknownHostException ex) {
            //Logger.getLogger(PingDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
