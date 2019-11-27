/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkdemo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 *
 * @author Windows 7
 */
public class Ping {
    private String hostAddress = "Unknown";
    private int portHost = 80;
    private String rtt = "Timeout";
    private static final int TIMEOUT = 5000; //millis
    
    //ping using layer 4
    @SuppressWarnings("empty-statement")
    public Ping(String host, int port){
        System.out.println("Ping to: "+host+":"+port);
        SocketChannel socChan = null;
        InetAddress inet = null;
        
        try{
            inet = InetAddress.getByName(host);
            InetSocketAddress inetSoc = new InetSocketAddress(inet, port);
            socChan = SocketChannel.open();
            socChan.configureBlocking(false);
            socChan.connect(inetSoc);
            
            long time = System.currentTimeMillis()+5000;
            while(System.currentTimeMillis() < time ){
                if(socChan.finishConnect() == true){
                    time = System.currentTimeMillis() - (time-5000);
                    this.rtt = Long.toString(time);
                    break;
                }
            }
            
//            String s = "Hello Server";
//            ByteBuffer b = ByteBuffer.wrap(s.getBytes());
//            socChan.write(b);

            //blocking mode
//            long time = System.currentTimeMillis();
//            if(socChan.connect(inetSoc)){
//                time = System.currentTimeMillis() - time;
//                this.rtt = Long.toString(time);
//            }
            
        }catch(UnknownHostException e){ 
            System.out.println("unkown host");
        }catch(IllegalArgumentException | IOException ei){
            System.out.println("failed to connect");
        }finally{
            try{
                if(socChan != null){
                    socChan.close();
                }
            }catch(IOException e){
            }
        }
        
        System.out.println("response time: "+ this.rtt);
    }
    
    public Ping(String host){
        System.out.println("Ping to: "+host);
        InetAddress inet = null;
        
        try{
            inet = InetAddress.getByName(host);
            long time = System.currentTimeMillis();
            if(inet.isReachable(TIMEOUT)){
                time = System.currentTimeMillis() - time;
                this.rtt = Long.toString(time);
            }
        }catch(UnknownHostException e){
            
        }catch(IOException ei){
            
        }
        System.out.println("response time: "+ this.rtt);
    }
    
}
