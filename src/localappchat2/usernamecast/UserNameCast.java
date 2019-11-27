/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.usernamecast;

import java.util.ArrayList;
import localappchat2.commons.Utils;
import localappchat2.gui.NameList;
import localappchat2.network.FromNetworkPacket;
import localappchat2.network.InfoNet;
import localappchat2.network.MulticastReceiver;
import localappchat2.network.MulticastSender;

/**
 *
 * @author TV.Tinh
 */
public class UserNameCast extends Thread{
    
    NameList parent;
    MulticastReceiver mcReceiver;
    MulticastSender mcSender;
    public ArrayList<FromNetworkPacket> onlineList = new ArrayList<>(); 
    ArrayList<FromNetworkPacket> recevedList = new ArrayList<>(); 
    int delay = 3500; //ms
    int ttlMulticast = 2;
    boolean isOpen;
    public static String ID = "UserNameCast";
    
    public UserNameCast(NameList p){
        this.parent = p;
        mcReceiver = new MulticastReceiver(InfoNet.getMulticastInet(), InfoNet.PORT_MTCAST);
        mcSender = new MulticastSender(InfoNet.getMulticastInet(), InfoNet.PORT_MTCAST, ttlMulticast);
        isOpen = mcReceiver.isOpen&mcSender.isOpen;
    }
    
    public UserNameCast(){
        mcReceiver = new MulticastReceiver(InfoNet.getMulticastInet(), InfoNet.PORT_MTCAST);
        mcSender = new MulticastSender(InfoNet.getMulticastInet(), InfoNet.PORT_MTCAST, ttlMulticast);
        isOpen = mcReceiver.isOpen&mcSender.isOpen;
    }
    
    @Override
    public void run(){
        if(isOpen){
            mcReceiver.start();
            mcReceiver.setQueueData(recevedList);
            mcSender.start();
        }
        else{
            System.out.println("Error to create (UserNameCast)");
        }
        long time = System.currentTimeMillis()+delay;
        while(isOpen){
            if(System.currentTimeMillis() > time){
                time = System.currentTimeMillis()+delay;
                mcSender.send(InfoNet.MAC_ADDRESS.getBytes());
                updateOnlineList();
                if(parent!= null) handle();
//                if(!onlineList.isEmpty()) System.out.println(onlineList.get(0).getAddress());
            }else{
                Utils.sleep(1);
            }
        }
    }
    
    private void handle(){
        parent.handle(ID);
    }
    
    private void updateOnlineList(){
        onlineList.clear();
        for(int i=recevedList.size()-1;i>=0;i--){
            onlineList.add(recevedList.get(i));
        }
        recevedList.clear();
    }
    
    public void close(){
        if(isOpen){
            isOpen = false;
            this.stop();
            mcReceiver.close();
            mcSender.close();
        }
        System.out.println("Closed (UserNameCast)");
    }
}
