/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author TV.Tinh
 */
public class ThemeManager {
    public static final Dimension SCREEN_SIZE_REF = new Dimension(1366, 768);
    public static float ScaleWidth = 1.0f;
    public static float ScaleHeight = 1.0f;
    public static Dimension SCREEN_SIZE = null;
    
    public static Color MainThemeColor = new Color(215, 215, 175);
    public static Color AboutTextColor = Color.BLACK;
    
    public ThemeManager(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_SIZE = screenSize;
        ScaleWidth = ((float)SCREEN_SIZE.width/SCREEN_SIZE_REF.width);
        ScaleHeight =  ((float)SCREEN_SIZE.height/SCREEN_SIZE_REF.height);
    }
    static public float getScaleWidth(){
        if(SCREEN_SIZE == null){
            ThemeManager tm = new ThemeManager();
        }
        return ScaleWidth;
    }
    static public float getScaleHeight(){
        if(SCREEN_SIZE == null){
            ThemeManager tm = new ThemeManager();
        }
        return ScaleHeight;
    }

}
