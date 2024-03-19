import java.util.ArrayList;
import java.util.List;

public class Cell {
    int digit;
    // 0 means there is a pencil mark of that digit in this cell
    // 1 means there is no pencil mark of that digit in this cell
    int []pencilmark = new int[9];
    int numOfPencilmark;
    int row;
    int column;

    public int getBoxNumber () {
        int boxNumber;

        if (this.row < 3) {
            boxNumber = 2;
        } else if (this.row < 6) {
            boxNumber = 5;
        } else {
            boxNumber = 8;
        }

        if (this.column < 3) {
            boxNumber -= 2;
        } else if (this.column < 6) {
            boxNumber--;
        }

        return boxNumber;
    }

    public int indexInBox () {
        int index;

        if ((this.row + 1) % 3 == 1) {
            index = 0;
        } else if ((this.row + 1) % 3 == 2) {
            index = 3;
        } else {
            index = 6;
        }

        if ((this.column + 1) % 3 == 2) {
            index++;
        } else if ((this.column + 1) % 3 == 0){
            index += 2;
        }

        return index;
    }
}
