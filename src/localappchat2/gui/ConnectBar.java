/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author TV.Tinh
 */
public class ConnectBar extends Panel{
    
    public static Rectangle inputBound = new Rectangle(0,0,105,23);
    public static Rectangle buttonBound = new Rectangle(105,0,30,23);
    public static Rectangle titleBound = new Rectangle(0,20,135,27);
    
    private TextField input = new TextField();
    private Button button = new Button();
    private Label title = new Label("Connect to IP");
    private ChatWindow parent;
    public static final String ID = "ConnectBar";
    
    public ConnectBar(ChatWindow p){
        this.parent = p;
        init();
    }
    
    private void init(){
        this.setLayout(null);
        getScale();
        input.setText("192.168.");
        input.setFont(new Font("Courier New", 0, 14));
        input.setBounds(inputBound);
        this.add(input);
        
        button = new Button("@");
        button.setFont(new Font("Courier New", 1, 10));
        button.setBounds(buttonBound);
        this.add(button);
        
        title.setFont(new Font("Courier New", 0, 10));
        title.setForeground(Color.GRAY);
        title.setBounds(titleBound);
        this.add(title);
        
        addListener();
    }
    private void getScale(){
        float scaleW = ThemeManager.ScaleWidth;
        float scaleH = ThemeManager.ScaleHeight;
        
        inputBound = new Rectangle((int)(inputBound.x*scaleW)
                ,(int)(inputBound.y*scaleH)
                ,(int)(inputBound.width*scaleW)
                ,(int)(inputBound.height*scaleH));
        buttonBound = new Rectangle((int)(buttonBound.x*scaleW)
                , (int)(buttonBound.y*scaleH)
                , (int)(buttonBound.width*scaleW)
                , (int)(buttonBound.height*scaleW));
        titleBound = new Rectangle((int)(titleBound.x*scaleW)
                , (int)(titleBound.y*scaleH)
                , (int)(titleBound.width*scaleW)
                , (int)(titleBound.height*scaleH));
    }
    
    private void addListener(){
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handle(input.getText());
                input.setText("192.168.");
            }
        });
    }
    
    public void handle(String s){
        parent.handle(s, ID);
    }
    
}

