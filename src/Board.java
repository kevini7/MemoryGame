import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class Board {

    // 2D Array to hold cells
    private Cell cells[][];

    // Resource loader
    private ClassLoader loader = getClass().getClassLoader();

    // Constructor
    public Board(int boardSize, ActionListener AL, JPanel view, int cellSize, int numMines) {

        // Set up the game board;
        cells = new Cell[boardSize][boardSize];
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                cells[i][j] = new Cell();
                cells[i][j].addActionListener(AL);
                //cells[i][j].setBackground(Color.DARK_GRAY);
                cells[i][j].setPreferredSize(new Dimension(cellSize, cellSize));
                cells[i][j].setRow(i);
                cells[i][j].setCol(j);
                view.add(cells[i][j]);
            }
        }

        // add mines to the board
        addMines(cells, numMines);

    }

    // method to randomly add mines to the board
    public void addMines(Cell c[][], int numMines) {
        int randIndex, randIndex2;
        int minesPlaced = 0;
        while(minesPlaced != numMines) {
            randIndex = (int)(Math.random()*c.length);
            randIndex2 = (int)(Math.random()*c.length);
            if(c[randIndex][randIndex2].hasMine() == false) {
                c[randIndex][randIndex2].setHasMine(true);
                minesPlaced += 1;
            }
        }
    }


    public Cell[][] getCells() { return cells; }


}
