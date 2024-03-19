public class Main {
    public static void main(String[] args) {
        int progress = 0;

        int[][] givenGrid = new int[][]{
                {0, 0, 3,    2, 0, 0,    0, 0, 0},
                {0, 7, 4,    0, 0, 0,    5, 3, 0},
                {0, 6, 0,    0, 7, 0,    9, 0, 1},

                {6, 0, 7,    0, 5, 3,    8, 4, 9},
                {1, 0, 8,    9, 2, 7,    3, 6, 0},
                {5, 0, 9,    4, 0, 0,    0, 0, 0},

                {0, 9, 0,    0, 0, 0,    0, 1, 0},
                {0, 0, 6,    0, 3, 1,    0, 0, 0},
                {0, 0, 0,    6, 0, 2,    4, 0, 3}
        };

        Sudoku sudoku = new Sudoku(givenGrid);

        while (sudoku.UNSOLVED > 0) {
            progress = sudoku.solveSudoku();
            if (progress == 0) {
                break;
            }
        }

        if (sudoku.UNSOLVED == 0) {
            System.out.println("Congratulations! You have solved the puzzle.");
        } else {
            System.out.println("Can no longer make any progress :(");
        }

        sudoku.printSudoku();
    }
}