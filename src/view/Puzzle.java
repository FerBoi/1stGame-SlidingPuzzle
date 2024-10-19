/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JPanel;

/**
 * This class is responsible for initializing the puzzle, managing the state of
 * the pieces, and handling user interactions such as moving pieces and checking
 * if the puzzle has been solved.
 *
 * @author Fernando GJ
 */
public class Puzzle extends JPanel {
    /**
     * Puzzle img (-1 included) 
     */
    private BufferedImage img;
    /**
     * Original img matrix (img provided by the user)
     */
    private static int[][] imgProvidedMatrix;
    /**
     * It represents the variable 'img' as a matrix
     */
    private int[][] matrix;
    /**
     * Total number of rows the puzzle must have
     */
    private final static int TOTAL_ROW = 4;
    /**
     * Total number of column the puzzle must have
     */
    private final static int TOTAL_COLUMN = 4;
    /**
     * Variable that represents the position of the white piece (row, column)
     */
    private final int[] CURRENT_POSITION_WHITE_PIECE = new int[2];
    /**
     * Image dimensions
     */
    private static final Dimension IMG_DIMENSIONS = new Dimension();
    /**
     * Each image chunk has the same dimensions (all chunks are equal, so these dimensions are consistent 
     * throughout the entire puzzle)
     */
    private static final Dimension CHUNK_DIMENSIONS = new Dimension();
    
    /**
     * The first method to create the puzzle (using the image provided by the user). Additionally, the image needs to 
     * be processed to divide it into 4 rows and 4 columns of equal dimensions and, finally, painted.
     * @param img - Image provided by the user
     * @see Puzzle#divideImage()
     * @see Puzzle#convertImgToMatrix() 
     * @see Puzzle#doRandomMovements() 
     * @see Puzzle#convertMatrixToArray() 
     */
    public void createPuzzle(BufferedImage img) {
        this.img = img;
        IMG_DIMENSIONS.width = img.getWidth();
        IMG_DIMENSIONS.height = img.getHeight();
        CHUNK_DIMENSIONS.width = IMG_DIMENSIONS.height / TOTAL_COLUMN;
        CHUNK_DIMENSIONS.height = IMG_DIMENSIONS.width / TOTAL_ROW;
        
        /*
        When the user changes the puzzle, this method is called. Therefore, we need to ensure that the current 
        position is set to the beginning (row 0, column 0)
        */
        for (int i = 0; i < this.CURRENT_POSITION_WHITE_PIECE.length; i++)
            this.CURRENT_POSITION_WHITE_PIECE[i] = 0;
        
        this.divideImage();        
        this.convertImgToMatrix();
        this.doRandomMovements();
        this.convertMatrixToArray();
    }
    
    /**
     * The second method creates the puzzle using a matrix, and through it, we obtain the BufferedImage
     * @param currentRow - where is the white piece located in terms of rows?
     * @param currentColumn - where is the white piece located in terms of columns?
     * @param matrix - player matrix
     * @param originalMatrix - image matrix
     * @return Original bufferedImage to be displayed in the preview image label
     */
    public BufferedImage createPuzzle(int currentRow, int currentColumn, int[][] matrix, int[][] originalMatrix) {
        this.CURRENT_POSITION_WHITE_PIECE[0] = currentRow;
        this.CURRENT_POSITION_WHITE_PIECE[1] = currentColumn;
        this.matrix = matrix;
        imgProvidedMatrix = originalMatrix;
        
        IMG_DIMENSIONS.width = this.matrix[0].length;
        IMG_DIMENSIONS.height = this.matrix.length;
        
       this.img = new BufferedImage(IMG_DIMENSIONS.width, IMG_DIMENSIONS.height, BufferedImage.TYPE_INT_ARGB);
       BufferedImage originalImg = new BufferedImage(IMG_DIMENSIONS.width, IMG_DIMENSIONS.height, BufferedImage.TYPE_INT_ARGB);
        
        for (int i = 0; i < IMG_DIMENSIONS.height; i++) {
            for (int j = 0; j < IMG_DIMENSIONS.width; j++) {
                this.img.setRGB(j, i, this.matrix[i][j]);
                originalImg.setRGB(j, i, imgProvidedMatrix[i][j]);
            }
        }        
        
        return originalImg;
    }
    
    /**
     * Reduces the dimensions of the img provided by the user in case of either the width or the height aren`t divisible
     * by the 'TOTAL_ROW' or 'TOTAL_COLUMN' variables.
     * Finally, we obtain a new image with the new dimensions
     */
    private void divideImage() {
        while(IMG_DIMENSIONS.width % TOTAL_COLUMN != 0 || IMG_DIMENSIONS.height % TOTAL_ROW != 0) {
            if(IMG_DIMENSIONS.width % TOTAL_COLUMN != 0)
                IMG_DIMENSIONS.width--;
            
            if(IMG_DIMENSIONS.height % TOTAL_ROW != 0)
                IMG_DIMENSIONS.height--;
        }
        
        this.img = this.img.getSubimage(0, 0, IMG_DIMENSIONS.width, IMG_DIMENSIONS.height);
    }

    /**
     * Method to convert from a BufferedImage to a matrix.  
     * @param img - Image provided by the user
     */
    private void convertImgToMatrix() {
        int[] colorsImg = new int[IMG_DIMENSIONS.width * IMG_DIMENSIONS.height];
        this.matrix = new int[IMG_DIMENSIONS.height][IMG_DIMENSIONS.width];
        imgProvidedMatrix = new int[IMG_DIMENSIONS.height][IMG_DIMENSIONS.width];
        this.img.getRGB(0, 0, IMG_DIMENSIONS.width, IMG_DIMENSIONS.height, colorsImg, 0, IMG_DIMENSIONS.width);
        
        for (int i = 0; i < IMG_DIMENSIONS.height; ++i) {
            for (int j = 0; j < IMG_DIMENSIONS.width; ++j) {
                this.matrix[i][j] = colorsImg[IMG_DIMENSIONS.width * i + j]; 
                imgProvidedMatrix[i][j] = this.matrix[i][j];

                if (i < CHUNK_DIMENSIONS.width && j < CHUNK_DIMENSIONS.height)
                    this.matrix[i][j] = -1;
            }
        }
    }

    /**
     * Returns the current image (BufferedImage) used in the puzzle.
     * @return the BufferedImage object representing the current image
     */
    public BufferedImage getImg() {
        return img;
    }
    
    /**
     * Returns the current matrix representing the puzzle's state.
     * @return a 2D array of integers representing the puzzle's current state
     */
    public int[][] getMatrix() {
        return matrix;
    }

    /**
     * Returns the original matrix corresponding to the image provided by the
     * user.
     * @return a 2D array of integers representing the original state of the
     * puzzle (without white piece)
     */
    public int[][] getOriginalMatrix() {
        return imgProvidedMatrix;
    }

    /**
     * Returns the current position of the white piece in the puzzle.
     * @return an array of two integers representing the row and column of the
     * white piece in the puzzle's current state
     */
    public int[] getCURRENT_POSITION_WHITE_PIECE() {
        return CURRENT_POSITION_WHITE_PIECE;
    }
    
    /**
     * Performs random movements on the puzzle to shuffle its pieces. The
     * number of random movements is determined by the product of TOTAL_ROW and
     * TOTAL_COLUMN
     * @see Puzzle#move(view.MoveDirection) 
     */
    private void doRandomMovements() {
        Random r = new Random();

        MoveDirection oldDirection = null;
        MoveDirection currentDirection = null;

        int invalidNumber = -1;
        int i = 0;
        final int RANDOM_MOVEMENTS = TOTAL_ROW * TOTAL_COLUMN;
        

        while (i < RANDOM_MOVEMENTS) {
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

    /**
     * Moves the white piece in the specified direction if the move is valid.
     * Updates the position of the white piece and swaps it with the adjacent
     * piece.
     * @param direction - The direction in which to move the white piece
     * @return true if the move was successful, false otherwise
     */
    public boolean move(MoveDirection direction) {
        if ((direction == MoveDirection.UP && CURRENT_POSITION_WHITE_PIECE[0] - 1 < 0) || (direction == MoveDirection.DOWN && CURRENT_POSITION_WHITE_PIECE[0] + 1 > TOTAL_ROW - 1)
                || (direction == MoveDirection.LEFT && CURRENT_POSITION_WHITE_PIECE[1] - 1 < 0) || (direction == MoveDirection.RIGHT && CURRENT_POSITION_WHITE_PIECE[1] + 1 > TOTAL_ROW - 1)) {
            return false;
        }

        final int ROW_WHITE_PIECE = CURRENT_POSITION_WHITE_PIECE[0] * CHUNK_DIMENSIONS.width;
        final int COLUMN_WHITE_PIECE = CURRENT_POSITION_WHITE_PIECE[1] * CHUNK_DIMENSIONS.height;

        int rowPieceMove, columnPieceMove;

        switch (direction) {
            case UP ->
                CURRENT_POSITION_WHITE_PIECE[0]--;
            case DOWN ->
                CURRENT_POSITION_WHITE_PIECE[0]++;
            case LEFT ->
                CURRENT_POSITION_WHITE_PIECE[1]--;
            case RIGHT ->
                CURRENT_POSITION_WHITE_PIECE[1]++;
        }

        rowPieceMove = CURRENT_POSITION_WHITE_PIECE[0] * CHUNK_DIMENSIONS.width;
        columnPieceMove = CURRENT_POSITION_WHITE_PIECE[1] * CHUNK_DIMENSIONS.height;

        for (int i = 0; i < CHUNK_DIMENSIONS.width; i++) {
            for (int j = 0; j < CHUNK_DIMENSIONS.height; j++) {
                int value = this.matrix[i + rowPieceMove][j + columnPieceMove];
                this.matrix[i + rowPieceMove][j + columnPieceMove] = this.matrix[i + ROW_WHITE_PIECE][j + COLUMN_WHITE_PIECE];
                this.matrix[i + ROW_WHITE_PIECE][j + COLUMN_WHITE_PIECE] = value;
            }
        }
        
        return true;
    }
    
    /**
     * Resets the puzzle to its initial state by restoring the original matrix
     * and then performing random movements to shuffle the pieces.
     * @see Puzzle#doRandomMovements() 
     * @see Puzzle#convertMatrixToArray() 
     */
    public void reset() {
        for (int i = 0; i < imgProvidedMatrix.length; i++) {
            for (int j = 0; j < imgProvidedMatrix[i].length; j++) {
                if(i < CHUNK_DIMENSIONS.width && j < CHUNK_DIMENSIONS.height)
                    this.matrix[i][j] = -1;
                else
                    this.matrix[i][j] = imgProvidedMatrix[i][j];
            }
        }
        
        this.doRandomMovements();
        this.convertMatrixToArray();
    }
    
    /**
     * Checks if the puzzle has been solved by comparing the current matrix with
     * the original matrix.
     * @return true if the puzzle is solved, false otherwise
     */
    public boolean hasWon() {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[i].length; j++) {
                // Check if the positions of the white piece are correct
                if(i < CHUNK_DIMENSIONS.width && j < CHUNK_DIMENSIONS.height && this.matrix[i][j] != -1)
                    return false;
                // Check if the positions of the other pieces match the original
                else if(i >= CHUNK_DIMENSIONS.width && j >= CHUNK_DIMENSIONS.height && this.matrix[i][j] != imgProvidedMatrix[i][j])
                    return false;
            }
        }

        return true;
    }
    
    /**
     * Converts the current matrix representation of the puzzle into a
     * one-dimensional array and updates the BufferedImage to reflect the
     * current state of the puzzle.
     */
    private void convertMatrixToArray() {
        int[] array = new int[IMG_DIMENSIONS.width * IMG_DIMENSIONS.height];

        // Variable para seguir la posición en el array unidimensional
        int posicion = 0;
        
        // Copiar cada fila de la matriz al array
        for (int[] m : this.matrix) {
            System.arraycopy(m, 0, array, posicion, m.length);
            posicion += m.length; // Actualizamos la posición en el array
        }

        this.img.setRGB(0, 0, IMG_DIMENSIONS.width, IMG_DIMENSIONS.height, array, 0, IMG_DIMENSIONS.width);
        this.repaint();
    }
    
    /**
     * Paints the panel by drawing the current BufferedImage
     * @param g the Graphics context used for painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);        
      
        g.drawImage(this.img, 0, 0, this.getWidth(), this.getHeight(), this);
    }
    
} // end Puzzle