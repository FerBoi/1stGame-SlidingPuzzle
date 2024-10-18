/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JPanel;

/**
 *
 * @author Fernando GJ
 */
public class Puzzle extends JPanel {
    private BufferedImage img;
    
    private int[][] originalMatrix;
    private int[][] matrix;
    
    private final int TOTAL_ROW = 4;
    private final int TOTAL_COLUMN = 4;
    
    private int currentRow;
    private int currentColumn;
    
    private int width;
    private int height;

    public void createPuzzle(BufferedImage img) {
        this.img = img;
        this.width = img.getWidth();
        this.height = img.getHeight();
        
        this.divideImage();        
        this.convertImgToMatrix(img);
    }
    
    public BufferedImage createPuzzle(int currentRow, int currentColumn, int[][] matrix, int[][] originalMatrix) {
        this.currentRow = currentRow;
        this.currentColumn = currentColumn;
        this.matrix = matrix;
        this.originalMatrix = originalMatrix;
        
        this.width = this.matrix[0].length;
        this.height = this.matrix.length;
        
       this.img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
       BufferedImage originalImg = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        
        // transform a number to a img point color
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.img.setRGB(j, i, this.matrix[i][j]);
                originalImg.setRGB(j, i, this.originalMatrix[i][j]);
            }
        }        
        
        return originalImg;
    }
    
    private void divideImage() {
        while(this.width % this.TOTAL_COLUMN != 0 || this.height % this.TOTAL_ROW != 0) {
            if(this.width % this.TOTAL_COLUMN != 0)
                this.width--;
            
            if(this.height % this.TOTAL_ROW != 0)
                this.height--;
        }
        
        this.img = this.img.getSubimage(0, 0, this.width, this.height);
    }

    private void convertImgToMatrix(BufferedImage img) {
        int[] colorsImg = new int[width * height];
        this.matrix = new int[height][width];
        this.originalMatrix = new int[this.height][this.width];
        this.img.getRGB(0, 0, this.width, this.height, colorsImg, 0, width);

        final int WIDTH_CHUNK = this.height / this.TOTAL_ROW;
        final int HEIGHT_CHUNK = this.width / this.TOTAL_COLUMN;

        for (int i = 0; i < this.height; ++i) {
            for (int j = 0; j < this.width; ++j) {
                this.matrix[i][j] = colorsImg[this.width * i + j]; // Mapeo de índice del arreglo unidimensional al bidimensional.
                this.originalMatrix[i][j] = this.matrix[i][j];

                if (i < WIDTH_CHUNK && j < HEIGHT_CHUNK)
                    this.matrix[i][j] = -1; // white pixel
            }
        }
        
        this.doRandomMovements();
        this.convertMatrixToArray();
    }

    public BufferedImage getImg() {
        return img;
    }
    
    public int[][] getMatrix() {
        return matrix;
    }

    public int[][] getOriginalMatrix() {
        return originalMatrix;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }
    
    private void doRandomMovements() {
        Random r = new Random();

        MoveDirection oldDirection = null;
        MoveDirection currentDirection = null;

        int invalidNumber = -1;
        int i = 0;
        

        while (i < this.TOTAL_ROW * this.TOTAL_COLUMN) {
            int number = 0;

            do {
                number = r.nextInt(4);
            } while (invalidNumber != -1 && number == invalidNumber);

            switch (number) {
                case 0 ->
                    currentDirection = MoveDirection.UP;
                case 1 ->
                    currentDirection = MoveDirection.DOWN;
                case 2 ->
                    currentDirection = MoveDirection.LEFT;
                case 3 ->
                    currentDirection = MoveDirection.RIGHT;
            }

            if (oldDirection == null) {
                oldDirection = currentDirection;
                this.move(currentDirection);
                i++;
            } else if ((oldDirection == MoveDirection.UP && currentDirection != MoveDirection.DOWN) || (oldDirection == MoveDirection.DOWN
                    && currentDirection != MoveDirection.UP) || (oldDirection == MoveDirection.LEFT && currentDirection != MoveDirection.RIGHT)
                    || (oldDirection == MoveDirection.RIGHT && currentDirection != MoveDirection.LEFT)) {
                oldDirection = currentDirection;

                if (this.move(currentDirection))
                    i++;
                else
                    invalidNumber = number;
            }
        }
    }

    public boolean move(MoveDirection direction) {
        final int WIDTH_CHUNK = this.height / this.TOTAL_ROW;
        final int HEIGHT_CHUNK = this.width / this.TOTAL_COLUMN;

        if ((direction == MoveDirection.UP && this.currentRow - 1 < 0) || (direction == MoveDirection.DOWN && this.currentRow + 1 > this.TOTAL_ROW - 1)
                || (direction == MoveDirection.LEFT && this.currentColumn - 1 < 0) || (direction == MoveDirection.RIGHT && this.currentColumn + 1 > this.TOTAL_ROW - 1)) {
            return false;
        }

        final int ROW_WHITE_PIECE = this.currentRow * WIDTH_CHUNK;
        final int COLUMN_WHITE_PIECE = this.currentColumn * HEIGHT_CHUNK;

        int rowPieceMove = 0;
        int columnPieceMove = 0;

        switch (direction) {
            case UP ->
                this.currentRow--;
            case DOWN ->
                this.currentRow++;
            case LEFT ->
                this.currentColumn--;
            case RIGHT ->
                this.currentColumn++;
        }

        rowPieceMove = this.currentRow * WIDTH_CHUNK;
        columnPieceMove = this.currentColumn * HEIGHT_CHUNK;

        for (int i = 0; i < WIDTH_CHUNK; i++) {
            for (int j = 0; j < HEIGHT_CHUNK; j++) {
                int value = this.matrix[i + rowPieceMove][j + columnPieceMove];
                this.matrix[i + rowPieceMove][j + columnPieceMove] = this.matrix[i + ROW_WHITE_PIECE][j + COLUMN_WHITE_PIECE];
                this.matrix[i + ROW_WHITE_PIECE][j + COLUMN_WHITE_PIECE] = value;
            }
        }
        
        this.convertMatrixToArray();
        return true;
    }
    
    public void reset() {
        final int WIDTH_CHUNK = this.height / this.TOTAL_ROW;
        final int HEIGHT_CHUNK = this.width / this.TOTAL_COLUMN;
        
        for (int i = 0; i < this.originalMatrix.length; i++) {
            for (int j = 0; j < this.originalMatrix[i].length; j++) {
                if(i < WIDTH_CHUNK && j < HEIGHT_CHUNK)
                    this.matrix[i][j] = -1;
                else
                    this.matrix[i][j] = this.originalMatrix[i][j];
            }
        }
        
        this.doRandomMovements();
        this.convertMatrixToArray();
    }
    
    public boolean hasWon() {
        final int WIDTH_CHUNK = this.height / this.TOTAL_ROW;
        final int HEIGHT_CHUNK = this.width / this.TOTAL_COLUMN;
        
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[i].length; j++) {
                if(i < WIDTH_CHUNK && j < HEIGHT_CHUNK && this.matrix[i][j] != -1)
                    return false;
                else if(i >= WIDTH_CHUNK && j >= HEIGHT_CHUNK && this.matrix[i][j] != this.originalMatrix[i][j])
                    return false;
            }
        }

        return true;
    }
    
    private void convertMatrixToArray() {
        int[] array = new int[width * height];

        // Variable para seguir la posición en el array unidimensional
        int posicion = 0;
        
        // Copiar cada fila de la matriz al array
        for (int[] m : this.matrix) {
            System.arraycopy(m, 0, array, posicion, m.length);
            posicion += m.length; // Actualizamos la posición en el array
        }

        this.img.setRGB(0, 0, width, height, array, 0, width);
        this.repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
      
        g.drawImage(this.img, 0, 0, this.getWidth(), this.getHeight(), this);
    }
    
} // end Puzzle