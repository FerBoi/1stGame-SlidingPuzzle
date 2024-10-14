/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package main;

import com.formdev.flatlaf.FlatLightLaf;
import java.util.logging.Level;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import view.MainWindow;

/**
 *
 * @author Fernando GJ
 */
public class Init {

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
        
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        window.setLocationRelativeTo(null);
        window.setVisible(true);

    } // end main

} // end Init
