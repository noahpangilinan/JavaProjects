package puzzles.jam.model;

import puzzles.common.solver.Configuration;

import java.io.File;
import java.util.*;

/**
 * The class that stores the game state of a neighbor in Solver
 * Performs actions to advance the state of this configuration
 * @author Parker Noffke
 */
public class JamConfig implements Configuration {
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
     * The 2-D array representation of the board, with '.' meaning empty and a letter meaning a part of a car.
     */
    private char[][] board;


    /**
     * The constructor used to load the initial configuration to solve the puzzle
     * @param filename the name of the initial file that the config should load
     */
    public JamConfig(String filename) {
        loadBoardFromFile(new File(filename));
        displayBoard();
    }

    /**
     * The constructor used to create neighbors
     * @param previous The parent JamConfig
     * @param moveRow The row being moved to
     * @param moveCol The column being moved to
     * @param carIndex the index of the car being changed
     */
    public JamConfig(JamConfig previous, int moveRow, int moveCol, int carIndex) {
        //copies the state of the parent JamConfig
        for (Car c : previous.cars) {
            this.cars.add(new Car(c.getLabel(), c.getStart(), c.getEnd()));
        }
        this.board = new char[boardRow][boardCol];
        for(int row = 0; row < boardRow; row++) {
            for(int col = 0; col < boardCol; col++) {
                this.board[row][col] = previous.board[row][col];
            }
        }
        //makes the single car movement of this neighbor
        this.selectedCar = this.cars.get(carIndex);
        moveCar(moveRow, moveCol);
    }

    /**
     * Used when getting a hint for the GUI and PTUI.
     * Creates the initial config that will be solved.
     * @param model the JamModel, current state of the game
     */
    public JamConfig (JamModel model) {
        //copies model's board
        boardRow = model.getBoardRow();
        boardCol = model.getBoardCol();
        board = new char[boardRow][boardCol];
        for(int row = 0; row < boardRow; row++) {
            for(int col = 0; col < boardCol; col++) {
                board[row][col]= model.getBoard()[row][col];
            }
        }
        //copies model's cars
        numCars = cars.size();
        for(Car c: model.getCars()) {
            int startRow = c.getStart().getRow();
            int startCol = c.getStart().getCol();
            int endRow = c.getEnd().getRow();
            int endCol = c.getEnd().getCol();
            char carLabel =  c.getLabel();
            cars.add(new Car(carLabel, new Coordinate(startRow,startCol), new Coordinate(endRow,endCol)));
        }

    }

    /**
     *  Returns the ArrayList of Car objects that hold the game's state
     * @return the ArrayList of Car objects that hold the game's state
     */
    public ArrayList<Car> getCars() {return cars;}

    /**
     * Attempts to load a board from a given file name.
     * Used only when Jam is being run.
     * @param file The file to load
     * @return True iff loaded successfully
     */
    public void loadBoardFromFile(File file)  {
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
        }catch (Exception e) {
            System.out.println("Load failed");
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
     * Moves selectedCar to (row,col).
     * Calls updateBoard to reflect thi change
     * @param row Row to be moved to
     * @param col Column to be moved to
     */
    public void moveCar(int row, int col){
        int startRow = selectedCar.getStart().getRow();
        int startCol = selectedCar.getStart().getCol();
        int endRow = selectedCar.getEnd().getRow();
        int endCol = selectedCar.getEnd().getCol();
        //check if selection is valid, then changes Car's coordinates

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
        updateBoard(selectedCar);
    }

    /**
     * Updates the 2-D array board by taking in the car that has changed
     * then updating the array correspondingly
     * @param c the car that has changed
     */
    public void updateBoard(Car c) {
        int startRow = c.getStart().getRow();
        int startCol = c.getStart().getCol();
        int endRow = c.getEnd().getRow();
        int endCol = c.getEnd().getCol();

        if(c.isHorizontal()) { //car is horizontal
            //assign Car c's new position
            for(int i = c.getStart().getCol(); i <= c.getEnd().getCol(); i++){
                board[startRow][i] = c.getLabel();
            }

            //remove obsolete position by checking if spot has same label and is outside of the car
            for(int col = 0; col < boardCol; col++) {
                if(board[startRow][col] == c.getLabel() &&
                        (col < startCol || col > endCol)) {
                    board[startRow][col] = '.';
                }
            }
        } else { //car isnt horizontal
            //assign Car c's new position
            for(int i = c.getStart().getRow(); i <= c.getEnd().getRow(); i++){
                board[i][startCol] = c.getLabel();
            }

            //remove obsolete position by checking if spot has same label and is outside of the car
            for(int row = 0; row < boardCol; row++) {
                if(board[row][startCol] == c.getLabel() &&
                        (row < startRow || row > endRow)) {
                    board[row][startCol] = '.';
                }
            }
        }
    }

    /**
     * Displays the current state of the game by printing out
     * a representation of the board
     */
    public void displayBoard(){
        //prints the tiles
        for (int r = 0; r < boardRow; r++) {
            for (int c = 0; c < boardCol; c++) {
                System.out.print(board[r][c]);
                System.out.print(" ".charAt(0));
            }
            System.out.println();
        }
    }

    /**
     * Tests if the red car has reached the right side of the board
     * @return True iff the red car has reached the right side of the board
     */
    @Override
    public boolean isSolution() {
        if(cars.get(cars.size() - 1).getEnd().getCol() == boardCol - 1) {
            return true;
        } else {return false;}
    }

    /**
     * Gets the neighbors of the current configuration by creating
     * neighbors that each have changed by one valid movement.
     * @return the Collection of Configuration neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbors = new ArrayList<Configuration>();

        //for each car, adds a neighbor for each valid movement
        for (int carIndex = 0; carIndex < cars.size(); carIndex++) {
            selectedCar = cars.get(carIndex);
            int startRow = selectedCar.getStart().getRow();
            int startCol = selectedCar.getStart().getCol();

            if(selectedCar.isHorizontal()) {
                for(int index = 0; index < boardCol; index++) {
                    boolean result = canMove(startRow, index,
                            new Coordinate(startRow,startCol));
                    if(result) {neighbors.add(new JamConfig(this, startRow, index, carIndex));}
                }
            } else{
                for(int index = 0; index < boardRow; index++) {
                    boolean result = canMove(index, startCol,
                            new Coordinate(startRow, startCol));
                    if(result) {neighbors.add(new JamConfig(this, index, startCol, carIndex));}
                }
            }

        }
        return neighbors;
    }

    /**
     * Equals method used to compare configs and see
     * if they have the same gamestate.
     * Uses the car ArrayList for this comparison.
     * @param o The config being compared with
     * @return True iff the two Configs are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JamConfig jamConfig = (JamConfig) o;
        return cars.equals(jamConfig.cars);
    }

    /**
     * The hashcode for the current configuration of the puzzle
     * @return hashcode for the current configuration of the puzzle
     */
    @Override
    public int hashCode() {
        return Objects.hash(cars);
    }

    /**
     * Returns the String of the state of the current configuration
     * @return The String of the state of the current configuration
     */
    @Override
    public String toString() {
        return "JamConfig: Number of Cars: " + numCars + "\t" + "Cars: " + cars.toString();
    }
}
