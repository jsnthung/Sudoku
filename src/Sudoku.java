public class Sudoku {
    Cell[][] grid = new Cell[9][9];
    Cell[][] boxes = new Cell[9][9];

    public Sudoku (int[][] sudoku) {
        int i, j, k;
        int boxNumber = 0;

        // assign each cell its corresponding digit, row number, column number
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                grid[i][j] = new Cell();

                grid[i][j].digit = sudoku[i][j];
                grid[i][j].row = i;
                grid[i][j].column = j;

                // if a digit is given, then there is no pencil mark
                if (grid[i][j].digit != 0) {
                    for (k = 0; k < 9; k++) {
                        grid[i][j].pencilmark[k] = 0;
                        grid[i][j].numOfPencilmark = 0;
                    }
                }
                // if no digit is given, then put all pencil mark
                else {
                    for (k = 0; k < 9; k++) {
                        grid[i][j].pencilmark[k] = 1;
                        grid[i][j].numOfPencilmark = 9;
                    }
                }
            }
        }

        // assign each cell to its corresponding box
        for (i = 0, k = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                boxes[boxNumber][k] = grid[i][j];
                k++;

                if (j == 2) {
                    boxNumber++;
                    if (i % 3 == 0) {
                        k = 0;
                    } else if (i % 3 == 1) {
                        k = 3;
                    } else {
                        k = 6;
                    }
                }
                if (j == 5) {
                    boxNumber++;
                    if (i % 3 == 0) {
                        k = 0;
                    } else if (i % 3 == 1) {
                        k = 3;
                    } else {
                        k = 6;
                    }
                }
            }

            boxNumber -= 2;
            if (i == 2) {
                boxNumber = 3;
                k = 0;
            }
            if (i == 5) {
                boxNumber = 6;
                k = 0;
            }
        }

        // basic row rule
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                if (grid[i][j].digit != 0) {
                    for (k = 0; k < 9; k++) {
                        if (grid[i][k].pencilmark[grid[i][j].digit - 1] == 1) {
                            grid[i][k].pencilmark[grid[i][j].digit - 1] = 0;
                            grid[i][k].numOfPencilmark--;
                        }
                    }
                }
            }
        }

        // basic column rule
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                if (grid[j][i].digit != 0) {
                    for (k = 0; k < 9; k++) {
                        if (grid[k][i].pencilmark[grid[j][i].digit - 1] == 1) {
                            grid[k][i].pencilmark[grid[j][i].digit - 1] = 0;
                            grid[k][i].numOfPencilmark--;
                        }
                    }
                }
            }
        }

        // basic box rule
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                if (boxes[i][j].digit != 0) {
                    for (k = 0; k < 9; k++) {
                        if (boxes[i][k].pencilmark[boxes[i][j].digit - 1] == 1) {
                            boxes[i][k].pencilmark[boxes[i][j].digit - 1] = 0;
                            boxes[i][k].numOfPencilmark--;
                        }
                    }
                }
            }
        }
    }

    public void printSudoku () {
        int i, j;

        System.out.println("-------------------------------");
        for (i = 0; i < 9; i++) {
            System.out.print("|");
            for (j = 0; j < 9; j++) {
                System.out.print(" " + this.grid[i][j].digit + " ");

                if ((j + 1) % 3 == 0) {
                    System.out.print("|");
                }
            }

            System.out.println();

            if ((i + 1) % 3 == 0) {
                System.out.println("-------------------------------");
            }
        }
    }

    public void foundDigit (int row, int column) {
        int i, boxNumber;

        // remove pencil mark on cells in the same row
        for (i = 0; i < 9; i++) {
            if (grid[row][i].pencilmark[grid[row][column].digit - 1] == 1) {
                grid[row][i].pencilmark[grid[row][column].digit - 1] = 0;
                grid[row][i].numOfPencilmark--;
            }
        }

        // remove pencil mark on cells in the same column
        for (i = 0; i < 9; i++) {
            if (grid[i][column].pencilmark[grid[row][column].digit - 1] == 1) {
                grid[i][column].pencilmark[grid[row][column].digit - 1] = 0;
                grid[i][column].numOfPencilmark--;
            }
        }

        // remove pencil mark on cells in the same box
        if (row < 3) {
            boxNumber = 2;
        } else if (row < 6) {
            boxNumber = 5;
        } else {
            boxNumber = 8;
        }

        if (column < 3) {
            boxNumber -= 2;
        } else if (column < 6) {
            boxNumber--;
        }

        for (i = 0; i < 9; i++) {
            if (boxes[boxNumber][i].pencilmark[grid[row][column].digit - 1] == 1) {
                boxes[boxNumber][i].pencilmark[grid[row][column].digit - 1] = 0;
                boxes[boxNumber][i].numOfPencilmark--;
            }
        }
    }

    public void nakedSingle () {
        int i, j, k;

        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                if (grid[i][j].numOfPencilmark == 1) {
                    for (k = 0; k < 9; k++) {
                        if (grid[i][j].pencilmark[k] == 1) {
                            grid[i][j].digit = k + 1;
                            grid[i][j].pencilmark[k] = 0;
                        }
                    }
                }
            }
        }
    }

    public void hiddenSingle () {
        // in row
        // in column
        // in box
    }
}
