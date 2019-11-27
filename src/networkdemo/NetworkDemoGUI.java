/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkdemo;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author Windows 7
 */
public class NetworkDemoGUI extends Frame{
    
    public ChatPanel chatPanel;
    public SearchBar searchBar;
    public FriendsList friendsList;
    public ScrollPane scrollFriendList;
    public Panel panelFriend;
    
    public NetworkDemoGUI(){
        this.setBounds(200,100,500, 600);
        this.setLayout(null);
        this.setResizable(false);
        this.setBackground(new Color(215,215,175));
        
        searchBar = new SearchBar(this);
        searchBar.setBounds(10,30,135,50);
        searchBar.setComponnentBounds();
        
        friendsList = new FriendsList();
        friendsList.setHandle(this);
//        friendsList.setBounds(10,80,135,500);
        panelFriend = new Panel();
        panelFriend.setLayout(null);
        panelFriend.setBounds(10,80,117,500);
        panelFriend.add(friendsList);
        friendsList.setBounds(0,0,135,500);
        
        chatPanel = new ChatPanel();
        chatPanel.setBounds(150, 30, 340, 550);

        this.add(panelFriend);
        this.add(chatPanel);
        //this.add(friendsList);
        this.add(searchBar);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        
        this.show();
    }
    
    public void handle(String s, String type){
        if(type.equals("Search")){
            chatPanel.friendName.setText(s);
        }
        if(type.equals(FriendsList.ID)){
            chatPanel.friendName.setText(s);
        }
    }
    
    
}
