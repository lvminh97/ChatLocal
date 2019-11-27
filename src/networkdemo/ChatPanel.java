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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import localappchat2.AudioVideoCall;

public class ChatPanel extends Panel 
        
        implements ActionListener{
            
    public TextArea sendMesage;
    public java.awt.ScrollPane scrolSendMsg;
    public java.awt.List mesage;
    public java.awt.Button sendBtn;
    public java.awt.Label friendName;
    public Panel sendPanel;
    public java.awt.Button btnAVCall;
    public java.awt.Button btnVoiceCall;
    
    public final static String address = "192.168.0.102";
    public final static int port = 2018;
            
    public ChatPanel(){
       
        this.setBackground(Color.white);
        this.setLayout(null);
        
        friendName = new Label("Name");
        friendName.setFont(new Font("Arial",1,15));
        friendName.setAlignment(FlowLayout.CENTER);
        
        btnAVCall = new Button("Video Call");
        btnAVCall.setFont(new Font("Courier New", 1, 9));
        btnAVCall.setBackground(Color.white);
        
        btnVoiceCall = new Button("Voice Call");
        btnVoiceCall.setFont(new Font("Courier New", 1, 9));
        btnVoiceCall.setBackground(Color.white);
        
        sendMesage = new TextArea();
        sendMesage.setFont(new Font("Arial", 0, 12));
        sendPanel = new Panel();
        sendPanel.setLayout(null);
        sendPanel.add(sendMesage);
        
        mesage = new List();
        
        sendBtn = new Button("Send");
        sendBtn.setBackground(new Color(215,215,175));
        sendBtn.setFont(new Font("Courier New", 1, 10));
        
        this.add(friendName);
        this.add(btnAVCall);
        this.add(btnVoiceCall);
        //this.add(sendMesage);
        this.add(sendPanel);
        this.add(mesage);
        this.add(sendBtn);
        sendBtn.addActionListener(this);
        btnAVCall.addActionListener(this);
        btnVoiceCall.addActionListener(this);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setComponentBounds();
            }

        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == sendBtn){
            String s = sendMesage.getText();
            sendMesage.setText("");
            if(!s.equals("")){
                //System.out.println(s);
                mesage.add("You: "+s);
                
                ClientDemo client = new ClientDemo(this,friendName.getText(),port);
                client.send(s);
                client.start();
                
                mesage.repaint();
            }
        }

        if(e.getSource() == btnAVCall){
            try {
                InetAddress ia = InetAddress.getByName(friendName.getText());
                AudioVideoCall avc = new AudioVideoCall(ia, AudioVideoCall.TYP_AUDIO_VIDEO_CALL);
                avc.start();
            } catch (UnknownHostException ex) {
            }
        }
        if(e.getSource() == btnVoiceCall){
            try {
                InetAddress ia = InetAddress.getByName(friendName.getText());
                AudioVideoCall avc = new AudioVideoCall(ia, AudioVideoCall.TYP_VOICE_CALL);
                avc.start();
            } catch (UnknownHostException ex) {
            }
        }
    }
    
    public void setComponentBounds(){
        friendName.setBounds(0,0,this.getWidth()-100, 50);
        btnAVCall.setBounds(this.getWidth()-100,0,50, 50);
        btnVoiceCall.setBounds(this.getWidth()-50,0,50, 50);
        
        mesage.setBounds(0,50,this.getWidth(), this.getHeight()-60-50);
        sendPanel.setBounds(0,this.getHeight()-60,this.getWidth()-60,60);
        sendMesage.setBounds(0,0,this.getWidth()-46,75);
        sendBtn.setBounds(this.getWidth()-60, this.getHeight()-60,60,60);
    }
}
