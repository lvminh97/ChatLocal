/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import localappchat2.network.FromNetworkPacket;
import localappchat2.usernamecast.UserNameCast;

/**
 *
 * @author TV.Tinh
 */
public class NameList extends Panel{
    
    public static Rectangle scrollBound = new Rectangle(0,0,135,453); //153,453 to disable scroll
    public static Rectangle unameBound = new Rectangle(0,0,135,453);
    public static Dimension userNamePanelSize = new Dimension(115,51);
    
    ScrollPane scrollPanel = new ScrollPane();
    Panel unamePanel = new Panel();
    ArrayList<UserNamePanel> userNameList = new ArrayList<>();
    
    private UserNameCast unameService;
    
    public NameList(){
        unameService = new UserNameCast(this);
        unameService.start();
        init();
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                close();
            }
        });
        
    }
    
    private void init(){
        getScale();
        
        this.setLayout(null);
        
        unamePanel.setBounds(unameBound);
        unamePanel.setLayout(null);
        scrollPanel.add(unamePanel);
        
        scrollPanel.setBounds(scrollBound);
        scrollPanel.setBackground(Color.white);
        this.add(scrollPanel);
    }
    
    public void handle(String source){
        if(source.equals(UserNameCast.ID)){
            addToPanel();
        }
    }
            
    private void getScale(){
        float scaleW = ThemeManager.ScaleWidth;
        float scaleH = ThemeManager.ScaleHeight;
        
        scrollBound = new Rectangle((int)(scrollBound.x*scaleW)
                , (int)(scrollBound.y*scaleH)
                , (int)(scrollBound.width*scaleW)
                , (int)(scrollBound.height*scaleH));
        unameBound = new Rectangle((int)(unameBound.x*scaleW)
                , (int)(unameBound.y*scaleH)
                , (int)(unameBound.width*scaleW)
                , (int)(unameBound.height*scaleH));
        userNamePanelSize = new Dimension((int)(userNamePanelSize.width*scaleW)
                , (int)(userNamePanelSize.height*scaleH));
    }
    
    private void addToPanel(){
        unamePanel.removeAll();
        for(int i = 0; i < unameService.onlineList.size();i++){
            String ip = unameService.onlineList.get(i).getAddress();
            String name = new String(unameService.onlineList.get(i).getData());
            UserNamePanel user = new UserNamePanel(name, ip, true);
            user.setLocation(0, userNamePanelSize.height*i);
            user.setSize(userNamePanelSize);
            unamePanel.add(user);
        }
    }
 
    public void close(){
        unameService.close();
    }
    
}
