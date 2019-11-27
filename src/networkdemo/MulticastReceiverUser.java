/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkdemo;

import java.io.IOException;
import java.net.*;

/**
 *
 * @author Windows 7
 */
public class MulticastReceiverUser extends Thread{
    
    private UnameBroadCast parent;
    private MulticastSocket mcSocket = null;
    private InetAddress  group;
    private  int port = 0;
    private boolean valid = false;
    
    public MulticastReceiverUser(String ip, String port){
        try{
            this.group = InetAddress.getByName(ip);
            this.port = Integer.parseInt(port);
            valid = true;
            
            mcSocket = new MulticastSocket(this.port);
            mcSocket.joinGroup(this.group);
            
        }catch(NumberFormatException  | UnknownHostException e){
            System.err.println("Invalid address or port !");
        }catch(IOException ei){
            System.err.println(ei);
        }
    }
    public void setHanle(UnameBroadCast p){
        this.parent = p;
    }
    public void handle(String msg){
        parent.handle(msg);
    }
    @Override
    public void run(){
        if(valid){
            while(valid){
                try{
                    
                    byte[] buffer = new byte[StaticComponent.BUFSIZE_MULCAST];
                    DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                    mcSocket.receive(dp);
                    String ip = dp.getAddress().getHostAddress();
                    String name = new String(dp.getData());
                    String s = ip + " " + name;
                    System.out.println(s);
                    
                    handle(s);
                    
                }catch(IOException e){
                    System.err.println(e);
                }
            }
        }
        else{
            System.out.println("Can't Join to a Group");
        }
    }
    public boolean isActive(){
        return this.valid;
    }
    public synchronized void close(){
        valid = false;
        if(mcSocket!=null){
            try{
                mcSocket.leaveGroup(group);
                //mcSocket.close();
            }catch(IOException e){
                System.err.println(e);
            }
        }
        
    }
    
}
