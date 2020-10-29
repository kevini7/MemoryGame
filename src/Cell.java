import javax.swing.*;

public class Cell extends JButton {

    // Resource loader
    private ClassLoader loader = getClass().getClassLoader();

    // icons for cells
    private Icon mine = new ImageIcon(loader.getResource("img/mine.png"));
    private Icon redMine = new ImageIcon(loader.getResource("img/red_mine.jpeg"));
    private Icon blank = new ImageIcon(loader.getResource("img/blank.png"));
    private Icon one = new ImageIcon(loader.getResource("img/num1.png"));
    private Icon two = new ImageIcon(loader.getResource("img/num2.png"));
    private Icon three = new ImageIcon(loader.getResource("img/num3.png"));
    private Icon flower = new ImageIcon(loader.getResource("img/flower.png"));

    // attributes for cells
    private int row, col;
    private boolean hasMine;
    private boolean mineLose;
    private boolean isClicked;

    // Constructor with notClicked cell initialization
    public Cell() {
        super();
        isClicked = false;
        hasMine = false;
        mineLose = false;
    }

    public  void setCellImage(Icon img) { setIcon(img);}

    public void showBlank() { setIcon(blank); }
    public void showMine() { setIcon(mine); }
    public void showRedMine() { setIcon(redMine); }
    public void showOne() { setIcon(one); }
    public void showTwo() { setIcon(two); }
    public void showThree() { setIcon(three); }
    public void showFlower() { setIcon(flower); }

    public void setHasMine(Boolean b) { hasMine = b; }
    public boolean hasMine() { return hasMine; }
    public void setMineLose(Boolean b) { mineLose = b; }
    public boolean getMineLose() { return mineLose; }

    public void setRow(int r) { row = r; }
    public void setCol(int c) { col = c; }
    public int getRow() { return row; }
    public int getCol() { return col; }

    public void setIsClicked(Boolean b) { isClicked = b; }
    public boolean getIsClicked() { return isClicked; }

    public Icon getMineIcon() { return mine; }






}
