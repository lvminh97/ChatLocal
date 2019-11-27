/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.gui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 *
 * @author TV.Tinh
 */
public class About extends Panel{
    
    public static int NameSize = 12;
    public static int CreditSize = 10;
    public static int ContactSize = 12;
    
    public static final String NAME = "Java Local Chat";
    public static final String[] CREDIT = {
        "HUST - SET", "ET4430 - 20181", "Group 09"
    };
    public static final String CONTACT = "fb.com/tv.tinh";
    
    private Label appName = new Label(NAME);
    private Label[] creditName = new Label[CREDIT.length];
    private Label contactName = new Label(CONTACT);
    
    public About(){
        init();
    }
    
    private void init(){
        this.setLayout(null);
        this.setBackground(ThemeManager.MainThemeColor);
        
        appName.setFont(new Font("Courier New", 1, 12));
        appName.setAlignment(FlowLayout.CENTER);
        appName.setForeground(ThemeManager.AboutTextColor);
        appName.setFocusable(true);
        this.add(appName);
        
        for(int i = 0; i< CREDIT.length;i++){
            creditName[i] = new Label(CREDIT[i]);
            creditName[i].setFont(new Font("Courier New", 0, 10));
            creditName[i].setAlignment(FlowLayout.CENTER);
            this.add(creditName[i]);
        }
        
        contactName.setAlignment(FlowLayout.CENTER);
        contactName.setFont(new Font("Courier New", 1, 11));
        this.add(contactName);
        
        addListener();
    }
    private void addListener(){
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setBoundElements();
            }
        });
    }
    private void setBoundElements(){
        int w = this.getWidth();
        int h = this.getHeight();
        int hSum = 0;
        appName.setBounds(0, hSum, 135, 15);
        hSum += 20;
        for(int i = 0; i< creditName.length;i++){
            creditName[i].setBounds(0,hSum, w, 11);
            hSum += 11;
        }
        hSum += 5;
        contactName.setBounds(0, hSum, w, 15);
    }
}

