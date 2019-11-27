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
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Date;
import localappchat2.commons.Utils;

public class FriendsList extends ScrollPane{
    
    private NetworkDemoGUI parent;
    private Panel unamePanel;
    private ArrayList<UserNamePanel> userNameList;
    private final int heightEle = 50;
    public static final String ID = "FriendsList";
    
    public FriendsList(){
        userNameList = new ArrayList<>();
        TimeCounter timeCounter = new TimeCounter(5500);
        timeCounter.setHandle(this);
        timeCounter.start();
        init();
//        addNewEle("74-de-2b-58-35-5d", "192.168.0.103", true);
    }
    
    public void setHandle(NetworkDemoGUI parent){
        this.parent = parent;
    }
    
    private void init(){
        this.setBackground(Color.WHITE);
        unamePanel = new Panel();
        unamePanel.setLayout(null);
        
        unamePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setComponentBounds(e);
            }
        });
        
        addToPanel();
        this.add(unamePanel);
    }
    
    public void addToPanel(){
        //this.remove(unamePanel);
        unamePanel.removeAll();
        unamePanel.setSize(unamePanel.getWidth(), heightEle*userNameList.size());
        for(UserNamePanel uname : userNameList){
            unamePanel.add(uname);
        }
    }
    
    public void addNewEle(String name, String ip, boolean isOnline){
        UserNamePanel uname = new UserNamePanel(name, ip, isOnline, this);
        userNameList.add(uname);
        addToPanel();
    }
     
    public void setComponentBounds(ComponentEvent e){
        int w = this.getWidth();
        int h = this.getHeight();
        for(int i = 0; i< userNameList.size();i++){
           userNameList.get(i).setBounds(0,heightEle*i,this.getWidth()-20,heightEle);
        }
    }
    
    public void checkHanleMulcast(String name, String ip){
        boolean check = true;
        for(int i = 0; i< userNameList.size();i++){
               UserNamePanel uname = userNameList.get(i);
               boolean equal = uname.checkEqual(name, ip); // if equal setOnline
               if(equal){
                   check = false;
                   uname.setOnline(equal);
               }
               //System.out.println(userNameList.size()+ " "+ equal);
        }
        if(check){
            UserNamePanel uname = new UserNamePanel(name, ip, true, this);
            userNameList.add(uname);
            addToPanel();
        }
    }
    
    public void handle(String s, String type){
        
        if(type.equals(TimeCounter.ID)){
            //System.out.println("timecnt");
            Date date = new Date();
            long time = date.getTime() - 10000;
            for(UserNamePanel uname : userNameList){
                if(uname.isOnline() && (uname.getTimeOnCast() < time)){
                    uname.setOnline(false);
                }
            }
        }
        if(type.equals(UnameBroadCast.ID)){
            int e = s.indexOf(0);
            if(e>1){
                String ss = s.substring(0, e);
                String[] uname = ss.split(" ");  // can't use with "|" other some other char
                checkHanleMulcast(uname[1], uname[0]);
            }
        }
        if(type.equals(UserNamePanel.ID)){
            //System.out.println("Chosen: " + s);
            parent.handle(s, FriendsList.ID);
        }
    }
    
    private class TimeCounter extends Thread{
        
        private FriendsList parent;
        private int timeInterval = 8000;
        public static final String ID = "TimeCounter";
        
        public TimeCounter(int interval){
            this.timeInterval = interval;
        }
        
        public void setTimeInterval(int time){
            this.timeInterval = time;
        }
        
        public void setHandle(FriendsList parent){
         this.parent  = parent;
        }
        
        @Override
        public void run(){
            
            long time = System.currentTimeMillis();
//            boolean doit = false;
            while(true){
                long ctime = System.currentTimeMillis();
                if(ctime < time){   //overflow
                    time = ctime;
                }
                else if(ctime >= (time+timeInterval)){ // delay 8s
                    time = System.currentTimeMillis() + timeInterval;
//                    doit = true;
                    String s = "TimeCounter";
                    parent.handle(s, ID);
                }
                else{
                    Utils.sleep(1);
                }
//                if(doit){
//                    doit = false;
//                    String s = "TimeCounter";
//                    parent.handle(s, ID);
//                }
            }
        }
    }
    
}
