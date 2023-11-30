package puzzles.tilt.gui;

/* imports! */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import java.io.File;
import java.util.Scanner;

public class TiltGUI extends Application implements Observer<TiltModel, String> {


    static int N = 5;
    /* Default dim size */
    static boolean argcheck;
    /* checks for argument */


    GridPane gridpane;
    /* gridpane for the gui */
    static Button[][] buttons;
    /* array of buttons for gui/grid*/
    Label movelabel;
    /* the label that displays number of moves*/
    Label gameMessage;
    /*displays win or error messages*/

    Button tup;
    /*tilt up control*/
    Button tdown;
    /*down tilt control*/
    Button tleft;
    /*left tilt control*/
    Button tright;
    /*tilt right control*/

    static String arg;
    /* the argument being passed in*/


    Stage stage;
    /* main stage for the gui*/
    Scene scene;
    /* main scene for the gui*/

    String buttonmsg = "";
    /* displays a message on the button*/

    final static String EMPTY = "-fx-background-color: #ffffff; -fx-border-color: #ffffff; -fx-border-width: 1px;";
    final static String BLUE = "-fx-background-color: #0000FF; -fx-border-color: #ffffff; -fx-border-width: 1px;";
    final static String GREY = "-fx-background-color: #D3D3D3; -fx-border-color: #ffffff; -fx-border-width: 1px;";
    final static String GREEN = "-fx-background-color: #00FF00; -fx-border-color: #ffffff; -fx-border-width: 1px;";
    final static String GOAL = "-fx-background-color: #000000; -fx-border-color: #ffffff; -fx-border-width: 1px;";
    /* list of color resources for the grid*/

    TiltModel model;
    boolean gameOn;
    Scanner in;

    public TiltGUI() {
        /*
        * Main GUI constructor
        *
        * makes a model and scanner
        *
        * */
        model = new TiltModel();
        model.addObserver(this);
        gameOn = false;
        in = new Scanner( System.in );
    }



    public boolean gameStart(){
        /* runs the game loop*/

        boolean ready = false;
        while(!ready){
            gameOn = true;
            ready = true;
        }
        return true;
    }






    public void displayBoard(){
        /* displays the board based on the model,
        * runs through the moddle and sets color based on the value of the tile
        *
        * */



        for (int j = 0; j < N; j++) {
            System.out.println();
            this.movelabel.setText("Moves: " + model.getBoard().getMoves());
            for (int i = 0; i < N; i++) {
                System.out.print(model.getTile(i, j) + " ");
                if(model.getTile(i,j).getState() == '.'){
                    buttons[i][j].setStyle(EMPTY);
                }
                else if (model.getTile(i,j).getState() == 'B'){
                    buttons[i][j].setStyle(BLUE);
                }
                else if (model.getTile(i,j).getState() == 'G'){
                    buttons[i][j].setStyle(GREEN);
                }
                else if (model.getTile(i,j).getState() == '*'){
                    buttons[i][j].setStyle(GREY);
                }
                else if (model.getTile(i,j).getState() == 'O'){
                    buttons[i][j].setStyle(GOAL);
                }

            }

        }

    }


    public void getHint(){
        /*
        * calls the models getHint()
        *
        * highlights the correct direction
        * */
        String dir = model.getHint();
        if(dir.equals("u")){
            tup.setStyle(GREEN);
        }
        if(dir.equals("d")){
            tdown.setStyle(GREEN);
        }
        if(dir.equals("l")){
            tleft.setStyle(GREEN);
        }
        if(dir.equals("r")){
            tright.setStyle(GREEN);
        }

    }





    public void run() {
        /*
        * Is called from the main and runs the game loop
        * */
        while (true) {
            if (!gameStart()) //loads new games or quits
                break;
            gameLoop(); // gameplay
        }

    }






    private void gameLoop() {
        /*
        *checks for quitting out and quits or continues the game
        * */


        while (gameOn) {
            String command = this.buttonmsg;
            if (command.equals("q") || command.equals("Q")) {
                System.out.println("Quitting to main menu.");
                gameOn = false;

                return;

            }

        }
    }



    @Override
    public void init() throws Exception {
        /*
        * runs at start and prints the init
        * */
        System.out.println("init: Initialize and connect to model!");
    }
    @Override
    public void start(Stage stage) throws Exception {

        /*
        *Runs on start up and creates the stage, gridpane and scene
        *
        * sets the title and checks for arguments
        *
        * shows the scene
        * */
        //create a new FileChooser


        this.stage = stage;
        this.gridpane = makeGridPane();
        this.scene = new Scene(this.gridpane);
        stage.setScene(this.scene);
        stage.setTitle("TILT");
        stage.show();
        if(argcheck){
            loadgamefromargs(arg);
        }

    }



    public void loadgame(){
        /*
        runs models loadgame,
        opens a filechooser and loads file
        makes a new gridpane with loaded model
        displays new board
         */
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load a game board.");
//open up a window for the user to interact with.
        File selectedFile = fileChooser.showOpenDialog(this.stage);
        fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Text Files", "*.lob"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/boards"));
        if(model.loadBoardFromFile(selectedFile)){
            this.gameMessage.setText("Game Loaded");

        }
        else{
            this.gameMessage.setText("Load Error");

        }
        gameStart();
        this.movelabel.setText("Moves: " + 0);
        this.N = model.getDimension();
        this.gridpane = makeGridPane();

        this.scene = new Scene(this.gridpane);
        stage.setScene(scene);
        displayBoard();

    }


    public void loadgamefromargs(String path){

        /* does the same thing as loadgame(), but with an argument
        * loads file from argument path
        * */

        if(model.loadBoardFromFile(new File(path))){
            this.gameMessage.setText("Game Loaded");

        }
        else{
            this.gameMessage.setText("Load Error");

        }
        gameStart();
        this.movelabel.setText("Moves: " + 0);
        this.N = model.getDimension();
        this.gridpane = makeGridPane();

        this.scene = new Scene(this.gridpane);
        stage.setScene(scene);
        displayBoard();

    }



    private GridPane makeGridPane(){
        /* \
        * private gridpane helper
        * makes a N by N empty gridpane of buttons
        *
        * adds controls and labels to the bottom of the gridpane
        *
        * */
        buttons = new Button[N][N];
        GridPane gridPane = new GridPane();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Button button = new Button();
                buttons[i][j] = button;
                //button.setOnAction(event ->
                //        gridbuttonpress(gridPane.getColumnIndex(button), gridPane.getRowIndex(button)));

                button.setStyle(EMPTY);


                button.setMinSize(75,75);
                gridPane.add(button, i, j);
            }

        }
        Label label = new Label("Moves: " + 0);
        this.movelabel = label;
        label.setMinSize(75,75);
        gridPane.add(label, 0, N);





        Button button2 = new Button("Load game");
        button2.setOnAction(event ->
                loadgame());
        gridPane.add(button2, 2, N);


        Button button3 = new Button("Hint");
        gridPane.add(button3, 3, N);
        button3.setOnAction(event ->
                getHint());

        Label label1 = new Label("");
        this.gameMessage = label1;
        label.setMinSize(75,75);
        gridPane.add(label1, 4, N);



        Button tiltUp = new Button("^");
        tup = tiltUp;
        tup.setStyle(GREY);
        tiltUp.setOnAction(event ->
                tilt("N"));
        gridPane.add(tiltUp, 0, N+1);



        Button tiltRight = new Button(">");
        tright = tiltRight;
        tright.setStyle(GREY);
        tiltRight.setOnAction(event ->
                tilt("E"));
        gridPane.add(tiltRight, 1, N+1);


        Button tiltDown = new Button("v");
        tdown = tiltDown;
        tdown.setStyle(GREY);
        gridPane.add(tiltDown, 2, N+1);
        tiltDown.setOnAction(event ->
                tilt("S"));

        Button tiltLeft = new Button("<");
        tleft = tiltLeft;
        tleft.setStyle(GREY);
        gridPane.add(tiltLeft, 3, N+1);
        tiltLeft.setOnAction(event ->
                tilt("W"));

        Label label2 = new Label("");
        label2.setMinSize(75,75);
        gridPane.add(label2, 4, N+1);

        return gridPane;
    }

    public static void main(String[] args) {

        /*
        * Runs on start, creates a gui and launches the program
        * checks for arguments and passes it through using a static var
        * */
        boolean x = false;
        if(args.length == 1){
            x =true;
            argcheck = true;
            arg = args[0];

        }
        TiltGUI ui = new TiltGUI();
        Application.launch(args);

        ui.run();


    }



    public void tilt(String x){
        /*calls model.tilt based on which
        *button was pressed and tilts in the desired
        * direction
        * */

            if (x.equals("N")) {
                model.tiltUp();
            } else if (x.equals("S")) {
                model.tiltDown();
            } else if (x.equals("E")) {
                model.tiltRight();
            } else if (x.equals("W")) {
                model.tiltLeft();
            }
        tup.setStyle(GREY);
        tdown.setStyle(GREY);
        tright.setStyle(GREY);
        tleft.setStyle(GREY);
        update(model, "\nTilting...");
    }

    @Override
    public void stop() throws Exception {
        /*
        * runs on close
        * */
        super.stop();
    }


    @Override
    public void update(TiltModel model, String msg) {
        /*
        * updates and re-displayed the board, and sends an update message
        * */
        if (msg.equals(TiltModel.LOADED)){ // game is loaded successfully
            System.out.println("Game Loaded");
            displayBoard();
            return;
        }else if (msg.equals(TiltModel.LOAD_FAILED)){ //Game failed to load
            System.out.println("Error Loading Game");
            return;
        } else if (msg.startsWith(TiltModel.HINT_PREFIX)) { //Model is reporting a  hint
            //System.out.println(msg);
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
            this.gameMessage.setText("You win!");

            gameOn = false;
            return;
        }
        displayBoard(); // renders the board
        //System.out.println(msg);
    }
}
