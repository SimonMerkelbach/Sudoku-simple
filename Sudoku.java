import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class Sudoku {
    // constants adjusting size
    private static final int FIELD_SIZE = 25;
    private static final int GAP_SIZE = 10;
    private static final int MARGIN = 15;
    private static final int BUTTON_WIDTH = 3 * FIELD_SIZE + 2 * GAP_SIZE;
    private static final int BUTTON_HEIGHT = FIELD_SIZE;
    private static final int CONTENTPANE_WIDTH = 9 * FIELD_SIZE + 8 * GAP_SIZE + 2 * MARGIN;
    private static final int CONTENTPANE_HEIGHT =  9 * FIELD_SIZE + 8 * GAP_SIZE + 3 * MARGIN + BUTTON_HEIGHT;


    // object declarations
    private static JFrame jFrame;                   // window
    private static JPanel contentPane;              // contains input fields and buttons
    private static JFormattedTextField[][] fields;  // input
    private static JButton solveButton;
    private static JButton resetButton;
    private static JButton generateButton;

    private static NumberFormat numberFormat;
    private static NumberFormatter numberFormatter;


    public static void main(String[] args) {
        // contentPane settings
        contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setPreferredSize(new Dimension(CONTENTPANE_WIDTH, CONTENTPANE_HEIGHT));


        // numberformatter used to filter input
        numberFormat = NumberFormat.getIntegerInstance();
        numberFormatter = new NumberFormatter(numberFormat);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(1);
        numberFormatter.setMaximum(9);


        // component initialization, component settings, relative component placement
        fields = new JFormattedTextField[9][9];
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                fields[i][j] = new JFormattedTextField(numberFormatter);
                fields[i][j].setBounds(FIELD_SIZE * j + GAP_SIZE * j + MARGIN, FIELD_SIZE * i + GAP_SIZE * i + MARGIN, FIELD_SIZE, FIELD_SIZE);
            }
        }

        solveButton = new JButton();
        solveButton.setText("Solve!");
        solveButton.setBounds(MARGIN, 9 * FIELD_SIZE + 8 * GAP_SIZE + 2 * MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT);
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] grid = getGrid();
                solveSudoku(grid);
                setGrid(grid);
            }
        });

        resetButton = new JButton();
        resetButton.setText("Reset!");
        resetButton.setBounds(MARGIN + BUTTON_WIDTH + GAP_SIZE, 9 * FIELD_SIZE + 8 * GAP_SIZE + 2 * MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });

        generateButton = new JButton();
        generateButton.setText("Random!");
        generateButton.setBounds(MARGIN + 2 * BUTTON_WIDTH + 2 * GAP_SIZE, 9 * FIELD_SIZE + 8 * GAP_SIZE + 2 * MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT);
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateSudoku();
            }
        });


        // add components to contentPane
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                contentPane.add(fields[i][j]);
            }
        }
        contentPane.add(solveButton);
        contentPane.add(resetButton);
        contentPane.add(generateButton);


        // JFrame settings
        jFrame = new JFrame();
        jFrame.setTitle("Sudoku");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setContentPane(contentPane);
        jFrame.pack();
        jFrame.setLocationByPlatform(true);
        jFrame.setVisible(true);
    }
    
    
    public static int[][] getGrid () {
        int[][] grid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (fields[i][j].getText().equals("")) {
                    grid[i][j] = 0;
                }
                else {
                    grid[i][j] = Integer.parseInt(fields[i][j].getText());
                }
            }
        }
        return grid;
    }
    
    public static void setGrid (int[][] grid) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                fields[i][j].setText(String.valueOf(grid[i][j]));
            }
        }
    }




    public static boolean solveSudoku (int[][] grid) {
        return solveSudoku(grid, 0, 0);
    }

    public static boolean solveSudoku (int[][] grid, int row, int col) {
        // sudoku is solved
        if (col > 9) {
            return true;
        }

        // current cell is set
        else if (grid[row][col] != 0) {
            if (row == 8) {
                solveSudoku(grid, 0, col+1);
            }
            else {
                solveSudoku(grid, row+1, col);
            }
        }

        // current cell is empty
        else {
            for (int value = 1; value <= 9; value++) {
                if (isValid(grid, value, row, col)) {
                    grid[row][col] = value;
                    if (row == 8) {
                        solveSudoku(grid, 0, col+1);
                    }
                    else {
                        solveSudoku(grid, row+1, col);
                    }
                }
            }
        }
        return false;
    }

    private static boolean isValid (int[][] grid, int value, int row, int col) {
        // check rows and columns
        for (int i = 0; i < 9; i++) {
            if (grid[i][col] == value) {
                return false;
            }
            if (grid[row][i] == value) {
                return false;
            }
        }

        // check boxes
        int boxStartRow = (row / 3) * 3;
        int boxStartCol = (col / 3) * 3;
        for (int i = boxStartRow; i < boxStartRow + 3; i++) {
            for (int j = boxStartCol; j < boxStartCol + 3; j++) {
                if (grid[i][j] == value) {
                    return false;
                }
            }
        }

        // value can be placed in grid[row][col]
        return true;
    }

    private static void resetFields () {
        for (JFormattedTextField[] i : fields) {
            for (JFormattedTextField j : i) {
                j.setText("");
            }
        }
    }

    private static void generateSudoku () {

    }
}
