/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.gui;

import java.awt.Dimension;
import java.awt.List;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.util.Date;

/**
 *
 * @author TV.Tinh
 */
public class ChatContent extends Panel{
    
    public static Rectangle scrollBound = new Rectangle(0,0,340,475);
    public Dimension panelSize = new Dimension(340,460);
    public Dimension bubbleSize = new Dimension(156, 80);
    public static boolean isScaled = false;
    
    private ScrollPane scroll = new ScrollPane();
    private Panel panel = new Panel();
    List message = new List();
    
    public ChatContent(){
        getScale();
        init();
    }
    private void init(){
        this.setLayout(null);
        //this.setBackground(Color.red);
        
        scroll.setBounds(scrollBound);
        this.add(scroll);
        
        message.setSize(panelSize);
        message.setLocation(0, 0);
        scroll.add(panel);
        
        Date date = new Date();
        //System.out.println(date.getMonth());
        String msg = date.toString();
        addToPanel(msg);
        
    }
    private void getScale(){
        if(isScaled){
            float scaleW = ThemeManager.ScaleWidth;
            float scaleH = ThemeManager.ScaleHeight;
            
        }
        
    }
    public void addToPanel(String msg){
        message.add(msg);
    }
    
}
