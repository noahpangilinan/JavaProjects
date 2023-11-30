package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.LightsOutModel;
import model.Observer;
import model.Tile;

import java.io.File;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class LightOutGUI extends Application implements Observer<LightsOutModel, String>{

    int N = 5;
    int movecounter = 0;

    GridPane gridpane;
    Button[][] buttons;
    Label movelabel;
    Label gameMessage;

    Stage stage;

    String buttonmsg = "";
    final static String on = "-fx-background-color: #00FFFF; -fx-border-color: #ffffff; -fx-border-width: 1px;";
    final static String off = "-fx-background-color: #5A5A5A; -fx-border-color: #ffffff; -fx-border-width: 1px;";

    final static String hint = "-fx-background-color: #FF00FF; -fx-border-color: #ffffff; -fx-border-width: 1px;";


    LightsOutModel model;
    boolean gameOn = false;
    Scanner in;

    public LightOutGUI() {
        model = new LightsOutModel();
        model.addObserver(this);
        gameOn = false;
        in = new Scanner( System.in );
    }







    public boolean gameStart(){

        boolean ready = false;
        while(!ready){
            gameOn = true;
            ready = true;
        }
        return true;
    }






    public void displayBoard(){
        //formatting for text ui is soooo elegant
        //System.out.print("\033[0;4m"); //turn on underline
        //prints the tiles

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if(model.getTile(i,j).isOn()){
                    buttons[i][j].setStyle(on);
                }
                else{
                    buttons[i][j].setStyle(off);
                }

            }

        }

    }





    public void run() {
        while (true) {
            if (!gameStart()) //loads new games or quits
                break;
            gameLoop(); // gameplay
        }

    }






    private void gameLoop() {
        String msg;

        while (gameOn) {
            msg = "";
            String command = this.buttonmsg;
            if (command.equals("q") || command.equals("Q")) {
                System.out.println("Quitting to main menu.");
                gameOn = false;

                return;

            } else if (command.equals("h") || command.equals("H")) {
                model.getHint();

                // model.toggleTile(hint.getX(),hint.getY());

            }

        }
    }



    @Override
    public void init() throws Exception {
        System.out.println("init: Initialize and connect to model!");
    }
    @Override
    public void start(Stage stage) throws Exception {
        //create a new FileChooser



        this.stage = stage;
        this.gridpane = makeGridPane();
        Scene scene = new Scene(this.gridpane);
        stage.setScene(scene);
        stage.setTitle("Lights Out!");
        stage.show();








    }

    public void gridbuttonpress(int row, int col){
        if(gameOn) {
            this.movecounter++;
            this.movelabel.setText("Moves: " + movecounter);
            model.toggleTile(row, col);
            displayBoard();

        }


    }

    public void loadgame(){
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
        this.movecounter = 0;
        this.movelabel.setText("Moves: " + this.movecounter);
        displayBoard();

    }

    public void gethint(){
        if(gameOn) {
            int x = model.getHint().getX();
            int y = model.getHint().getY();

            buttons[x][y].setStyle(hint);

        }


    }

    private GridPane makeGridPane(){
        movecounter = 0;
        buttons = new Button[N][N];
        GridPane gridPane = new GridPane();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Button button = new Button();
                buttons[i][j] = button;
                button.setOnAction(event ->
                        gridbuttonpress(gridPane.getColumnIndex(button), gridPane.getRowIndex(button)));

                button.setStyle(off);


                button.setMinSize(75,75);
                gridPane.add(button, i, j);
            }

        }
        Label label = new Label("Moves: " + movecounter);
        this.movelabel = label;
        label.setMinSize(75,75);
        gridPane.add(label, 0, N);



        Button button = new Button("New Game");
        button.setOnAction(event ->
                newgamebutton());
        gridPane.add(button, 1, N);



        Button button2 = new Button("Load game");
        button2.setOnAction(event ->
                loadgame());
        gridPane.add(button2, 2, N);


        Button button3 = new Button("Hint");
        gridPane.add(button3, 3, N);
        button3.setOnAction(event ->
                gethint());

        Label label1 = new Label("");
        this.gameMessage = label1;
        label.setMinSize(75,75);
        gridPane.add(label1, 4, N);

        return gridPane;
    }

    public static void main(String[] args) {
        LightOutGUI ui = new LightOutGUI();
        Application.launch(args);
        ui.run();

    }

    public void newgamebutton(){
        movecounter = 0;
        this.movelabel.setText("Moves: " + movecounter);
        this.gameMessage.setText("");
        gameStart();
        model.generateRandomBoard();
        displayBoard();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }


    @Override
    public void update(LightsOutModel model, String msg) {
        if (msg.equals(LightsOutModel.LOADED)){ // game is loaded successfully
            System.out.println("Game Loaded");
            displayBoard();
            return;
        }else if (msg.equals(LightsOutModel.LOAD_FAILED)){ //Game failed to load
            System.out.println("Error Loading Game");
            return;
        } else if (msg.startsWith(LightsOutModel.HINT_PREFIX)) { //Model is reporting a  hint
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
            this.gameMessage.setText("You win!");

            gameOn = false;
            return;
        }
        displayBoard(); // renders the board
        System.out.println(msg);
    }
}
