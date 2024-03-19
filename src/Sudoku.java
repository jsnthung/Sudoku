import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sudoku {
    Cell[][] grid = new Cell[9][9];
    Cell[][] boxes = new Cell[9][9];
    int UNSOLVED;

    public Sudoku (int[][] sudoku) {
        int i, j, k;
        int boxNumber = 0;
        this.UNSOLVED = 81;

        // assign each cell its corresponding digit, row number, column number
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                grid[i][j] = new Cell();

                grid[i][j].digit = sudoku[i][j];
                grid[i][j].row = i;
                grid[i][j].column = j;

                // if a digit is given, then there is no pencil mark
                if (grid[i][j].digit != 0) {
                    this.UNSOLVED--;
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

    public void removePencilmarks (int row, int column) {
        int i;
        for (i = 0; i < 9; i++) {
            grid[row][column].pencilmark[i] = 0;
        }
    }
    public List<Cell> cellsSeen (Cell cell) {
        int i;
        List<Cell> cellList = new ArrayList<>();

        // in same row
        for (i = 0; i < 9; i++) {
            cellList.add(grid[cell.row][i]);
        }

        // in same column
        for (i = 0; i < 9; i++) {
            cellList.add(grid[i][cell.column]);
        }

        // in same box
        for (i = 0; i < 9; i++) {
            cellList.add(boxes[cell.getBoxNumber()][i]);
        }

        cellList = cellList.stream().distinct().collect(Collectors.toList());
        cellList.remove(cell);

        return cellList;
    }
    public int solveSudoku () {
        if (nakedSingle() == 1) {
            return 1;
        }

        if (hiddenSingle() == 1) {
            return 1;
        }

        if (nakedPair() == 1) {
            return 1;
        }

        return 0;
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

    public int nakedSingle () {
        int i, j, k;

        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                if (grid[i][j].numOfPencilmark == 1) {
                    for (k = 0; k < 9; k++) {
                        if (grid[i][j].pencilmark[k] == 1) {
                            grid[i][j].digit = k + 1;
                            this.UNSOLVED--;
                            removePencilmarks(i, j);
                            grid[i][j].numOfPencilmark = 0;
                            foundDigit(i, j);

                            return 1;
                        }
                    }
                }
            }
        }

        return 0;
    }
    public int hiddenSingle () {
        int i, j, k;
        int [] pencilmarkCount = new int[9];

        // in row
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                pencilmarkCount[j] = 0;
            }

            for (j = 0; j < 9; j++) {
                for (k = 0; k < 9; k++) {
                    if (grid[i][j].pencilmark[k] == 1) {
                        pencilmarkCount[k]++;
                    }
                }
            }

            for (j = 0; j < 9; j++) {
                if (pencilmarkCount[j] == 1) {
                    for (k = 0; k < 9; k++) {
                        if (grid[i][k].pencilmark[j] == 1) {
                            grid[i][k].digit = j + 1;
                            this.UNSOLVED--;
                            removePencilmarks(i, k);
                            grid[i][k].numOfPencilmark = 0;
                            foundDigit(i, k);

                            return 1;
                        }
                    }
                }
            }
        }

        // in column
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                pencilmarkCount[j] = 0;
            }

            for (j = 0; j < 9; j++) {
                for (k = 0; k < 9; k++) {
                    if (grid[j][i].pencilmark[k] == 1) {
                        pencilmarkCount[k]++;
                    }
                }
            }

            for (j = 0; j < 9; j++) {
                if (pencilmarkCount[j] == 1) {
                    for (k = 0; k < 9; k++) {
                        if (grid[k][i].pencilmark[j] == 1) {
                            grid[k][i].digit = j + 1;
                            this.UNSOLVED--;
                            removePencilmarks(k, i);
                            grid[k][i].numOfPencilmark = 0;
                            foundDigit(k, i);

                            return 1;
                        }
                    }
                }
            }
        }

        // in box
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                pencilmarkCount[j] = 0;
            }

            for (j = 0; j < 9; j++) {
                for (k = 0; k < 9; k++) {
                    if (boxes[i][j].pencilmark[k] == 1) {
                        pencilmarkCount[k]++;
                    }
                }
            }

            for (j = 0; j < 9; j++) {
                if (pencilmarkCount[j] == 1) {
                    for (k = 0; k < 9; k++) {
                        if (boxes[i][k].pencilmark[j] == 1) {
                            boxes[i][k].digit = j + 1;
                            this.UNSOLVED--;
                            removePencilmarks(i, k);
                            boxes[i][k].numOfPencilmark = 0;
                            foundDigit(i, k);

                            return 1;
                        }
                    }
                }
            }
        }

        return 0;
    }

    public int nakedPair () {
        int i, j, k, x, y;
        Cell firstPair;
        Cell secondPair;
        int firstDigit = 0, secondDigit = 0;
        boolean madeProgress = false;
        boolean foundPair = true;

        // in row
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                if (grid[i][j].numOfPencilmark == 2) {
                    firstPair = grid[i][j];

                    for (x = 0; x < 9; x++) {
                        if (grid[i][j].pencilmark[x] == 1) {
                            firstDigit = x + 1;

                            for (y = x + 1; y < 9; y++) {
                                if (grid[i][j].pencilmark[y] == 1) {
                                    secondDigit = y + 1;
                                    break;
                                }
                            }
                            break;
                        }
                    }

                    for (k = j + 1; k < 9; k++) {
                        if (grid[i][k].numOfPencilmark == 2) {
                            secondPair = grid[i][k];
                            foundPair = true;

                            for (x = 0; x < 9; x++) {
                                if (firstPair.pencilmark[x] != secondPair.pencilmark[x]) {
                                    foundPair = false;
                                    break;
                                }
                            }

                            if (foundPair) {
                                madeProgress = false;

                                for (x = 0; x < 9; x++) {
                                    if (x != firstPair.column && x != secondPair.column) {
                                        if (grid[firstPair.row][x].pencilmark[firstDigit - 1] == 1) {
                                            grid[firstPair.row][x].pencilmark[firstDigit - 1] = 0;
                                            grid[firstPair.row][x].numOfPencilmark--;
                                            madeProgress = true;
                                        }

                                        if (grid[firstPair.row][x].pencilmark[secondDigit - 1] == 1) {
                                            grid[firstPair.row][x].pencilmark[secondDigit - 1] = 0;
                                            grid[firstPair.row][x].numOfPencilmark--;
                                            madeProgress = true;
                                        }
                                    }
                                }

                                if (madeProgress) {
                                    return 1;
                                }
                            }
                        }
                    }
                }
            }
        }

        // in column
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                if (grid[j][i].numOfPencilmark == 2) {
                    firstPair = grid[j][i];

                    for (x = 0; x < 9; x++) {
                        if (grid[j][i].pencilmark[x] == 1) {
                            firstDigit = x + 1;

                            for (y = x + 1; y < 9; y++) {
                                if (grid[j][i].pencilmark[y] == 1) {
                                    secondDigit = y + 1;
                                    break;
                                }
                            }
                            break;
                        }
                    }

                    for (k = j + 1; k < 9; k++) {
                        if (grid[k][i].numOfPencilmark == 2) {
                            secondPair = grid[k][i];
                            foundPair = true;

                            for (x = 0; x < 9; x++) {
                                if (firstPair.pencilmark[x] != secondPair.pencilmark[x]) {
                                    foundPair = false;
                                    break;
                                }
                            }

                            if (foundPair) {
                                madeProgress = false;

                                for (x = 0; x < 9; x++) {
                                    if (x != firstPair.row && x != secondPair.row) {
                                        if (grid[x][firstPair.column].pencilmark[firstDigit - 1] == 1) {
                                            grid[x][firstPair.column].pencilmark[firstDigit - 1] = 0;
                                            grid[x][firstPair.column].numOfPencilmark--;
                                            madeProgress = true;
                                        }

                                        if (grid[x][firstPair.column].pencilmark[secondDigit - 1] == 1) {
                                            grid[x][firstPair.column].pencilmark[secondDigit - 1] = 0;
                                            grid[x][firstPair.column].numOfPencilmark--;
                                            madeProgress = true;
                                        }
                                    }
                                }

                                if (madeProgress) {
                                    return 1;
                                }
                            }
                        }
                    }
                }
            }
        }

        // in box
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                if (boxes[i][j].numOfPencilmark == 2) {
                    firstPair = boxes[i][j];

                    for (x = 0; x < 9; x++) {
                        if (boxes[i][j].pencilmark[x] == 1) {
                            firstDigit = x + 1;

                            for (y = x + 1; y < 9; y++) {
                                if (boxes[i][j].pencilmark[y] == 1) {
                                    secondDigit = y + 1;
                                    break;
                                }
                            }
                            break;
                        }
                    }

                    for (k = j + 1; k < 9; k++) {
                        if (boxes[i][k].numOfPencilmark == 2) {
                            secondPair = boxes[i][k];
                            foundPair = true;

                            for (x = 0; x < 9; x++) {
                                if (firstPair.pencilmark[x] != secondPair.pencilmark[x]) {
                                    foundPair = false;
                                    break;
                                }
                            }

                            if (foundPair) {
                                madeProgress = false;

                                for (x = 0; x < 9; x++) {
                                    if (x != firstPair.indexInBox() && x != secondPair.indexInBox()) {
                                        if (boxes[i][x].pencilmark[firstDigit - 1] == 1) {
                                            boxes[i][x].pencilmark[firstDigit - 1] = 0;
                                            boxes[i][x].numOfPencilmark--;
                                            madeProgress = true;
                                        }

                                        if (boxes[i][x].pencilmark[secondDigit - 1] == 1) {
                                            boxes[i][x].pencilmark[secondDigit - 1] = 0;
                                            boxes[i][x].numOfPencilmark--;
                                            madeProgress = true;
                                        }
                                    }
                                }

                                if (madeProgress) {
                                    madeProgress = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        return 0;
    }

    public int lockedSetTwoCells () {
        if (lockedSetTwoCellsRow() == 1) {
            return 1;
        } else if (lockedSetTwoCellsColumn() == 1) {
            return 1;
        } else {
            return lockedSetTwoCellsBox();
        }
    }
    public int lockedSetTwoCellsRow () {
        int i, j, k, x, y;
        boolean madeProgress;
        List<Cell> cellsSeenByFirstCell;
        List<Cell> cellsSeenBySecondCell;
        List<Cell> cellsSeenByBothCells;

        int []pencilmarkCount;

        for (i = 0; i < 9; i++) {
            pencilmarkCount = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
            madeProgress = false;

            for (j = 0; j < 9; j++) {
                for (k = 0; k < 9; k++) {
                    if (grid[i][j].pencilmark[k] == 1) {
                        pencilmarkCount[k]++;
                    }
                }
            }

            for (j = 0; j < 9; j++) {
                cellsSeenByFirstCell = new ArrayList<>();
                cellsSeenBySecondCell = new ArrayList<>();
                cellsSeenByBothCells = new ArrayList<>();
                if (pencilmarkCount[j] == 2) {
                    for (x = 0; x < 9; x++) {
                        if (grid[i][x].pencilmark[j] == 1) {
                            cellsSeenByFirstCell = cellsSeen(grid[i][x]);

                            for (y = x + 1; y < 9; y++) {
                                if (grid[i][y].pencilmark[j] == 1) {
                                    cellsSeenBySecondCell = cellsSeen(grid[i][y]);
                                    break;
                                }
                            }

                            break;
                        }
                    }

                    for (x = 0; x < cellsSeenByFirstCell.size(); x++) {
                        if (cellsSeenBySecondCell.contains(cellsSeenByFirstCell.get(x))) {
                            cellsSeenByBothCells.add(cellsSeenByFirstCell.get(x));
                        }
                    }

                    for (x = 0; x < cellsSeenByBothCells.size(); x++) {
                        if (cellsSeenByBothCells.get(x).pencilmark[j] == 1) {
                            cellsSeenByBothCells.get(x).pencilmark[j] = 0;
                            cellsSeenByBothCells.get(x).numOfPencilmark--;
                            madeProgress = true;
                        }
                    }

                    if (madeProgress) {
                        return 1;
                    }
                }
            }
        }

        return 0;
    }
    public int lockedSetTwoCellsColumn () {
        int i, j, k, x, y;
        boolean madeProgress;
        List<Cell> cellsSeenByFirstCell;
        List<Cell> cellsSeenBySecondCell;
        List<Cell> cellsSeenByBothCells;

        int []pencilmarkCount;

        for (i = 0; i < 9; i++) {
            pencilmarkCount = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
            madeProgress = false;

            for (j = 0; j < 9; j++) {
                for (k = 0; k < 9; k++) {
                    if (grid[j][i].pencilmark[k] == 1) {
                        pencilmarkCount[k]++;
                    }
                }
            }

            for (j = 0; j < 9; j++) {
                cellsSeenByFirstCell = new ArrayList<>();
                cellsSeenBySecondCell = new ArrayList<>();
                cellsSeenByBothCells = new ArrayList<>();
                if (pencilmarkCount[j] == 2) {
                    for (x = 0; x < 9; x++) {
                        if (grid[x][i].pencilmark[j] == 1) {
                            cellsSeenByFirstCell = cellsSeen(grid[x][i]);

                            for (y = x + 1; y < 9; y++) {
                                if (grid[x][i].pencilmark[j] == 1) {
                                    cellsSeenBySecondCell = cellsSeen(grid[y][i]);
                                    break;
                                }
                            }

                            break;
                        }
                    }

                    for (x = 0; x < cellsSeenByFirstCell.size(); x++) {
                        if (cellsSeenBySecondCell.contains(cellsSeenByFirstCell.get(x))) {
                            cellsSeenByBothCells.add(cellsSeenByFirstCell.get(x));
                        }
                    }

                    for (x = 0; x < cellsSeenByBothCells.size(); x++) {
                        if (cellsSeenByBothCells.get(x).pencilmark[j] == 1) {
                            cellsSeenByBothCells.get(x).pencilmark[j] = 0;
                            cellsSeenByBothCells.get(x).numOfPencilmark--;
                            madeProgress = true;
                        }
                    }

                    if (madeProgress) {
                        return 1;
                    }
                }
            }
        }

        return 0;
    }
    public int lockedSetTwoCellsBox () {
        int i, j, k, x, y;
        boolean madeProgress = false;
        List<Cell> cellsSeenByFirstCell;
        List<Cell> cellsSeenBySecondCell;
        List<Cell> cellsSeenByBothCells;

        int []pencilmarkCount;

        for (i = 0; i < 9; i++) {
            pencilmarkCount = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
            madeProgress = false;

            for (j = 0; j < 9; j++) {
                for (k = 0; k < 9; k++) {
                    if (boxes[i][j].pencilmark[k] == 1) {
                        pencilmarkCount[k]++;
                    }
                }
            }

            for (j = 0; j < 9; j++) {
                cellsSeenByFirstCell = new ArrayList<>();
                cellsSeenBySecondCell = new ArrayList<>();
                cellsSeenByBothCells = new ArrayList<>();
                if (pencilmarkCount[j] == 2) {
                    for (x = 0; x < 9; x++) {
                        if (boxes[i][x].pencilmark[j] == 1) {
                            cellsSeenByFirstCell = cellsSeen(boxes[i][x]);

                            for (y = x + 1; y < 9; y++) {
                                if (boxes[i][y].pencilmark[j] == 1) {
                                    cellsSeenBySecondCell = cellsSeen(boxes[i][y]);
                                    break;
                                }
                            }

                            break;
                        }
                    }

                    for (x = 0; x < cellsSeenByFirstCell.size(); x++) {
                        if (cellsSeenBySecondCell.contains(cellsSeenByFirstCell.get(x))) {
                            cellsSeenByBothCells.add(cellsSeenByFirstCell.get(x));
                        }
                    }

                    for (x = 0; x < cellsSeenByBothCells.size(); x++) {
                        if (cellsSeenByBothCells.get(x).pencilmark[j] == 1) {
                            cellsSeenByBothCells.get(x).pencilmark[j] = 0;
                            cellsSeenByBothCells.get(x).numOfPencilmark--;
                            madeProgress = true;
                        }
                    }

                    if (madeProgress) {
                        return 1;
                    }
                }
            }
        }

        return 0;
    }
}
