/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkdemo;

/**
 *
 * @author John McTavish
 */
import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author John McTavish
 */
public class LoginWindow extends Frame{
    private Label label1;
    private TextField usernameTxt;
    private Label label2;
    private TextField passwordTxt;
    private Button loginBtn;
    private int res;
    public LoginWindow(){
        this.setLayout(null);
        this.setSize(320, 200);
        this.setResizable(false);
        this.setBounds(100, 100, this.getWidth(), this.getHeight());
        this.setTitle("Login Window");
        // Username
        label1 = new Label();
        label1.setText("Username");
        label1.setBounds(40, 50, 60, 25);
        
        usernameTxt = new TextField(30);
        usernameTxt.setBounds(120, 50, 150, 25);
        // Password
        label2 = new Label();
        label2.setText("Password");
        label2.setBounds(40, 100, 60, 25);
        
        passwordTxt = new TextField(30);
        passwordTxt.setBounds(120, 100, 150, 25);
        passwordTxt.setEchoChar('•');
        //
        loginBtn = new Button();
        loginBtn.setBounds(120, 140, 100, 30);
        loginBtn.setLabel("Login");
        //
        this.add(label1);
        this.add(usernameTxt);
        this.add(label2);
        this.add(passwordTxt);
        this.add(loginBtn);
        this.show();
        //
        loginBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                loginBtnAction(e);
            }
        
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }
    public void loginBtnAction(ActionEvent e){
        DBConnect db = new DBConnect();
        db.connect("D://DB/localchat.db");
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();
        try {
            ResultSet r = db.select("SELECT COUNT(*) FROM user WHERE username='" + username + "' AND password='" + password + "'");
            while(r.next()){
                if(r.getInt("count(*)") == 1){
                    System.out.println("Login successful!!!");
                    res = 1;
                }
                else{
                    System.out.println("Wrong username or password!!!");
                    res = 0;
                }
            }
        } catch (SQLException ex) {
            //Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public synchronized int getRes(){
        return res;
    }
}
