/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localappchat2;

import java.io.File;
import java.net.InetAddress;
import java.net.URISyntaxException;
import localappchat2.commons.Utils;
import localappchat2.gui.ChatWindow;
import localappchat2.network.InfoNet;
import localappchat2.network.MulticastReceiver;
import localappchat2.network.MulticastSender;
import localappchat2.network.TcpSender;
import localappchat2.network.UdpSocketSender;
import localappchat2.usernamecast.UserNameCast;


/**
 *
 * @author Windows 7
 */
public class LocalAppChat2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("LTNC-20181-Group06 | contact: fb.com/tv.tinh");
        
        System.load("E:\\NetworkDemo\\release\\lib\\opencv_java400.dll");
//        loadNativeLib();      // for release
        
//        String[] arg = {"192.168.1.8","avc"};
        testVideoCall(args);
        
//        testMulticast();
//        testGui();
//        testTcpSender();
        
    }
    
    static public void testVideoCall(String[] args){
        if(args.length >= 2){
            InetAddress targetCall = InfoNet.checkHost(args[0]);
            int type = (args[1].equals("vc"))?(AudioVideoCall.TYP_VOICE_CALL):
                    (AudioVideoCall.TYP_AUDIO_VIDEO_CALL);
            if(targetCall != null){
                AudioVideoCall test = new AudioVideoCall(targetCall, type);
                test.start();
            }
            else{}
        }
        else{
            AudioVideoCall test = new AudioVideoCall(InfoNet.getLoopBackInet(), AudioVideoCall.TYP_AUDIO_VIDEO_CALL);
            test.start();
        }
    }
    
    static public void loadNativeLib(){
        try{
            File fileJar = new File(LocalAppChat2.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String curPathOfJar = fileJar.getPath();
            String nameJar = fileJar.getName();
            String pathLib = curPathOfJar.substring(0, curPathOfJar.length()-nameJar.length());
            System.load(pathLib+"lib\\opencv_java400.dll");
            System.out.println("Load Native Lib from: "+pathLib+"lib\\opencv_java400.dll");
        }catch(URISyntaxException e){
            System.out.println("Cant read the path libs");
        }
    }
    
    static public void testMTReceiver(){
        
        MulticastReceiver mcr = new MulticastReceiver(InfoNet.getMulticastInet(), InfoNet.PORT_MTCAST);
        mcr.start();
        UdpSocketSender us = new UdpSocketSender(InfoNet.getMulticastInet(), InfoNet.PORT_MTCAST);
        us.start();
        
        while(true){
            if(mcr.available()){
                String s = new String(mcr.getData());
                String a = mcr.getFromAddress();
                System.out.println(a+" " +s);
                if(s.equals("esc")){
                    mcr.close();
                    break;
                }
            }
            else{
                Utils.sleep(1);
            }
        }
    }
    
    static public void testMTSender(){
//        MulticastSender us = new MulticastSender(InfoNet.getMulticastInet(), InfoNet.PORT_MTCAST, 2);
        UdpSocketSender us = new UdpSocketSender(InfoNet.getMulticastInet(), InfoNet.PORT_MTCAST);
        us.start();
       long time = System.currentTimeMillis() + 500;
       int cnt = 0;
       while(true){
           if(System.currentTimeMillis()>time){
               time = System.currentTimeMillis() + 500;
               us.send(("test"+cnt).getBytes());
               System.out.println(".");
               cnt++;
               if(cnt == 10){
                   us.close();
                   break;
               }
           }
           else{
               Utils.sleep(1);
           }
       }
    } 
    static public void testMulticast(){
        UserNameCast unc = new UserNameCast();
        unc.start();
    }
    static public void testGui(){
        ChatWindow test = new ChatWindow();
    }
    public static void testTcpSender(){
        TcpSender test = new TcpSender(InfoNet.getLocalInet(), InfoNet.PORT_TCP);
        test.start();
        long time = System.currentTimeMillis() + 500;
       int cnt = 0;
        while(true){
           if(System.currentTimeMillis()>time){
               time = System.currentTimeMillis() + 500;
               test.send(("test"+cnt).getBytes());
               System.out.println(".");
               cnt++;
               if(cnt == 10){
                   test.close();
                   break;
               }
           }
           else{
               Utils.sleep(1);
           }
       }
    }
}
