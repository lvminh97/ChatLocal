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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchBar extends Panel{
    
    private NetworkDemoGUI mainWindow;
    private TextField inputTxt;
    private Button searchBtn;
    private Label resultLb;
    
    
    public SearchBar(NetworkDemoGUI parent){
        this.mainWindow = parent;
        
        this.setBackground(new Color(215,215,175));
        this.setLayout(null);
        
        inputTxt = new TextField();
        inputTxt.setText("192.168.");
        inputTxt.setFont(new Font("Courier New", 0, 14));
        
        searchBtn = new Button("Connect");
        searchBtn.setFont(new Font("Arial", 0, 8));
        
        resultLb = new Label("User or IP search");
        resultLb.setFont(new Font("Arial", 2, 10));
        resultLb.setForeground(Color.gray);
        
        this.add(inputTxt);
        this.add(searchBtn);
        this.add(resultLb);
        
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBtnAction(e);
            }
        });
        
    }
    public void setComponnentBounds(){
        int w = this.getWidth();
        int h = this.getHeight();
        if(w == 0 || h == 0){
            return;
        }
        inputTxt.setBounds(0,0, w-30, h-27);
        searchBtn.setBounds(w-30,0,30,h-27);
        resultLb.setBounds(0,h-25,w,20);
    }
    
    private void searchBtnAction(ActionEvent e){
        String s = inputTxt.getText();
        inputTxt.setText("");
        mainWindow.handle(s, "Search");
        System.out.println("Search for: " + s);
        
    }

}
