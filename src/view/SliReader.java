/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fernando GJ
 */
public class SliReader {
    
    public static Object[] readFile(File file) {
        Object[] allData = new Object[6];
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            for (int i = 0; i < 4; i++)
                allData[i] = ois.readInt();

            try {
                for (int i = 0; i < 2; i++)
                    allData[i + 4] = (int[][]) ois.readObject();                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SliReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch (IOException ex) {
            return null;
        }
        
        return allData;
    }
    

} // end SliReader