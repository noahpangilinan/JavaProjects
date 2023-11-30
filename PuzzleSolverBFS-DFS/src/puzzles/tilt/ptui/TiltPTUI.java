package puzzles.tilt.ptui;


import javafx.scene.Scene;
import puzzles.common.Observer;
import puzzles.tilt.model.Board;
import puzzles.tilt.model.Tile;
import puzzles.tilt.model.TiltModel;

import java.io.File;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A text user interface for Lights Out
 */
public class TiltPTUI implements Observer<TiltModel, String> {
    private static char EMPTYSYMBOL = '.';
    private static char GREENSYMBOL = 'G';
    private static char BLUESYMBOL = 'B';
    private static char GOALSYMBOL = 'O';
    private static char WALLSYMBOL = 'O';
    /* Cool encodings. Fonzies only.
    private static char ONSYMBOL = '●';
    private static char OFFSYMBOL = '○';

     */
    private TiltModel model;
    private Scanner in;
    private boolean gameOn;
    static boolean argcheck;

    static String arg;


    /**
     * The Text UI for Lights Out
     */
    public TiltPTUI() {
        model = new TiltModel();
        model.addObserver(this);
        gameOn = false;
        in = new Scanner( System.in );

    }

    /**
     * Gets a filename from the user and attempts to load the file.
     * @return true iff the game was loaded successfully
     */
    public boolean loadFromFile(){
        boolean ready = false;

        while(!ready){
            System.out.println("Enter a valid file name or type Q to go back.");
            String command =  in.next();
            if (command.equals("q") || command.equals("Q")) {
                System.out.println("going back...");
                return false;
            }
            ready = model.loadBoardFromFile(new File(command));


        }
        return true;
    }

    public void loadgamefromargs(String path){
        boolean ready = false;
        ready = model.loadBoardFromFile(new File(path));
        displayBoard();

    }

    /**
     * Loads a new game or generates a random one.
     * @return True if the user starts a game; False if the user quits
     */
    public boolean gameStart(){

        boolean ready = false;
        while(!ready){
            if(argcheck){
                loadgamefromargs(arg);
                argcheck= false;
                gameOn = true;
                break;
            }
            else {
                System.out.println("(R)andom Board. (L)oad a board. (Q)uit");
                String command = in.next(); // Using next allows you to string together load commands like l boards/1.lob.
                switch (command) {
                    case "R":
                    case "r":
                        model.generateRandomBoard();
                        ready = true;
                        break;
                    case "L":
                    case "l":
                        ready = loadFromFile();
                        break;
                    case "Q":
                    case "q":
                        System.out.println("Exiting");
                        ready = true;
                        in = new Scanner(System.in);//get rid of any remaining commands from the start menu
                        return false;

                    default:
                        System.out.println("Enter R, L, or Q.");
                }
            }
            gameOn = true;
        }
        in = new Scanner(System.in);//get rid of any remaining commands from the start menu
        return true;
    }

    /**
     * iterates through the tiles and displays them
     */
    public void displayBoard(){
        //formatting for text ui is soooo elegant
        //System.out.print("\033[0;4m"); //turn on underline

        //prints the column number
        System.out.print("  ");
        for(int c =0; c<model.getDimension(); c++){
            System.out.print(c+" ");
        }
        //System.out.print("\033[0;0m"); //turn off underline
        int currentRow = -1;

        Board a = model.getBoard();

        //prints the tiles
        for(Tile t : a){
            if (currentRow!=t.getY()){ //newline for new rows.
                currentRow=t.getY();
                System.out.printf("%n%d ",currentRow);

            }
            char symbol = t.getState();

            System.out.print(symbol+" ");

        }

        System.out.printf("\n\nTotal Moves: %d\n", model.getBoard().getMoves());
        //  System.out.println();
    }

    /**
     * The main program loop. Keeps getting user input until
     */
    public void run() {
        while (true) {
            if (!gameStart()) //loads new games or quits
                break;
            gameLoop(); // gameplay
        }

    }

    /**
     * Handles the actual game play. Gets user input, toggles tiles, and asks for hints. Checks if the game is over and drops back to the main menu if it is.
     */
    private void gameLoop(){
        String msg;

        while(gameOn) {
            msg = "";
            System.out.println("Enter N S E W to tilt, (H)int, or (Q)uit to main menu");
            String command = in.nextLine().strip();
            if (command.equals("q") || command.equals("Q")) {
                System.out.println("Quitting to main menu.");
                gameOn = false;

                return;

            } else if(command.equals("h")||command.equals("H")){
                //model.getHint();

                // model.toggleTile(hint.getX(),hint.getY());

            } else {
                try {
                    Scanner s = new Scanner(command);
                    String x = s.nextLine();
                    if(x.equals("N")){
                        model.tiltUp();
                    } else if (x.equals("S")) {
                        model.tiltDown();
                    }else if (x.equals("E")) {
                        model.tiltRight();

                    }else if (x.equals("W")) {
                        model.tiltLeft();
                    }
                    displayBoard();
                    if (model.won()){
                        System.out.println("You Win!");
                        gameOn = false;

                    }

                } catch (InputMismatchException e) {

                    msg = "X and Y must be integers";
                } catch (NoSuchElementException e) {

                    msg = "Must enter X and Y on one line.";
                } catch (IndexOutOfBoundsException e) {
                    msg = String.format("X and Y should be between 0 and %d", model.getDimension());
                }
            }


            if (!msg.isEmpty())

                System.out.println("Command: "+command+"\n\033[0;1m***"+msg+"***\033[0;0m");

        }
    }

    /**
     * Runs the Text UI for Lights Outs
     * @param args cmd line args
     */
    public static void main(String[] args){
        boolean x = false;
        if(args.length == 1){
            x =true;
            argcheck = true;
            arg = args[0];

        }
        TiltPTUI ui = new TiltPTUI();
        ui.run();

    }

    @Override
    public void update(TiltModel model, String msg) {
        if (msg.equals(TiltModel.LOADED)){ // game is loaded successfully
            System.out.println("Game Loaded");
            displayBoard();
            return;
        }else if (msg.equals(TiltModel.LOAD_FAILED)){ //Game failed to load
            System.out.println("Error Loading Game");
            return;
        } else if (msg.startsWith(TiltModel.HINT_PREFIX)) { //Model is reporting a  hint
            System.out.println(msg);
            //don't display board
            return;
        }

        if (model.gameOver()) { //checks if game is over.
            displayBoard();
      /* Cool encodings renderable only on cool systems.
            System.out.print("\033[0;4m"); //turn on underline
            System.out.print("\033[5m");
            System.out.println("You win. Good for you.");
            System.out.print("\033[0;0m");
         //   System.out.print("\033[0;4m"); //turn on underline

       */

            System.out.println("You win. Good for you.");
            gameOn = false;
            return;
        }
        displayBoard(); // renders the board
        System.out.println(msg);
    }


}
