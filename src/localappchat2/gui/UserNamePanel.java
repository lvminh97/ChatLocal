/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author TV.Tinh
 */
public class UserNamePanel extends Panel{
    
    public static Rectangle bgPanelBound = new Rectangle(2,1,111,48);
    public static Rectangle nameBound = new Rectangle(0,0,111,20);
    public static Rectangle ipBound = new Rectangle(0,30,111,20);
    public static boolean isScaled = false;
    
    public static Color OfflColor = new Color(200,200,200);
    public static Color OnlColor = new Color(120,190,255);
    public static Color FocusColor = new Color(240,235,180);
    public static Color ClickColor = new Color(180,170,35);
    
    private Label name, ip;
    private Panel bgPanel;
    private boolean isOnline = false;
    
    public UserNamePanel(String name, String ip, boolean isOnline){
        this.isOnline = isOnline;
        getScale();
        init(name, ip, isOnline);
    }
    private void init(String sname, String sip, boolean isOnline){
        this.setLayout(null);
        this.setBackground(Color.gray);
        
        Color bg = (isOnline)?OnlColor:OfflColor;
        
        bgPanel = new Panel();
        bgPanel.setLayout(null);
        bgPanel.setBounds(bgPanelBound);
        bgPanel.setBackground(bg);
        this.add(bgPanel);
        
        name = new Label(sname);
        name.setFont(new Font("Courier New", 1, 13));
        name.setBounds(nameBound);
        bgPanel.add(name);
        
        ip = new Label(sip);
        ip.setFont(new Font("Courier New", 0, 10));
        ip.setForeground(Color.gray);
        ip.setAlignment(FlowLayout.RIGHT);
        ip.setBounds(ipBound);
        bgPanel.add(ip);
        
        addMouseLis(name);
        addMouseLis(ip);
        addMouseLis(bgPanel);
        
    }
    private void getScale(){
        if(!isScaled){
            isScaled = true;
            float scaleW = ThemeManager.ScaleWidth;
            float scaleH = ThemeManager.ScaleHeight;
            
            bgPanelBound = new Rectangle((int)(bgPanelBound.x*scaleW)
                    , (int)(bgPanelBound.y*scaleH)
                    , (int)(bgPanelBound.width*scaleW)
                    , (int)(bgPanelBound.height*scaleH));
            nameBound = new Rectangle((int)(nameBound.x*scaleW)
                    , (int)(nameBound.y*scaleH)
                    , (int)(nameBound.width*scaleW)
                    , (int)(nameBound.height*scaleH));
            ipBound = new Rectangle((int)(ipBound.x*scaleW)
                    ,(int)(ipBound.y*scaleH)
                    ,(int)(ipBound.width*scaleW)
                    ,(int)(ipBound.height*scaleH));
        }
    }
    
    public void addMouseLis(Component comp){
        
        comp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                mouseAction(e);
            }
            @Override
            public void mouseExited(MouseEvent e){
                mouseAction(e);
            };
            @Override
            public void mousePressed(MouseEvent e){
                mouseAction(e);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                mouseAction(e);
            }
        });
        
    }
    
    public void mouseAction(MouseEvent e){
        if(e.getID() == MouseEvent.MOUSE_ENTERED){
            bgPanel.setBackground(FocusColor);
            name.setBackground(FocusColor);
            ip.setBackground(FocusColor);
        }
        if(e.getID() == MouseEvent.MOUSE_EXITED){
            Color c = (isOnline)? OnlColor : OfflColor;
            bgPanel.setBackground(c);
            name.setBackground(c);
            ip.setBackground(c);
        }
        if(e.getID() == MouseEvent.MOUSE_PRESSED){
            bgPanel.setBackground(ClickColor);
            name.setBackground(ClickColor);
            ip.setBackground(ClickColor);
            
            //handle(this.sip);
        }
        if(e.getID() == MouseEvent.MOUSE_RELEASED){
            bgPanel.setBackground(FocusColor);
            name.setBackground(FocusColor);
            ip.setBackground(FocusColor);
        }
    }
    

}
