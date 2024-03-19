public class Main {
    public static void main(String[] args) {
        int[][] givenGrid = new int[][]{
                {0, 0, 0,    6, 9, 7,    0, 0, 0},
                {0, 6, 3,    0, 0, 0,    4, 1, 0},
                {0, 0, 7,    0, 0, 0,    9, 0, 0},

                {0, 9, 4,    0, 1, 0,    3, 2, 0},
                {2, 0, 0,    0, 8, 0,    0, 0, 9},
                {5, 0, 0,    2, 0, 9,    0, 0, 1},

                {0, 0, 0,    1, 0, 2,    0, 0, 0},
                {0, 0, 2,    0, 0, 0,    7, 0, 0},
                {0, 4, 9,    0, 7, 0,    1, 5, 0}
        };

        Sudoku sudoku = new Sudoku(givenGrid);

        sudoku.printSudoku();
    }
}