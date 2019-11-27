/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.commons;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Date;
import localappchat2.LocalAppChat2;

/**
 *
 * @author TV.Tinh
 */
public class Utils {
    
    static public void sleep(long time){
        try {
            Thread.sleep(2);
        } catch (InterruptedException ex) {
        }
    }
    
    static public String getCurDir(){
        String path = "";
        try{
            File fileJar = new File(LocalAppChat2.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String curPathOfJar = fileJar.getPath();
            String nameJar = fileJar.getName();
            path = curPathOfJar.substring(0, curPathOfJar.length()-nameJar.length());
            System.out.println("Current Dir: "+path);
        }catch(URISyntaxException e){
            System.out.println("Cant read the path libs");
        }
        return path;
    }
    public static String getTime(){
        Date d = new Date();
        String time = Integer.toString(d.getDate())
                +Integer.toString(d.getMonth()+1)
                +Integer.toString(d.getYear())
                +Integer.toString(d.getHours())
                +Integer.toString(d.getMinutes())
                +Integer.toString(d.getSeconds());
        return time;
    }

}
