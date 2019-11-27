/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkdemo;

/**
 *
 * @author Windows 7
 */
import java.awt.List;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class ServerManagerDemo extends Thread{
    
    private ChatPanel chatPanel;
    private ServerSocket mServer;
    private ArrayList<ServerDemo> mSocketList;
    private int mPort = 2018;
    private int mBackLog = 30;
    private boolean isClosed = true;
    private String siteLocal = "";
    
    public synchronized void close(){
        isClosed = true;
        if(mSocketList.size() > 0){
            for (ServerDemo mSocket : mSocketList) {
                mSocket.close();
            }
            mSocketList.removeAll(mSocketList);
        }
        if(mServer != null){
            try{
                mServer.close();
            }catch(IOException e){
            }
        }
    }
    
    private void createServer(){
        close();
        try{
            //InetAddress siteLocal = getSiteLocalAddress();
            //System.out.println(siteLocal.getHostAddress());
            this.mServer = new ServerSocket(mPort, mBackLog, InetAddress.getByName(siteLocal));
            isClosed = false;
        }catch(IOException e){
            System.err.println("Can't init the server at port " + mPort);
        }
    }
    
    public ServerManagerDemo(int port){
        this.mPort = port;
        mSocketList = new ArrayList<>();
        createServer();
    }
    
    public ServerManagerDemo(ChatPanel panel, InforNetwork in){
        this.chatPanel = panel;
        this.mPort = in.PORT_TCP;
        this.siteLocal = in.SITE_LOCAL_IP;
        
        mSocketList = new ArrayList<>();
        createServer();
    }
    
    public void checkConnection(){
        for( int i = 0; i< mSocketList.size(); i++){
            if(!mSocketList.get(i).isActive()){
                mSocketList.remove(i);
            }
        }
        for(int i = 0; i< mSocketList.size(); i++){
            mSocketList.get(i).setID(i);
        }
    }
    
    @Override
    public void run(){
        System.out.println("Server:\t"+mServer.getInetAddress().getHostAddress()
                + "\nPort:\t"+ mServer.getLocalPort());
        while(!isClosed){
            try{
                Socket socket = mServer.accept();
                checkConnection();
                ServerDemo soc = new ServerDemo(this, socket, mSocketList.size());
                mSocketList.add(soc);
                soc.start();
            }catch(IOException e){
                isClosed = true;
                System.err.println("Can't accept a connection");
            }
        }
    }
    
    public void handle(String s){
        chatPanel.mesage.add(s);
    }
    
    /*
    public InetAddress getSiteLocalAddress(){
        InetAddress inetAddr = null;
        try{
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements())
            {
                NetworkInterface n = (NetworkInterface) e.nextElement();
//                byte[] mac = n.getHardwareAddress();
//                if(mac!= null){
//                for(byte ele : mac){
//                    int a = (ele < 0)?(256+ele):(ele);
//                    System.out.print(Integer.toHexString(a) + "-");
//                    System.out.print((int)a + " ");
//                }System.out.println();
//                }
                
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements())
                {
                    inetAddr = (InetAddress) ee.nextElement();
                   // System.out.println(inetAddr);
                    if(inetAddr.isSiteLocalAddress() && n.isUp()){
                        byte[] mac = n.getHardwareAddress();
                        String macadd = "";
                        for(byte m:mac){
                            int a = (m < 0)?(256+m):(m); 
                            macadd += Integer.toHexString(a);
                            macadd += (m == mac[mac.length-1])? "":"-";
                        }
                        System.out.println(macadd);
                        return inetAddr;
                    }
                }
            }
        }catch(SocketException e){}
        
        try{
            inetAddr = InetAddress.getByName("0.0.0.0");
        }catch(UnknownHostException e){}
        
        return inetAddr;
    }
    */
    
}
