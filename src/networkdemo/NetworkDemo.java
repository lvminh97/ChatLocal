/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkdemo;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import localappchat2.LocalAppChat2;
import localappchat2.commons.Utils;

/**
 *
 * @author Windows 7
 */
public class NetworkDemo {
    
    public static void main(String[] args){
        LoginWindow loginwind = new LoginWindow();
        loginwind.show();
        while(loginwind.getRes() == 0){}
        if(loginwind.getRes() == 1){
            loginwind.hide();
            System.load("E:\\NetworkDemo\\release\\lib\\opencv_java400.dll");
    //        loadNativeLib();      // for release
            InforNetwork infoNet = new InforNetwork();


            NetworkDemoGUI gui = new NetworkDemoGUI();

            ServerManagerDemo server = new ServerManagerDemo(gui.chatPanel,infoNet);
            server.start();

            String siteLocal = infoNet.SITE_LOCAL_IP;
            StaticComponent.SITE_LOCAL_IP = siteLocal;

            UnameBroadCast broadcast = new UnameBroadCast(infoNet.MAC_ADDRESS);
            broadcast.setHandle(gui.friendsList);
            broadcast.start();
        }
        
//        infoNet.getAllNetworkInterface();    
//        Ping ping = new Ping("google.com",8);
//        Ping ping = new Ping("192.168.0.102",7);
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
}
