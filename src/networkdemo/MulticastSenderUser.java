/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkdemo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import localappchat2.commons.Utils;

/**
 *
 * @author Windows 7
 */
public class MulticastSenderUser extends Thread{
    
    public MulticastSocket mcSocket = null;
    private InetAddress group;
    private int port = 2019;
    private int ttl = 2;
    private boolean valid = false;
    private String uNameMulcast = "";
    
    public void setUNameMulcast(String uname){
        this.uNameMulcast = uname;
    }
    public boolean isActive(){
        return this.valid;
    }
    public MulticastSenderUser(String group, String port, String ttl){
        try{
            this.group = InetAddress.getByName(group);
            this.port  = Integer.parseInt(port);
            this.ttl   = Integer.parseInt(ttl);
            valid = true;
            
            NetworkInterface netItf = 
                    NetworkInterface.getByInetAddress(
                            InetAddress.getByName(StaticComponent.SITE_LOCAL_IP));
            
            mcSocket = new MulticastSocket();
            mcSocket.setTimeToLive(this.ttl);
            
            if(netItf != null){
                //System.out.println(netItf.getDisplayName());
                SocketAddress sa = new InetSocketAddress(this.group, this.port);
                mcSocket.joinGroup(sa, netItf);
            }else{
                mcSocket.joinGroup(this.group);
            }
            
            System.out.println("Joined in "+this.group.getHostAddress()
                    +":"+this.port);
            
            
        }catch(NumberFormatException|UnknownHostException e){
            System.err.println("Invalid Address or Port !");
        }catch(IOException ei){
            System.err.println(ei);
        }
    }
    
    @Override
    public void run(){
        if(valid){
            long sTime = System.currentTimeMillis()-3000;
            long cTime = 0;
            while(valid){
                cTime = System.currentTimeMillis();
                if(cTime >= sTime + 5000){
                   sTime = System.currentTimeMillis();

                   send(uNameMulcast);
//                   System.out.println("Sent uname to multicast group");
                }
                else{
                    Utils.sleep(1);
                }
            }
        }else{
            System.out.println("Can't Join to a Group");
        }
    }
    public void send(String msg){
        if(valid){
            try{
                byte[] data = msg.getBytes("UTF-8");
                DatagramPacket dp = new DatagramPacket(data, data.length, this.group, this.port);
                mcSocket.send(dp);
                //System.out.println("Sent: " + msg);
            }catch(UnsupportedEncodingException e){
                System.err.println(e);
            }catch(IOException ei){
                System.err.println(ei);
            }
        }else{
            System.out.println("Can't Join to a Group");
        }
    }
    public synchronized void close(){
        valid = false;
        if(mcSocket != null){
            try {
                mcSocket.leaveGroup(this.group); 
                mcSocket.close();
            } catch (IOException ex) {
                //Logger.getLogger(MulticastSender.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println(ex);
            }
        }
    }
    
}
