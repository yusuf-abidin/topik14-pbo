/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author yusuf
 */
public class ImageSize {
   
    public static void resizeImage(JLabel jLabel){
        
        ImageIcon img = (ImageIcon) jLabel.getIcon();
        jLabel.setIcon(new ImageIcon(new ImageIcon(img.getImage()).getImage().getScaledInstance(jLabel.getWidth(), jLabel.getHeight(),  Image.SCALE_SMOOTH)));
    }

    
}
