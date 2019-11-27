/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.gui;

import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author TV.Tinh
 */
public class ChatWindow extends Frame{
    
    public static Rectangle chatWdBound = new Rectangle(100,84,500, 600);
    public static Rectangle aboutBound = new Rectangle(10, 520, 135, 70);
    public static Rectangle connectBarBound = new Rectangle(10,30,135,50);
    public static Rectangle nameListBound = new Rectangle(10,80,135,435);
    public static Rectangle chatContentBound = new Rectangle(150, 80, 340, 460);
    
    private About about;
    private ConnectBar connectBar;
    private NameList nameList;
    private ChatContent chatContent;
    
    public ChatWindow(){
        ThemeManager theme = new ThemeManager();
        getScale();
        init();
    }
    
    private void init(){
        this.setLayout(null);
        this.setResizable(false);
        this.setBounds(chatWdBound);
        
        about = new About();
        about.setBounds(aboutBound);
        this.add(about);
        
        connectBar = new ConnectBar(this);
        connectBar.setBounds(connectBarBound);
        this.add(connectBar);
        
        nameList = new NameList();
        nameList.setBounds(nameListBound);
        this.add(nameList);
        
        chatContent = new ChatContent();
        chatContent.setBounds(chatContentBound);
        this.add(chatContent);
        
        //this.add(new StringDraw());
        
        this.addListener();
        this.show();
    }
    
    private void getScale(){
        float scaleW = ThemeManager.ScaleWidth;
        float scaleH = ThemeManager.ScaleHeight;
        
        chatWdBound = new Rectangle((int)(chatWdBound.x*scaleW)
                ,(int)(chatWdBound.y*scaleH)
                ,(int)(chatWdBound.width*scaleW)
                ,(int)(chatWdBound.height*scaleH));
        aboutBound = new Rectangle((int)ThemeManager.ScaleWidth*aboutBound.x
                ,(int)(ThemeManager.ScaleHeight*aboutBound.y)
                ,(int)(ThemeManager.ScaleWidth*aboutBound.width)
                ,(int)(ThemeManager.ScaleHeight*aboutBound.height));
        connectBarBound = new Rectangle((int)(connectBarBound.x*scaleW)
                , (int)(connectBarBound.y*scaleH)
                , (int)(connectBarBound.width*scaleW)
                , (int)(connectBarBound.height*scaleH));
        chatContentBound = new Rectangle((int)(chatContentBound.x*scaleW)
                , (int)(chatContentBound.y*scaleH)
                , (int)(chatContentBound.width*scaleW)
                , (int)(chatContentBound.height*scaleH));
        
    }
    
    private void addListener(){
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }
    
    public void handle(String s, String type){
        if(type.equals(ConnectBar.ID)){
            System.out.println(s);
        }
    }
}
