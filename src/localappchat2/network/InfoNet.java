/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 *
 * @author TV.Tinh
 */
public class InfoNet {
    public static String SITE_LOCAL_IP = "0.0.0.0";
    public static String MAC_ADDRESS   = "";
    public static String MAC_ETH_ADDRESS = "";
    public static int PORT_TCP   = 2018;
    public static final String NO_ADDRESS = "NO_ADDRESS";
    
    public static String GROUP_MTCAST = "224.2.2.2";
    public static int PORT_MTCAST = 2019;
    public static int TTL_MULCAST  = 1;
    
    public static int PORT_VIDEO_CALL = 2020;
    public static int PORT_VOICE_CALL = 2021;
    
    
    public static final byte[][] VIRTUAL_MAC = {
        {0x00,0x50,0x56},   //VMWare
        {0x00,0x0C,0x29},   //VMWare
        {0x00,0x05,0x69},   //VMWare
        {0x0A,0x00,0x27},   //VirtualBox
        {0x08,0x00,0x27},   //VirtualBox
    };
    
    public static InetAddress MulticastInet = null;
    public static InetAddress LocalInet = null;
    public static InetAddress LoopBackInet = null;
    static boolean hadRun = false;
    
    public InfoNet(){
        getAllMacAddress();
//        System.out.println("MAC_WLAN: " + MAC_ADDRESS);
//        System.out.println("MAC_ETH:  " + MAC_ETH_ADDRESS);
//        System.out.println("IP:       " + SITE_LOCAL_IP);
        hadRun = true;
        try{
            MulticastInet = InetAddress.getByName(GROUP_MTCAST);
            LocalInet = InetAddress.getByName(SITE_LOCAL_IP);
            if(SITE_LOCAL_IP.equals("0.0.0.0")){
                LoopBackInet = InetAddress.getByName("127.0.0.1");
            }
            else{
                LoopBackInet = LocalInet;
            }
        }catch(UnknownHostException e){}
    }
    static public InetAddress getMulticastInet(){
        if(!hadRun){
            InfoNet in = new InfoNet();
        }
        return MulticastInet;
    }
    static public InetAddress getLocalInet(){
        if(!hadRun){
            InfoNet in = new InfoNet();
        }
        return LocalInet;
    }
    
    static public InetAddress getLoopBackInet(){
        if(!hadRun){
            InfoNet in = new InfoNet();
        }
        return LoopBackInet;
    }
    
    static public InetAddress checkHost(String host){
        InetAddress inet = null;
        try {
            inet = InetAddress.getByName(host);
        } catch(UnknownHostException uhe) {
        }
        return inet;
    }
    
    private boolean getAllMacAddress(){
        
        try {
            Enumeration en = NetworkInterface.getNetworkInterfaces();
            while(en.hasMoreElements()){
                NetworkInterface ni = (NetworkInterface) en.nextElement();
                String name = ni.getName();
                if(name.indexOf("wlan") == 0){
                    String mac = getMacAddr(ni);
                    if(!mac.equals(NO_ADDRESS)){
                        MAC_ADDRESS = (MAC_ADDRESS.equals(""))?mac:MAC_ADDRESS;
                        String ip = getLocalAddr(ni);
                        if(!ip.equals(NO_ADDRESS)){
                            SITE_LOCAL_IP = (SITE_LOCAL_IP.equals("0.0.0.0"))?
                                    ip:SITE_LOCAL_IP;
                        }
                    }
                }
                if(name.indexOf("eth") == 0){
                    String mac = getMacAddr(ni);
                    if(!mac.equals(NO_ADDRESS)){
                        MAC_ETH_ADDRESS = (MAC_ETH_ADDRESS.equals(""))?mac:MAC_ETH_ADDRESS;
                        String ip = getLocalAddr(ni);
                        if(!ip.equals(NO_ADDRESS)){
                            SITE_LOCAL_IP = (SITE_LOCAL_IP.equals("0.0.0.0"))?
                                    ip:SITE_LOCAL_IP;
                        }
                    }
                }
            }
            
        } catch (SocketException ex) {
            System.out.println("no network");
            //Logger.getLogger(InforNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public void getAllNetworkInterface(){
        InetAddress ia = null;
        try{
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            int cnt = 0;
            while(e.hasMoreElements()){
                NetworkInterface n = (NetworkInterface) e.nextElement();
                System.out.println(++cnt + " "+n.getName() + ": " + n.getDisplayName());
                System.out.println("MAC: " + getMacAddr(n));
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()){
                    ia = (InetAddress) ee.nextElement();
                    System.out.println(ia.getHostName() + " " + ia.getHostAddress());
                }
            }
        }catch(SocketException e){}
    }
    
    private String getMacAddr(NetworkInterface ni){
        String ret = NO_ADDRESS;
        try{
            ni.getHardwareAddress();
            byte[] mac = ni.getHardwareAddress();
            for(byte[] cmp:VIRTUAL_MAC){
                if(cmp[0] == mac[0] && cmp[1] == mac[1] && cmp[2] == mac[2])
                    return ret;
            }
            if(mac != null){
                String macadd = "";
                for(byte m:mac){
                    int a = (m < 0)?(256+m):(m); 
                    macadd += Integer.toHexString(a);
                    macadd += (m == mac[mac.length-1])? "":"-";
                }
                ret = macadd;
            }
        }catch(SocketException | NullPointerException e){
        }
        return ret;
    }
    
    private String getLocalAddr(NetworkInterface ni){
        String ret = NO_ADDRESS;
        try{
            Enumeration e = ni.getInetAddresses();
            while(e.hasMoreElements()){
                InetAddress ia = (InetAddress) e.nextElement();
                if(ia.isSiteLocalAddress() && ni.isUp())
                    return ia.getHostAddress();
            }
        }catch(SocketException e){}
        return ret;
    }
    
}
