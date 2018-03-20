/*
    Reference https://github.com/bob-carpenter/java-sudoku/blob/master/Sudoku.java
*/
class Sudoku {
    public static boolean solve(int i, int j, int[][] cells) {
        if (i == 9) {
            i = 0;

            if (++j == 9) {
                return true;
            }
        }
       
        if (cells[i][j] != 0) {
            return solve(i + 1, j, cells);
        }
       
        for (int value = 1; value <= 9; ++value) {
            if (Sudoku.legal(i, j, value, cells)) {
                cells[i][j] = value;
                if (solve(i + 1, j, cells)) {
                    return true;
                }
            }
        }
        cells[i][j] = 0; 
        return false;
    }
   
    public static boolean legal(int i, int j, int value, int[][] cells) {
        for (int k = 0; k < 9; ++k) {
            if (value == cells[k][j]) {
                return false;
            }
        }
       
        for (int k = 0; k < 9; ++k) {
            if (value == cells[i][k]) {
                return false;
            }
        }
       
        int boxRowOffset = (i / 3) * 3;
        int boxColumnOffset = (j / 3) * 3;
       
        for (int k = 0; k < 3; ++k) {
            for (int m = 0; m < 3; ++m) {
                if (value == cells[boxRowOffset + k][boxColumnOffset + m]) {
                    return false;
                }
            }
        }
        return true;
    }
}

public class App {
    public static void main(String args[]) {
        /*
         *     0 1 2   3 4 5   6 7 8
         *    -----------------------
         * 0 |   8   | 4   2 |   6   |
         * 1 |   3 4 |       | 9 1   |
         * 2 | 9 6   |       |   8 4 |
         *    -----------------------
         * 3 |       | 2 1 6 |       |
         * 4 |       |       |       |
         * 5 |       | 3 5 7 |       |
         *   -----------------------
         * 6 | 8 4   |       |   7 5 |
         * 7 |   2 6 |       | 1 3   |
         * 8 |   9   | 7   1 |   4   |
         *    -----------------------
         */
        int[][] game = new int[][] {
            {0, 8, 0, 4, 0, 2, 0, 6, 0},
            {0, 3, 4, 0, 0, 0, 9, 1, 0},
            {9, 6, 0, 0, 0, 0, 0, 8, 4},
            {0, 0, 0, 2, 1, 6, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 3, 5, 7, 0, 0, 0},
            {8, 4, 0, 0, 0, 0, 0, 7, 5},
            {0, 2, 6, 0, 0, 0, 1, 3, 0},
            {0, 9, 0, 7, 0, 1, 0, 4, 0}
        };

        if (Sudoku.solve(0, 0, game))
            System.out.println("Can solve.");
        else
            System.out.println("Cannot solve.");
           
    }
}
