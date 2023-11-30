package puzzles.jam.ptui;

import puzzles.common.Observer;
import puzzles.jam.model.Coordinate;
import puzzles.jam.model.JamModel;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The class that contains and carries out the PTUI version of the JAM puzzle
 * @author Parker Noffke
 */
public class JamPTUI implements Observer<JamModel, String> {
    /**
     * The model of the game, used for various actions to progress the game
     */
    private JamModel model;
    /**
     * The scanner that is used to load new files
     */
    private Scanner in;
    /**
     * Boolean of whether the game is on or not
     */
    private boolean gameOn;




    /**
     * The constructor for JamPTUI that initializes the
     * program by loading a model, etc.
     */
    public JamPTUI(String filename) {
        model = new JamModel(filename);
        model.addObserver(this);
        gameOn = false;
        in = new Scanner( System.in );
    }

    /**
     * Used to update the display of the game depending on the message announced
     * @param jamModel the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     */
    @Override
    public void update(JamModel jamModel, String message) {
        if (message.equals(JamModel.LOADED)){ // game is loaded successfully
            System.out.println("Loaded: " + model.getCurrentFile());
            displayBoard();
            return;
        }else if (message.equals(JamModel.LOAD_FAILED)){ //Game failed to load
            System.out.println("Error Loading Game");
            return;
        } else if (message.startsWith(JamModel.HINT_PREFIX)) { //Model is reporting a  hint
            displayBoard();
            System.out.println(message);
            return;
        }
        System.out.println(message);
        displayBoard();
        }

    /**
     * Loads a new game.
     * @return True if the user starts a game
     */
    public boolean gameStart(){
        boolean ready = false;
        while(!ready) {
            ready = model.loadBoardFromFile(model.getCurrentFile());
            in = new Scanner(System.in);//get rid of any remaining commands from the start menu
        }
        gameOn = true;
        return true;
    }

    /**
     * Iterates through the tiles and displays them
     */
    public void displayBoard(){
        int boardCol = model.getBoardCol();
        int boardRow = model.getBoardRow();

        //prints the column number
        System.out.print("  ");
        for(int c =0; c<boardCol; c++){
            System.out.print(c+" ");
        }
        System.out.println();
        System.out.print("  ");
        for (int i = 0; i < 2 * boardCol; i++) {
            System.out.print("-");
        }
        //prints the tiles
        for (int r = 0; r < boardRow; r++) {
            System.out.printf("%n%d ", r,"| ");
            for (int c = 0; c < boardCol; c++) {
                System.out.print(model.getBoard()[r][c]);
                System.out.print(" ".charAt(0));
            }
        }
        System.out.println();
    }


    /**
     * The main program loop. Keeps getting user input until
     */
    public void run() {
        gameStart(); //loads new games
        gameLoop(); // gameplay
    }

    /**
     * Handles the actual game play. Gets user input, toggles tiles, and asks for hints.
     * Checks if the game is over and drops back to the main menu if it is.
     */
    private void gameLoop(){
        String msg;

        while(gameOn) {
            msg = "";
            System.out.println("h(int)              -- hint next move");
            System.out.println("l(oad) filename     -- load new puzzle file");
            System.out.println("s(elect) r c        -- select cell at r, c");
            System.out.println("q(uit)              -- quit the game");
            System.out.println("r(eset)             -- reset the current game");

            String command = in.nextLine().strip();
            if (command.equals("q") || command.equals("Q")) {
                System.out.println("Quitting to main menu.");
                gameOn = false;
                return;
            } else if(command.equals("h")||command.equals("H")){
                model.getHint();
            } else if(command.startsWith("l")||command.startsWith("L")) {
                String[] fields = command.split(" ");
                model.loadBoardFromFile(fields[1]);
            } else if(command.equals("r")||command.equals("R")) {
                model.reset = true;
                model.loadBoardFromFile(model.getCurrentFile());
            } else if(command.startsWith("s")||command.startsWith("S")) {
                try {
                    //select car
                    String[] fields = command.split(" ");
                    int row = Integer.parseInt(fields[1]);
                    int col = Integer.parseInt(fields[2]);
                    if(model.selectCar(row, col)) {
                        //move car
                        String moveCmd = in.nextLine().strip();
                        fields = moveCmd.split(" ");
                        int moveRow = Integer.parseInt(fields[1]);
                        int moveCol = Integer.parseInt(fields[2]);
                        if(fields[0].equals("s") || fields[0].equals("S")) {
                            model.moveCar(moveRow, moveCol, new Coordinate(col, row));
                        }
                    }
                } catch (InputMismatchException e) {
                    msg = "Row and Column must be integers";
                } catch (NoSuchElementException e) {
                    msg = "Must enter Row and Column on one line.";
                } catch (IndexOutOfBoundsException e) {
                    msg = String.format("Row and Column should be between 0 and (" + model.getBoardRow() + "," + model.getBoardCol() + "), respectively.");
                }
            }


            if (!msg.isEmpty())
                System.out.println("Command: "+command+"\n\033[0;1m***"+msg+"***\033[0;0m");

        }
    }

    /**
     * Runs the Text UI for Jam
     * @param args filename for initial puzzle
     */
    public static void main(String[] args){
        String filename = args[0];
        JamPTUI ui = new JamPTUI(filename);
        ui.run();
    }

}
