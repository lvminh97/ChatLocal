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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class UserNamePanel extends Panel{
    
    
    private final static Color OFFLCOLOR = new Color(200,200,200);
    private final static Color ONLCOLOR = new Color(120,190,255);
    private final static Color FOCUSCOLOR = new Color(240,235,180);
    private final static Color CLICKCOLOR = new Color(180,170,35);
    
    private FriendsList parent;
    private Label name, ip;
    private Panel bgpanel;
    private String sname = "Noname User";
    private String sip   = "0.0.0.0";
    private boolean online = false;
    private Date timeOnCast = null;
    private int width = 0, height = 0;
    public static final String ID = "UserNamePanel";
    

    public UserNamePanel(String name, String ip, boolean isOnline){
        this.sname = name;
        this.sip = ip;
        this.online = isOnline;
        init();
        setOnline(online);
    }
    
     public UserNamePanel(String name, String ip, boolean isOnline, FriendsList parent){
        this.parent = parent;
        this.sname = name;
        this.sip = ip;
        this.online = isOnline;
        init();
        setOnline(online);
    }
    
    public void setHandle(FriendsList parent){
        this.parent = parent;
    }
    
    public void handle(String s){
        parent.handle(s, ID);
    }
    
    private void init(){
        
        bgpanel = new Panel();
        bgpanel.setLayout(null);
        bgpanel.setBackground((online)? ONLCOLOR : OFFLCOLOR);
        
        name = new Label(sname);
        name.setFont(new Font("Courier New", 1, 13));
        
        ip = new Label(sip);
        ip.setFont(new Font("Courier New", 1, 10));
        ip.setForeground(Color.gray);
        ip.setAlignment(FlowLayout.RIGHT);
        
        this.setBackground(Color.gray);
        this.setLayout(null);
        this.add(bgpanel);
        bgpanel.add(name);
        bgpanel.add(ip);
        
        addMouseLis(bgpanel);
        addMouseLis(name);
        addMouseLis(ip);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setComponentBounds();
            }
        });
        
    }
    
    public void setOnline(boolean bool){
        this.online = bool;
        this.timeOnCast = new Date();
        Color c = (online)? ONLCOLOR : OFFLCOLOR;
        bgpanel.setBackground(c);
        name.setBackground(c);
        ip.setBackground(c);
    }
    
    public boolean isOnline(){
        return online;
    }
    
    public long getTimeOnCast(){
        return timeOnCast.getTime();
    }
    
    public void addMouseLis(Component comp){
        
        comp.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e){
//                mouseAction(e);
//            }
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
            bgpanel.setBackground(FOCUSCOLOR);
            name.setBackground(FOCUSCOLOR);
            ip.setBackground(FOCUSCOLOR);
        }
        if(e.getID() == MouseEvent.MOUSE_EXITED){
            Color c = (online)? ONLCOLOR : OFFLCOLOR;
            bgpanel.setBackground(c);
            name.setBackground(c);
            ip.setBackground(c);
        }
        if(e.getID() == MouseEvent.MOUSE_PRESSED){
            bgpanel.setBackground(CLICKCOLOR);
            name.setBackground(CLICKCOLOR);
            ip.setBackground(CLICKCOLOR);
            
            handle(this.sip);
        }
        if(e.getID() == MouseEvent.MOUSE_RELEASED){
            bgpanel.setBackground(FOCUSCOLOR);
            name.setBackground(FOCUSCOLOR);
            ip.setBackground(FOCUSCOLOR);
        }
    }
    
    public void setComponentBounds(){
        width = this.getWidth();
        height = this.getHeight();
        
        bgpanel.setBounds(2,1,width-4,height-2);
        name.setBounds(0,0,width-2,height-20);
        ip.setBounds(0,height-20,width-2,20);
        
    }
    
    public boolean checkEqual(String name, String ip){
        //System.out.println(name +" vs "+ this.name.getText());
        if(this.ip.getText().equals(ip)){   // can't check the name has existed
            return true;
        }
        return false;
    }
    
    
}
