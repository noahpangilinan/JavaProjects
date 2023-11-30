package magnets;

import backtracking.Configuration;
import test.IMagnetTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * The representation of a magnet configuration, including the ability
 * to backtrack and also give information to the JUnit tester.
 *
 * This implements a more optimal pruning strategy in isValid():
 * - Pair checked each time a new cell is populated
 * - Polarity checked each time a new cell is populated
 * - When last column or row is populated, the pos/neg counts are checked
 *
 * @author RIT CS
 */
public class MagnetsConfig implements Configuration, IMagnetTest {
    /** a cell that has not been assigned a value yet */
    private final static char EMPTY = '.';
    /** a blank cell */
    private final static char BLANK = 'X';
    /** a positive cell */
    private final static char POS = '+';
    /** a negative cell */
    private final static char NEG = '-';
    /** left pair value */
    private final static char LEFT = 'L';
    /** right pair value */
    private final static char RIGHT = 'R';
    /** top pair value */
    private final static char TOP = 'T';
    /** bottom pair value */
    private final static char BOTTOM = 'B';
    /** and ignored count for pos/neg row/col */
    private final static int IGNORED = -1;

    static int numrows;
    static int numcols;

    static int[] left;
    static int[] top;
    static int[] right;
    static int[] bottom;

    int cursorrow;
    int cursorcol;

    static char[][] pairings;
    private char[][] board;






    // TODO
    // add private state here

    /**
     * Read in the magnet puzzle from the filename.  After reading in, it should display:
     * - the filename
     * - the number of rows and columns
     * - the grid of pairs
     * - the initial config with all empty cells
     *
     * @param filename the name of the file
     * @throws IOException thrown if there is a problem opening or reading the file
     */
    public MagnetsConfig(String filename) throws IOException {
        System.out.println("File: " + filename);
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            // read first line: rows cols
            String[] fields = in.readLine().split("\\s+");
            numrows = Integer.parseInt(fields[0]);
            numcols = Integer.parseInt(fields[1]);
            System.out.println("Rows: " + numrows + ", Cols: " + numcols);
            String[] countersleft = in.readLine().split("\\s+");
            String[] counterstop = in.readLine().split("\\s+");
            String[] countersright = in.readLine().split("\\s+");
            String[] countersbottom = in.readLine().split("\\s+");
            left = new int[numrows];
            top = new int[numcols];
            right = new int[numrows];
            bottom = new int[numcols];
            pairings = new char[numrows][numcols];
            board = new char[numrows][numcols];
            this.cursorcol = -1;
            this.cursorrow = 0;

            for (int i = 0; i < numrows; i++) {
                String[] pairs = in.readLine().split("\\s+");
                left[i] = Integer.parseInt(countersleft[i]);
                right[i] = Integer.parseInt(countersright[i]);
                for (int j = 0; j < numcols; j++) {
                    pairings[i][j] = pairs[j].charAt(0);
                    board[i][j] = '.';
                }


            }
            System.out.println("Pairs:");
            for (int i = 0; i < numrows; i++) {
                for (int j = 0; j < numcols; j++) {
                    System.out.print(pairings[i][j] + " ");

                }
                System.out.println();

            }

            for (int j = 0; j < numcols; j++) {
                top[j] = Integer.parseInt(counterstop[j]);
                bottom[j] = Integer.parseInt(countersbottom[j]);
            }


            System.out.println("Initial Config:");
            System.out.println(this);





            // TODO: Finish implementing the constructor

        } // <3 Jim
    }

    /**
     * The copy constructor which advances the cursor, creates a new grid,
     * and populates the grid at the cursor location with val
     * @param other the board to copy
     * @param val the value to store at new cursor location
     */
    private MagnetsConfig(MagnetsConfig other, char val) {
        char[][] temp = new char[numrows][numcols];

        for (int i = 0; i < numrows; i++) {
            for (int j = 0; j < numcols; j++) {
                temp[i][j] = other.getVal(i,j);
            }
        }
        this.board = temp;

        this.cursorrow = other.getCursorRow();
        this.cursorcol = other.getCursorCol() + 1;
        if (this.cursorcol >= numcols && numrows > this.cursorrow) {
            this.cursorrow++;
            this.cursorcol = 0;
        }

        this.board[this.cursorrow][this.cursorcol] = val;



    }


    /**
     * Generate the successor configs.  For minimal pruning, this should be
     * done in the order: +, - and X.
     *
     * @return the collection of successors
     */
    @Override
    public List<Configuration> getSuccessors() {
        List<Configuration> successors = new ArrayList<>();
        MagnetsConfig temppos = new MagnetsConfig(this, '+');
        successors.add(temppos);

        MagnetsConfig tempneg = new MagnetsConfig(this, '-');
        successors.add(tempneg);

        MagnetsConfig tempblank = new MagnetsConfig(this, 'X');
        successors.add(tempblank);

        return successors;
    }


    /**
     * Checks to make sure a successor is valid or not.  For minimal pruning,
     * each newly placed cell at the cursor needs to make sure its pair
     * is valid, and there is no polarity violation.  When the last cell is
     * populated, all row/col pos/negative counts are checked.
     *
     * @return whether this config is valid or not
     */
    @Override
    public boolean isValid() {
        if(cursorrow-1 >= 0){
            if (getVal(cursorrow,cursorcol) != 'X' && getVal(cursorrow,cursorcol) == getVal(cursorrow-1,cursorcol)){
                return false;
            }
        }
        if(cursorrow+1 < numcols-1){
            if (getVal(cursorrow,cursorcol) != 'X' && getVal(cursorrow,cursorcol) == getVal(cursorrow+1,cursorcol)){
                return false;
            }
        }
        if(cursorcol-1 >= 0){
            if (getVal(cursorrow,cursorcol) != 'X' && getVal(cursorrow,cursorcol) == getVal(cursorrow,cursorcol-1)){
                return false;
            }
        }
        if(cursorcol+1 < numcols){
            if (getVal(cursorrow,cursorcol) != 'X' && getVal(cursorrow,cursorcol) == getVal(cursorrow,cursorcol+1)){
                return false;
            }
        }




        if(pairings[cursorrow][cursorcol] == BOTTOM){
            if(getVal(cursorrow,cursorcol) == 'X'){
                if(getVal(cursorrow-1, cursorcol) != 'X'){
                    return false;
                }
            }
            if(getVal(cursorrow,cursorcol) == '+'){
                if(getVal(cursorrow-1, cursorcol) != '-'){
                    return false;
                }
            }
            if(getVal(cursorrow,cursorcol) == '-'){
                if(getVal(cursorrow-1, cursorcol) != '+'){
                    return false;
                }
            }
        }


        if(pairings[cursorrow][cursorcol] == RIGHT){
            if(getVal(cursorrow,cursorcol) == 'X'){
                if(getVal(cursorrow, cursorcol-1) != 'X'){
                    return false;
                }
            }
            if(getVal(cursorrow,cursorcol) == '+'){
                if(getVal(cursorrow, cursorcol-1) != '-'){
                    return false;
                }
            }
            if(getVal(cursorrow,cursorcol) == '-'){
                if(getVal(cursorrow, cursorcol-1) != '+'){
                    return false;
                }
            }
        }
        int k = -1;
        for (int i: top) {
            boolean trying = false;
            k++;
            if(i != -1){
                int counter = 0;
                for (int j = 0; j < numrows; j++) {
                    if(getVal(j, k) == '+'){
                        counter++;
                    }
                    if(getVal(j, k) == '.'){
                        trying = true;
                    }
                }
                if(counter > i){
                    return false;

                }
                if (counter != i){

                    if(!trying){
                        return false;
                    }
                }
            }
        }
        k = -1;

        for (int i: left) {
            boolean trying = false;

            k++;
            if(i != -1){
                int counter = 0;
                for (int j = 0; j < numcols; j++) {
                    if(getVal(k, j) == '+'){
                        counter++;
                    }
                    if(getVal(k, j) == '.'){
                        trying = true;
                    }

                }
                if(counter > i){
                        return false;

                }
                if (counter != i){
                    if(!trying){
                        return false;
                    }
                }
            }
        }
        k = -1;

        for (int i: bottom) {
            boolean trying = false;

            k++;
            if(i != -1){
                int counter = 0;
                for (int j = 0; j < numrows; j++) {
                    if(getVal(j, k) == '-'){
                        counter++;
                    }
                    if(getVal(j, k) == '.'){
                        trying = true;
                    }

                }
                if(counter > i){
                    return false;

                }
                if (counter != i){
                    if(!trying){
                        return false;
                    }
                }
            }
        }
        k = -1;

        for (int i: right) {
            boolean trying = false;

            k++;
            if(i != -1){
                int counter = 0;
                for (int j = 0; j < numcols; j++) {
                    if(getVal(k, j) == '-'){
                        counter++;
                    }
                    if(getVal(k, j) == '.'){
                        trying = true;
                    }

                }
                if(counter > i){
                    return false;

                }
                if (counter != i){
                    if(!trying){
                        return false;
                    }
                }
            }
        }







        return true;
    }

    @Override
    public boolean isGoal() {
        if(this.cursorrow == numrows-1 && this.cursorcol == numcols-1){return true;}
        return false;
    }

    /**
     * Returns a string representation of the puzzle including all necessary info.
     *
     * @return the string
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        // top row
        result.append("+ ");
        for (int col = 0; col < getCols(); ++col) {
            result.append(getPosColCount(col) != IGNORED ? getPosColCount(col) : " ");
            if (col < getCols() - 1) {
                result.append(" ");
            }
        }
        result.append(System.lineSeparator());
        result.append("  ");
        for (int col = 0; col < getCols(); ++col) {
            if (col != getCols() - 1) {
                result.append("--");
            } else {
                result.append("-");
            }
        }
        result.append(System.lineSeparator());

        // middle rows
        for (int row = 0; row < getRows(); ++row) {
            result.append(getPosRowCount(row) != IGNORED ? getPosRowCount(row) : " ").append("|");
            for (int col = 0; col < getCols(); ++col) {
                result.append(getVal(row, col));
                if (col < getCols() - 1) {
                    result.append(" ");
                }
            }
            result.append("|").append(getNegRowCount(row) != IGNORED ? getNegRowCount(row) : " ");
            result.append(System.lineSeparator());
        }

        // bottom row
        result.append("  ");
        for (int col = 0; col < getCols(); ++col) {
            if (col != getCols() - 1) {
                result.append("--");
            } else {
                result.append("-");
            }
        }
        result.append(System.lineSeparator());

        result.append("  ");
        for (int col = 0; col < getCols(); ++col) {
            result.append(getNegColCount(col) != IGNORED ? getNegColCount(col) : " ").append(" ");
        }
        result.append(" -").append(System.lineSeparator());
        return result.toString();
    }

    // IMagnetTest

    @Override
    public int getRows() {
        // TODO
        return numrows;
    }

    @Override
    public int getCols() {
        // TODO
        return numcols;
    }

    @Override
    public int getPosRowCount(int row) {
        // TODO
        return left[row];
    }

    @Override
    public int getPosColCount(int col) {
        // TODO
        return top[col];
    }

    @Override
    public int getNegRowCount(int row) {
        // TODO
        return right[row];
    }

    @Override
    public int getNegColCount(int col) {
        // TODO
        return bottom[col];
    }

    @Override
    public char getPair(int row, int col) {
        // TODO
        return pairings[row][col];
    }

    @Override
    public char getVal(int row, int col) {
        // TODO
        return board[row][col];
    }

    @Override
    public int getCursorRow() {
        // TODO
        return cursorrow;
    }

    @Override
    public int getCursorCol() {
        // TODO
        return cursorcol;
    }
}
