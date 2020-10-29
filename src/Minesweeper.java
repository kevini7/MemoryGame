// Minesweeper
// Author: Kevin Imani
// Notes: Theme embellishment has been implemented, Go to game -> setup to select a theme


import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Stack;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Minesweeper extends JFrame implements ActionListener {

    // Game Play Objects
    private Board gameBoard;

    // layout objects
    private JPanel boardView, labelView;
    private JLabel timer, mines;
    private int cellSize;

    // records for keeping time, number of mines, etc.
    private boolean firstClick = true;
    private long gameTime;
    private Timer gameTimer;
    private Instant startTime;
    private int numMines;
    private boolean flowerThemeSelected = false, mineThemeSelected = true;

    // game options
    private JMenuBar bar;
    private JMenu gameOptions, help;
    private JMenuItem newGame, setup, exit, instruct;
    private JFrame settings;
    private JPanel buttonPanel, settingsPanel, themePanel;
    private JButton ok, cancel;
    private ButtonGroup level, theme;
    private JRadioButton beg, inter, exp, custom, mineTheme, flowerTheme;
    private JTextField customSize, customMines;
    private JLabel sizeLabel, minesLabel, themeLabel;

    // variables to store size of board
    private int boardSize;
    private int panelWidth;
    private int panelHeight;

    public Minesweeper() {

        Container cp = getContentPane();
        // Set up the board
        boardView = new JPanel();
        // set defaults for game
        boardSize = 8;
        numMines = 15;
        cellSize = 32;
        panelWidth = boardSize * cellSize;
        panelHeight = boardSize * cellSize;
        gameBoard = new Board(boardSize, this, boardView , cellSize, numMines);

        // Set the layout of the board
        boardView.setLayout(new GridLayout(boardSize, boardSize, 0, 0));

        // Set up label panel
        labelView = new JPanel();
        labelView.setLayout(new GridLayout(1, 2, 4, 5));
        mines = new JLabel("Mines: "  + numMines);
        mines.setFont(new Font("Monospace", Font.BOLD, 14));
        labelView.add(mines);
        timer = new JLabel("Time: " + gameTime);
        timer.setFont(new Font("Monospace", Font.BOLD, 14));
        labelView.add(timer);
        labelView.setBorder(new EmptyBorder(7,3,7,30));

        // Set up menu bar
        bar = new JMenuBar();
        gameOptions = new JMenu("Game");
        help = new JMenu("Help");
        newGame = new JMenuItem("New");
        setup = new JMenuItem("Setup");
        exit = new JMenuItem("Exit");
        instruct = new JMenuItem("How to Play");
        help.add(instruct);
        gameOptions.add(newGame);
        gameOptions.add(setup);
        gameOptions.add(exit);
        bar.add(gameOptions);
        bar.add(help);
        add(bar, BorderLayout.NORTH);

        // Set up the setting JDialog
        settings = new JFrame();
        settings.setSize(500,300);
        settings.setResizable(false);
        buttonPanel = new JPanel();
        settingsPanel = new JPanel(new GridLayout(6, 3, 5, 5));
        settingsPanel.setBorder(new EmptyBorder(5, 10, 5, 5));
        // setting up buttons for the settings
        ok = new JButton("Ok");
        cancel = new JButton("Cancel");
        buttonPanel.add(cancel);
        buttonPanel.add(ok);
        // button group to select difficulty
        level = new ButtonGroup();
        beg = new JRadioButton("Beginner");
        inter = new JRadioButton("Intermediate");
        exp = new JRadioButton("Expert");
        custom = new JRadioButton("Custom");
        level.add(beg);
        level.add(inter);
        level.add(exp);
        level.add(custom);
        settingsPanel.add(beg);
        settingsPanel.add(inter);
        settingsPanel.add(exp);
        settingsPanel.add(custom);
        sizeLabel = new JLabel("Board size:");
        minesLabel = new JLabel("Number of Mines:");
        customSize = new JTextField();
        customMines = new JTextField();

        // set up theme radio buttons
        themePanel = new JPanel(new GridLayout(1, 3, 2, 2));
        themePanel.setBorder(new EmptyBorder(5, 10, 5, 5));
        themeLabel = new JLabel("Select Theme");
        theme = new ButtonGroup();
        mineTheme = new JRadioButton("Mine Theme");
        flowerTheme = new JRadioButton("Flower Theme");
        theme.add(mineTheme);
        theme.add(flowerTheme);
        themePanel.add(themeLabel);
        themePanel.add(mineTheme);
        themePanel.add(flowerTheme);

        // add elements to dialog box
        settingsPanel.add(sizeLabel);
        settingsPanel.add(customSize);
        settingsPanel.add(minesLabel);
        settingsPanel.add(customMines);

        // set custom text fields to not visible for now
        customSize.setVisible(false);
        customMines.setVisible(false);
        sizeLabel.setVisible(false);
        minesLabel.setVisible(false);

        // add panels to settings dialog
        settings.add(buttonPanel, BorderLayout.SOUTH);
        settings.add(settingsPanel, BorderLayout.NORTH);
        settings.add(themePanel, BorderLayout.CENTER);


        // Add panels to container
        cp.add(labelView, BorderLayout.CENTER);
        cp.add(boardView, BorderLayout.SOUTH);
        pack();
        setVisible(true);

        // add ActionHandler to option menu
        ActionHandler handler = new ActionHandler();
        newGame.addActionListener(handler);
        setup.addActionListener(handler);
        exit.addActionListener(handler);
        instruct.addActionListener(handler);

        // Set up game timer
        ActionListener timerListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Duration runningTime = Duration.between(startTime, Instant.now());
                gameTime = runningTime.getSeconds();
                timer.setText("Time: " + gameTime);
            }
        };

        gameTimer = new Timer(1000, timerListener);

    }

    // inner class to handle option bar actions
    private class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == newGame) {
                newGame();
                repaint();
            }
            // setup is clicked in menu
            if(e.getSource() == setup) {
                // Bring up the setting dialog
                settings.setVisible(true);

                // action listener for cancel button
                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(e.getSource() == cancel)
                            settings.setVisible(false);
                    }
                });
                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(e.getSource() == ok) {

                            // Update the game based on selected settings
                            if(beg.isSelected()) {
                                boardSize = 4;
                                numMines = 4;
                                cellSize = 32;
                                panelWidth = boardSize * cellSize;
                                panelHeight = boardSize * cellSize;
                                setSize(panelWidth , panelHeight);
                                newGame();
                                settings.dispose();
                            }
                            if(inter.isSelected()) {
                                boardSize = 8;
                                numMines = 15;
                                cellSize = 32;
                                panelWidth = boardSize * cellSize;
                                panelHeight = boardSize * cellSize;
                                setSize(panelWidth , panelHeight);
                                newGame();
                                settings.dispose();
                            }
                            if(exp.isSelected()) {
                                boardSize = 12;
                                numMines = 40;
                                cellSize = 32;
                                panelWidth = boardSize * cellSize;
                                panelHeight = boardSize * cellSize;
                                setSize(panelWidth , panelHeight);
                                newGame();
                                settings.dispose();
                            }
                            if(custom.isSelected()) {
                                int size = Integer.parseInt(customSize.getText());
                                int mines = Integer.parseInt(customMines.getText());
                                // validate custom input
                                if(size >= 3 && size <= 12) {
                                    boardSize = Integer.parseInt(customSize.getText());
                                    if(mines >= 2 && mines <= (boardSize*boardSize)/2 ) {

                                        numMines = Integer.parseInt(customMines.getText());
                                        cellSize = 32;
                                        panelWidth = boardSize * cellSize;
                                        panelHeight = boardSize * cellSize;
                                        setSize(panelWidth, panelHeight);
                                        newGame();
                                        settings.setVisible(false);
                                        settings.dispose();
                                        revalidate();
                                    }
                                    else {
                                        // show error message if invalid number of mines
                                        ok.setEnabled(false);
                                        JDialog errorDialog = new JDialog(new JFrame(), "Invalid number of mines", true);
                                        JLabel message = new JLabel("Number of mines must be between 2 and " + (boardSize*boardSize)/2);
                                        message.setHorizontalAlignment(JLabel.CENTER);
                                        message.setVerticalAlignment(JLabel.CENTER);
                                        message.setFont(new Font("Monospace", Font.BOLD, 12));
                                        errorDialog.add(message, BorderLayout.CENTER);
                                        errorDialog.setSize(350, 100);
                                        JButton confirm = new JButton("Ok");
                                        errorDialog.add(confirm, BorderLayout.SOUTH);
                                        confirm.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                errorDialog.dispose();
                                            }
                                        });
                                        errorDialog.setVisible(true);
                                    }

                                }
                                else {
                                    // show error message if invalid board size
                                    ok.setEnabled(false);
                                    JDialog errorDialog = new JDialog(new JFrame(), "Invalid board size", true);
                                    JLabel message = new JLabel("Board size must be between 3 and 12");
                                    message.setHorizontalAlignment(JLabel.CENTER);
                                    message.setVerticalAlignment(JLabel.CENTER);
                                    message.setFont(new Font("Monospace", Font.BOLD, 12));
                                    errorDialog.add(message, BorderLayout.CENTER);
                                    errorDialog.setSize(350, 100);
                                    JButton confirm = new JButton("Ok");
                                    errorDialog.add(confirm, BorderLayout.SOUTH);
                                    confirm.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            errorDialog.dispose();
                                        }
                                    });
                                    errorDialog.setVisible(true);
                                }
                            }

                            // set the theme based on what is selected
                            if(mineTheme.isSelected()) {
                                mineThemeSelected = true;
                                flowerThemeSelected = false;
                            }
                            if(flowerTheme.isSelected()) {
                                flowerThemeSelected = true;
                                mineThemeSelected = false;
                            }

                        }
                        ok.setEnabled(true);

                    }
                });
                // action listener to see if custom radio button is selected
                custom.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        customSize.setVisible(true);
                        customMines.setVisible(true);
                        sizeLabel.setVisible(true);
                        minesLabel.setVisible(true);
                        customSize.setText(String.valueOf(boardSize));
                        customMines.setText(String.valueOf(numMines));
                    }
                });

            }

            if(e.getSource() == exit)
                System.exit(0);

            if(e.getSource() == instruct) {
                JDialog helpDialog = new JDialog(new JFrame(), "Minesweeper help", true);
                JTextArea message = new JTextArea("How to play: Your goal is to clear the board without clicking on a mine. Each cell has 8 neighbors. Select a cell and the image will be revealed." +
                        "If it is a blank cell, there is no mine around. If the cell has a 1, there is one mine neighboring that cell. If the cell has a 2, there are 2 mines neighboring that cell" +
                        "If the cell has a 3, there are three mines neighboring that cell. If you click a cell with a mine, the game is over. If you clear all cells that do not have mines, " +
                        "you win!");
                message.setLineWrap(true);
                message.setWrapStyleWord(true);
                message.setEditable(false);
                message.setFont(new Font("Monospace", Font.BOLD, 14));
                helpDialog.add(message, BorderLayout.CENTER);
                helpDialog.setSize(350, 250);
                pack();
                JButton confirm = new JButton("Ok");
                helpDialog.add(confirm, BorderLayout.SOUTH);
                confirm.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        helpDialog.dispose();
                    }
                });
                helpDialog.setVisible(true);
            }

        }
    }



    // handle clicks on the gameboard
    public void actionPerformed(ActionEvent e) {
        Cell cells[][] = gameBoard.getCells();
        // Check for win
        if(checkWin() == true) {
            gameTimer.stop();
            System.out.println("YOU WON!");
            // Show mines on win and remove action listeners
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    cells[i][j].removeActionListener(this); // remove action listeners for all cells
                    if (cells[i][j].hasMine()) {
                        cells[i][j].showMine(); // show mines
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Congrats! You Won!");
        }

        Cell currCell = (Cell) e.getSource();

        if(firstClick == true) {
            // start the timer
            gameTimer.start();
            startTime = Instant.now();
            // if the first cell that is clicked has a mine, swap it with another cell that does not have one
            if(currCell.hasMine()) {
                currCell.setHasMine(false);
                int row = 0;
                int col = 0;
                boolean flag = false;
                while (!flag) {
                    if (cells[row][col].hasMine() == false) {
                        cells[row][col].setHasMine(true);
                        flag = true;
                    } else {
                        row += 1;
                        col += 1;
                    }
                }
            }
            firstClick = false;
        }

        currCell.removeActionListener(this); // remove action listener once clicked
        currCell.setIsClicked(true);
        revealCell(currCell);
    }

    public void clearCells(Cell c) {
        Cell cells[][] = gameBoard.getCells();
        Stack<Cell> cellStack = new Stack<>();

        Cell poppedCell;
        int row, col;
        if(hasNeighborMine(c) == 0) {
            cellStack.push(c);
            // algorithm to clear cells
            while(!cellStack.isEmpty()) {
                poppedCell = cellStack.pop();
                // Get row and col of popped cell
                row = poppedCell.getRow();
                col = poppedCell.getCol();
                // Check if cell is surrounded by a mine
                // If not surrounded by mines
                if(hasNeighborMine(poppedCell) == 0) {
                    poppedCell.showBlank();
                    repaint();
                    poppedCell.setIsClicked(true);
                    poppedCell.removeActionListener(this);
                    // Add neighboring cells to stack, check for out of bounds and if the cell has already been clicked
                    if(row-1 != -1 && cells[row-1][col].getIsClicked() == false) // north cell
                        cellStack.push(cells[row-1][col]);
                    if(row-1 != -1 && col+1 != boardSize && cells[row-1][col+1].getIsClicked() == false) // northeast
                        cellStack.push(cells[row-1][col+1]);
                    if(col+1 != boardSize && cells[row][col+1].getIsClicked() == false) // east
                        cellStack.push(cells[row][col+1]);
                    if(row+1 != boardSize && col+1 != boardSize && cells[row+1][col+1].getIsClicked() == false) // southeast
                        cellStack.push(cells[row+1][col+1]);
                    if(row+1 != boardSize && cells[row+1][col].getIsClicked() == false) // south
                        cellStack.push(cells[row+1][col]);
                    if(row+1 != boardSize && col-1 != -1 && cells[row+1][col-1].getIsClicked() == false) // southwest
                        cellStack.push(cells[row+1][col-1]);
                    if(col-1 != -1 && cells[row][col-1].getIsClicked() == false) // west
                        cellStack.push(cells[row][col-1]);
                    if(row-1 != -1 && col-1 != -1 && cells[row-1][col-1].getIsClicked() == false) // northwest
                        cellStack.push(cells[row-1][col-1]);

                }

                // otherwise it is surrounded by mines
                if(hasNeighborMine(poppedCell) > 0) {

                    if(hasNeighborMine(poppedCell) == 1 && poppedCell.getIsClicked() == false)
                        poppedCell.showOne();
                    if(hasNeighborMine(poppedCell) == 2 && poppedCell.getIsClicked() == false)
                        poppedCell.showTwo();
                    if(hasNeighborMine(poppedCell) == 3 && poppedCell.getIsClicked() == false)
                        poppedCell.showThree();

                    repaint();
                    poppedCell.setIsClicked(true);
                    poppedCell.removeActionListener(this);
                }
            }
        }
    }

    public void revealCell(Cell c) {
        // Check if clicked cell is a mine
        Cell cells[][] = gameBoard.getCells();
        if (c.hasMine()) {
                gameTimer.stop(); // game over so stop timer
                System.out.println("Clicked a mine");
                // Handle game loss due to clicking mine
                c.setMineLose(true);
                revealAllCells(cells); // game over so reveal all cells
        }
        // Check if clicked cell has mines around it
        else if (hasNeighborMine(c) == 1) {
            c.showOne();
        }
        else if (hasNeighborMine(c) == 2) {
            c.showTwo();
        }
        else if (hasNeighborMine(c) == 3) {
            c.showThree();
        }
        // otherwise it is a blank cell
        else {
            c.showBlank();
            // Clear other non-mine cells around clicked cell
            clearCells(c);

        }
        c.setIsClicked(true);
        c.removeActionListener(this);

    }

    // method to reveal all cells on the board
    public void revealAllCells(Cell cells[][]) {
        Cell c;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                    c = cells[i][j];
                    c.setIsClicked(true);
                    if(c.getMineLose()) {
                        if(mineThemeSelected)
                            c.showRedMine();
                        if(flowerThemeSelected)
                            c.showFlower();
                    }
                    else if(c.hasMine()) {
                        if(mineThemeSelected)
                            c.showMine();
                        if(flowerThemeSelected)
                            c.showFlower();
                    }
                    else if(hasNeighborMine(c) == 1)
                        c.showOne();
                    else if(hasNeighborMine(c) == 2)
                        c.showTwo();
                    else if(hasNeighborMine(c) == 3)
                        c.showThree();
                    else if(c.hasMine() == false)
                        c.showBlank();
                    c.removeActionListener(this);
                    repaint();
                }
            }
    }

    // method to check for win
    public boolean checkWin() {
        boolean win = false;
        // Check for win condition
        Cell cells[][] = gameBoard.getCells();
        int cellCounter = 1;
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                if(cells[i][j].getIsClicked() == true && cells[i][j].hasMine() == false) {
                    cellCounter += 1;
                }
            }
        }
        if((boardSize*boardSize - numMines ) == cellCounter) { // game is won
            win = true;
        }
        return win;
    }

    // Check if current Cell has neighboring mines
    public int hasNeighborMine(Cell c) {
        Cell cells[][] = gameBoard.getCells();
        int result = 0;
        int currRow = c.getRow();
        int currCol = c.getCol();
        Cell north = null, northeast = null, east = null, southeast = null, south = null, southwest = null, west = null, northwest = null;
        // Check for out of bounds and if not assign cells
        if(currRow != 0)
            north = cells[currRow-1][currCol];
        if(currRow != 0 && currCol != boardSize-1)
            northeast = cells[currRow-1][currCol+1];
        if(currCol != boardSize-1)
            east = cells[currRow][currCol+1];
        if(currRow != boardSize-1 && currCol != boardSize-1)
            southeast = cells[currRow+1][currCol+1];
        if(currRow != boardSize-1)
            south = cells[currRow+1][currCol];
        if(currRow != boardSize-1 && currCol != 0)
            southwest = cells[currRow+1][currCol-1];
        if(currCol != 0)
            west = cells[currRow][currCol-1];
        if(currRow != 0 && currCol != 0)
            northwest = cells[currRow-1][currCol-1];
        // case for north cell
        if(north != null && north.hasMine())
            result += 1;
        // case for northeast cell
        if(northeast != null && northeast.hasMine())
            result += 1;
        // case for east cell
        if(east != null && east.hasMine())
            result += 1;
        // case for southeast cell
        if(southeast != null && southeast.hasMine())
            result += 1;
        // case for south cell
        if(south != null && south.hasMine())
            result += 1;
        // case for southwest cell
        if(southwest != null && southwest.hasMine())
            result += 1;
        // case for west cell
        if(west != null && west.hasMine())
            result += 1;
        // case for northwest cell
        if(northwest != null && northwest.hasMine())
            result += 1;

        return result;
    }

    public void newGame() {
        gameTimer.stop();
        gameTime = 0;
        firstClick = true;
        timer.setText("Time: " + gameTime);
        mines.setText("Mines: " + numMines);
        boardView.removeAll();
        boardView.revalidate();
        boardView.setLayout(new GridLayout(boardSize, boardSize, 0, 0));
        gameBoard = new Board(boardSize, this, boardView , cellSize, numMines);
        repaint();
        pack();
    }

    public static void main(String[] args) {
        Minesweeper M = new Minesweeper();
        M.setSize(M.panelWidth+5, M.panelHeight);
        M.setResizable(true);
        M.pack();
        M.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
    }



}














