/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 * @author Fernando GJ
 */
public class SliWriter {
    
    public static boolean writeSliFile(int[] otherData, int[][] matrix, int[][] matrixSolved, File fileToWrite) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileToWrite))) {
            for (int i = 0; i < otherData.length; i++)
                oos.writeInt(otherData[i]);
            
            oos.writeObject(matrix);
            oos.writeObject(matrixSolved);
            
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

} // end SliWriter
