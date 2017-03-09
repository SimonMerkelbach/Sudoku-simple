import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class Sudoku {
    // constants adjusting sizes, can be changed to change appearance of application
    private static final int FIELD_SIZE = 35;
    private static final int GAP_SIZE = 10;
    private static final int MARGIN = 20;
    // constants preserving relative sizes, used to place components relative to each other
    private static final int BUTTON_WIDTH = 3 * FIELD_SIZE + 2 * GAP_SIZE;
    private static final int BUTTON_HEIGHT = FIELD_SIZE;
    private static final int CONTENTPANE_WIDTH = 9 * FIELD_SIZE + 8 * GAP_SIZE + 2 * MARGIN;
    private static final int CONTENTPANE_HEIGHT =  9 * FIELD_SIZE + 8 * GAP_SIZE + 3 * MARGIN + BUTTON_HEIGHT;


    // object declarations
    private static JFrame jFrame;                   // window
    private static JPanel contentPane;              // contains input fields and buttons
    private static JFormattedTextField[][] fields;  // input fields
    private static JButton solveButton;
    private static JButton resetButton;

    private static NumberFormat numberFormat;       // used to filter input from input fields
    private static NumberFormatter numberFormatter; // used to filter input from input fields


    public static void main(String[] args) {
        // CONTENTPANE SETTINGS
        contentPane = new JPanel();
        contentPane.setLayout(null);    // don't use a layout manager
        contentPane.setPreferredSize(new Dimension(CONTENTPANE_WIDTH, CONTENTPANE_HEIGHT));


        // NUMBERFORMATTER SETTINGS USED TO FILTER INPUT (ONLY ACCEPT 1 - 9 AS INPUT)
        numberFormat = NumberFormat.getIntegerInstance();
        numberFormatter = new NumberFormatter(numberFormat);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(1);
        numberFormatter.setMaximum(9);


        // COMPONENT INTIIALIZATION, COMPONENT SETTINGS (SIZE, PLACEMENT, TEXT, ACTION)
        fields = new JFormattedTextField[9][9];
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                fields[i][j] = new JFormattedTextField(numberFormatter);
                fields[i][j].setBounds(FIELD_SIZE * j + GAP_SIZE * j + MARGIN,
                                        FIELD_SIZE * i + GAP_SIZE * i + MARGIN,
                                        FIELD_SIZE, FIELD_SIZE);
                fields[i][j].setHorizontalAlignment(JTextField.CENTER);
            }
        }

        solveButton = new JButton();
        solveButton.setText("Solve!");
        solveButton.setBounds(MARGIN,
                                9 * FIELD_SIZE + 8 * GAP_SIZE + 2 * MARGIN,
                                BUTTON_WIDTH, BUTTON_HEIGHT);
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
        resetButton.setBounds(MARGIN + BUTTON_WIDTH + GAP_SIZE,
                                9 * FIELD_SIZE + 8 * GAP_SIZE + 2 * MARGIN,
                                BUTTON_WIDTH, BUTTON_HEIGHT);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });


        // ADD COMPONENTS TO CONTENTPANE
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                contentPane.add(fields[i][j]);
            }
        }
        contentPane.add(solveButton);
        contentPane.add(resetButton);


        // JFRAME SETTINGS
        jFrame = new JFrame();
        jFrame.setTitle("Sudoku");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setContentPane(contentPane);
        jFrame.pack();
        jFrame.setLocationByPlatform(true);
        jFrame.setVisible(true);
    }


    // reads all fields, returns grid; empty fields = 0
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


    // sets all fields to values in grid
    public static void setGrid (int[][] grid) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                fields[i][j].setText(String.valueOf(grid[i][j]));
            }
        }
    }


    // overloaded method to ensure solveSudoku is called with row = 0 and col = 0
    public static boolean solveSudoku (int[][] grid) {
        return solveSudoku(grid, 0, 0);
    }

    // recursively solves a sudoku grid using backtracking
    private static boolean solveSudoku (int[][] grid, int row, int col) {
        // last row has been reached
        if (row == 9) {
            // reset row, advance to next column
            row = 0;
            col++;

            // if last column has been cleared, grid is solved
            if (col == 9)
                return true;
        }

        // current cell is filled, skip it
        if (grid[row][col] != 0) {
            return solveSudoku(grid, row+1,col);
        }

        // current cell is not filled, try values 1 - 9
        for (int value = 1; value <= 9; value++) {
            // if value fits, set cell, advance in grid
            if (isValid(grid, value, row, col)) {
                grid[row][col] = value;
                if (solveSudoku(grid, row+1,col))
                    return true;
            }
        }

        // values 1 - 9 didn't fit in cell, reset cell, backtrack
        grid[row][col] = 0;

        return false;
    }


    // checks whether 'value' can be placed in 'grid' at 'row' and 'col'
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


    // empties all fields
    private static void resetFields () {
        for (JFormattedTextField[] i : fields) {
            for (JFormattedTextField j : i) {
                j.setText("");
            }
        }
    }
}