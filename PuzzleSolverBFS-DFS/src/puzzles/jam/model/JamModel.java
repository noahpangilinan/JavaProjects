package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.File;
import java.util.*;

/**
 * Holds the state of the game and performs actions to advance the game.
 * @author Parker Noffke
 */
public class JamModel {
    /** the collection of observers of this model */
    private final List<Observer<JamModel, String>> observers = new LinkedList<>();
    /**
     * Message sent when a board has successfully loaded.
     */
    public static String LOADED = "loaded";
    /**
     * Message sent when a board has failed to load.
     */
    public static String LOAD_FAILED = "loadFailed";
    /**
     * The message that will precede a hint.
     */
    public static String HINT_PREFIX = "Next Step!";
    /**
     * The message that will inform of a puzzle reset.
     */
    public static final String RESET = "Puzzle reset!";
    /**
     * The message that will inform that a car is selected.
     */
    public static final String SELECTED = "Selected";
    /**
     * The String name of the current file that the puzzle has loaded
     */
    public static String currentFile;
    /**
     * The number of cars in the puzzle
     */
    private int numCars;
    /**
     * An ArrayList of Car objects which hold the data of each car
     */
    private ArrayList<Car> cars = new ArrayList<>();
    /**
     * The number of rows of the board
     */
    static int boardRow;
    /**
     * The number of columns of the board
     */
    static int boardCol;
    /**
     * The currently selected car
     */
    private Car selectedCar;
    /**
     * The 2-D array representation of the board, with '.' meaning empty and a letter meaning a specific car.
     */
    private char[][] board;
    /**
     * True if the board is supposed to reset when loading a file, false if it isn't
     */
    public static boolean reset;


    /**
     * Creates a new board (all tiles are off) , initializes observers list
     */
    public JamModel(String filename) {
        currentFile = filename;
        reset = false;
    }


    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<JamModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     *Gets the Board of the current JamConfig.
     * @return the Board of the current JamConfig.
     */
    public char[][] getBoard(){
        return board;
    }

    /**
     * Returns the String of the current file that the puzzle has loaded
     * @return the String of the current file that the puzzle has loaded
     */
    public String getCurrentFile(){
        return currentFile;
    }

    /**
     *Gets the number of columns of the board.
     * @return the number of columns of the board.
     */
    public int getBoardCol(){
        return boardCol;
    }

    /**
     *Gets the number of rows of the board.
     * @return the number of rows of the board.
     */
    public int getBoardRow(){
        return boardRow;
    }

    /**
     *  Returns the ArrayList of Car objects that hold the game's state
     * @return the ArrayList of Car objects that hold the game's state
     */
    public ArrayList<Car> getCars(){
        return cars;
    }


    /**
     * Attempts to load a board from a given file name. It will announce to the observers if it was loaded successfully or not.
     * @param filename The file to load
     * @return True iff loaded successfully
     */
    public boolean loadBoardFromFile(String filename) {return loadBoardFromFile(new File(filename), filename);
    }

    /**
     * Attempts to load a board from a file object. It will announce to the observers if it was loaded successfully or not.
     * @param file The file to load
     * @return True iff loaded successfully
     */
    public boolean loadBoardFromFile(File file, String filename)  {
        try {
            Scanner in = new Scanner(file);
            String line = in.nextLine();
            String[] fields = line.split(" ");
            boardRow = Integer.parseInt(fields[0]);
            boardCol =Integer.parseInt(fields[1]);
            numCars = Integer.parseInt(in.nextLine());
            cars.clear();
            //sets board to empty
            board = new char[boardRow][boardCol];
            for (int r = 0; r < boardRow; r++) {
                for (int c = 0; c < boardCol; c++) {
                    board[r][c] = '.';
                }
            }
            //reads in cars
            while ((in.hasNext())) {
                line = in.nextLine();
                String[] carFields = line.split(" ");
                Coordinate start = new Coordinate(Integer.parseInt(carFields[1]), Integer.parseInt(carFields[2]));
                Coordinate end = new Coordinate(Integer.parseInt(carFields[3]), Integer.parseInt(carFields[4]));
                Car car = new Car(carFields[0].charAt(0), start, end);
                cars.add(car);
                updateBoard(car);
            }
            if(reset) {
                reset = false;
                announce(RESET);
            } else {
                currentFile = filename;
                announce(LOADED);
            }
            return true;
        }catch (Exception e) {
            announce(LOAD_FAILED);
            return false; //invalid file
        }
    }

    /**
     * The method used to select a car. Returns true if it successfully selected a car.
     * Announces whether a car was selected successfully or not
     * @param row the row that is being selected
     * @param col the column that is being selected
     * @return True iff successfully selected a car
     */
    public boolean selectCar(int row, int col){
        for (Car car : cars) {
            //check if selection is on a car
            if(car.getStart().getRow() <= row && row <= car.getEnd().getRow()
                    && car.getStart().getCol() <= col && col <= car.getEnd().getCol()) {
                selectedCar = car;
                announce("Selected: (" + row + ", " + col + ")");
                return true;
            }
        }
        //car not found
        announce("No car at (" + row +", " + col + ")");
        return false;
    }


    /**
     * Moves selectedCar to (row,col). Announces whether the car moved successfully or not.
     * @param row Row to be moved to
     * @param col Column to be moved to
     */
    public void moveCar(int row, int col, Coordinate selected){
        boolean result = true;
        //check if selection is valid, then changes Car's coordinates
        int startRow = selectedCar.getStart().getRow();
        int startCol = selectedCar.getStart().getCol();
        int endRow = selectedCar.getEnd().getRow();
        int endCol = selectedCar.getEnd().getCol();

        //makes sure move doesn't cross over cars or is touching a car
        boolean validMove = canMove(row, col, selected);

        if(validMove) {
            if (selectedCar.isHorizontal()) {
                if (col < startCol) { // selection is valid, so checks if car is moving left
                //sets temp variable to know how far car moves, then changes car's coordinates correspondingly
                int move = startCol - col;
                selectedCar.getStart().changeCol(col);
                selectedCar.getEnd().changeCol(endCol - move);
                } else if (col > endCol) { //checks if car is moving right
                    //sets temp variable to know how far car moves, then changes car's coordinates correspondingly
                    int move = col - endCol;
                    selectedCar.getStart().changeCol(startCol + move);
                    selectedCar.getEnd().changeCol(col);
                }
            } else {
                if (row < startRow) { // selection is valid, so checks if car is moving up
                //sets temp variable to know how far car moves, then changes car's coordinates correspondingly
                int move = startRow - row;
                selectedCar.getStart().changeRow(row);
                selectedCar.getEnd().changeRow(endRow - move);
                } else if (row > endRow) { //checks if car is moving down
                    //sets temp variable to know how far car moves, then changes car's coordinates correspondingly
                    int move = row - endRow;
                    selectedCar.getStart().changeRow(startRow + move);
                    selectedCar.getEnd().changeRow(row);
                }
            }
        } else {result = false;}
        updateBoard(selectedCar);
        if(result){
            announce("Moved: (" + selected.getRow() + ", " + selected.getCol()
                    + ")  to (" + row + ", " + col + ")");
        } else {
            announce("Can't move from (" + selected.getRow() + ", " + selected.getCol()
                    + ")  to (" + row + ", " + col + ")");
        }
    }


    /**
     *  Method that checks if selectedCar can move to the specified spot.
     * @param row The row that the car wants to move to
     * @param col The column that the car wants to move to
     * @param selected the selected coordinate of (row,col)
     * @return true iff the car can move to the specified spot
     */
    public boolean canMove(int row, int col, Coordinate selected) {
        boolean result = true;
        //check if selection is valid, then changes Car's coordinates
        int startRow = selectedCar.getStart().getRow();
        int startCol = selectedCar.getStart().getCol();
        int endRow = selectedCar.getEnd().getRow();
        int endCol = selectedCar.getEnd().getCol();
        //make sure selected space to move to isn't a car
        if (selectedCar.isHorizontal()) {
            //car is horizontal, checks if selection is in correct row
            if (startRow != row) {
                result = false;
            } else if (!(0 <= col && col < boardCol)) { //checks that the specified column is within range
                result = false;
            } else if(col > selected.getCol()) {
                for(int index = selected.getCol(); index <= col; index++) {
                    String selectedLabel = Character.toString(board[startRow][index]);
                    //checks if index is valid (that is a dot, and that it is not the same label as selectedCar or the desired spot is inside the car
                    if(!(selectedLabel.equals(".")) && (!selectedLabel.equals(Character.toString(selectedCar.getLabel())) || (col <= endCol))) {
                        result = false;
                        break;
                    }
                }
            } else {
                for(int index = col; index <= selected.getCol(); index++) {
                    String selectedLabel = Character.toString(board[startRow][index]);
                    //checks if index is valid (that is a dot, and that it is not the same label as selectedCar or the desired spot is inside the car
                    if(!(selectedLabel.equals(".")) && (!selectedLabel.equals(Character.toString(selectedCar.getLabel())) || (col >= startCol))) {
                        result = false;
                        break;
                    }
                }
            }
        } else {
            //car isnt horizontal, checks if selection is in correct column
            if (startCol != col) {
                result = false;
            } else if (!(0 <= row && row < boardRow)) { //checks that the specified row is within range
                result = false;
            } else if(row > selected.getRow()) {
                for(int index = selected.getRow(); index <= row; index++) {
                    String selectedLabel = Character.toString(board[index][startCol]);
                    //checks if index is valid (that is a dot, and that it is not the same label as selectedCar or the index is inside the car
                    if(!(selectedLabel.equals(".")) && (!selectedLabel.equals(Character.toString(selectedCar.getLabel())) || (row <= endRow))) {
                        result = false;
                        break;
                    }
                }
            } else {
                for(int index = row; index <= selected.getRow(); index++) {
                    String selectedLabel = Character.toString(board[index][startCol]);
                    //checks if index is valid (that is a dot, and that it is not the same label as selectedCar or the index is inside the car
                    if(!(selectedLabel.equals(".")) && (!selectedLabel.equals(Character.toString(selectedCar.getLabel())) || (row >= startRow))) {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }


    /**
     * Updates the 2-D array board by taking in the car that has changed
     * then updating the array correspondingly
     * @param c the car that has changed
     */
    public void updateBoard(Car c) {
        if(c.isHorizontal()) { //car is horizontal
            int row = c.getStart().getRow();
            //assign Car c's new position
            for(int i = c.getStart().getCol(); i <= c.getEnd().getCol(); i++){
                board[row][i] = c.getLabel();
            }
            //remove obsolete position by checking if spot has same label and is outside of the car
            for(int col = 0; col < boardCol; col++) {
                if(board[row][col] == c.getLabel() &&
                        (col < c.getStart().getCol() || col > c.getEnd().getCol())) {
                    board[row][col] = '.';
                }
            }
        } else { //car isnt horizontal
            int col = c.getStart().getCol();
            //assign Car c's new position
            for(int i = c.getStart().getRow(); i <= c.getEnd().getRow(); i++){
                board[i][col] = c.getLabel();
            }
            //remove obsolete position by checking if spot has same label and is outside of the car
            for(int row = 0; row < boardRow; row++) {
                if(board[row][col] == c.getLabel() &&
                        (row < c.getStart().getRow() || row > c.getEnd().getRow())) {
                    board[row][col] = '.';
                }
            }
        }
    }

    /**
     * Performs a hint to the user. This requires solving the entire puzzle,
     * it may take a long time or even run out of memory.
     * Announces whether the board advanced to the next step, reached a solution
     * already, or there is no solution
     */
    public void getHint(){
        try {
            List<Configuration> sPath = Solver.solve(new JamConfig(this));
            Configuration next = sPath.get(1);
            if (next instanceof JamConfig) {
                JamConfig nextConfig = (JamConfig) next;
                for (int carIndex = 0; carIndex < numCars; carIndex++) {
                    if (!(nextConfig.getCars().get(carIndex).equals(this.cars.get(carIndex)))) {
                        this.cars.remove(this.cars.get(carIndex));
                        this.cars.add(carIndex, nextConfig.getCars().get(carIndex));
                        updateBoard(this.cars.get(carIndex));
                    }
                }
            }
            announce(HINT_PREFIX);
        } catch (Exception reachedSolution) {
            if(cars.get(numCars - 1).getEnd().getCol() == boardCol - 1) {
                announce("Reached Solution Already!");
            } else {
                announce("No Solution!");
            }
        }
    }

    /**
     * Announce to observers the model has changed;
     */
    private void announce( String arg ) {
        for ( var obs : this.observers ) {
            obs.update( this, arg );
        }
    }

}
