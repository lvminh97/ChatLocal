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
public class UnameBroadCast {
    
    private FriendsList friendList;
    private MulticastReceiverUser rxMulcast;
    private MulticastSenderUser   txMulcast;
    private String uName = "";
    public static final String ID = "UnameBroadCast";
    
    public UnameBroadCast(String uname){
        this.uName = uname;
        
        txMulcast = new MulticastSenderUser(StaticComponent.GROUP_MULCAST
                , StaticComponent.PORT_MULCAST, StaticComponent.TTL_MULCAST);
        txMulcast.setUNameMulcast(this.uName);
        
        rxMulcast = new MulticastReceiverUser(StaticComponent.GROUP_MULCAST,
                StaticComponent.PORT_MULCAST);
        rxMulcast.setHanle(this);
    }
    public void start(){
        if(txMulcast.isActive() && rxMulcast.isActive()){
            txMulcast.start();
            rxMulcast.start();
        }
    }
    
    public void setHandle(FriendsList fl){
        this.friendList = fl;
    }
    
    public void handle(String msg){
        friendList.handle(msg, ID);
    }
    
    public void close(){
        txMulcast.close();
        rxMulcast.close();
    }
    
}
