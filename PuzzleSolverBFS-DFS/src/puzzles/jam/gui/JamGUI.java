package puzzles.jam.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.jam.model.Coordinate;
import puzzles.jam.model.JamModel;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;

/**
 * Class that holds all the parts for the GUI version of the JAM puzzle
 * @author Parker Noffke
 */
public class JamGUI extends Application  implements Observer<JamModel, String>  {
    /**
     * The color used for the 'X' car
     */
    private final static String X_CAR_COLOR = "#DF0101";
    /**
     * The size used for fonts in the buttons
     */
    private final static int BUTTON_FONT_SIZE = 20;
    /**
     * The size of buttons on the GUI
     */
    private final static int ICON_SIZE = 75;
    /**
     * The model of the game, used for various actions to progress the game
     */
    private JamModel model;
    /**
     * The FileChooser that allows the user to select a board from their files.
     */
    private FileChooser fileChooser = new FileChooser();
    /**
     * GridPane that holds all the button lights for the game
     */
    private GridPane carGrid = new GridPane();
    /**
     * The text label that displays messages and hints to the user
     */
    private Label message = new Label();
    /**
     * The HashMap that stores the randomly generated colors for that instance of running the GUI
     */
    private HashMap<String, String> carColors = new HashMap<String, String>();
    /**
     * The Stage for the GUI that hols the Scene
     */
    private Stage stage;
    /**
     * The BorderPane that holds all the subparts of the GUI, like message, bottom buttons, and grid of cars
     */
    private BorderPane border = new BorderPane();
    /**
     * True if a car has been selected, false if it hasn't been selected
     */
    private boolean selected;
    /**
     * The Coordinates of the already selected tile when moving a car
     */
    private Coordinate selectedTile;


    /**
     * Initializes some game state like loading the initial board, setting up the FileChooser
     */
    public void init() {
        String filename = getParameters().getRaw().get(0);
        System.out.println("init:   Initialize and connect to model!");
        model = new JamModel(filename);
        model.loadBoardFromFile(filename);
        model.addObserver(this);
        //create a new FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load a game board.");
        //set the directory to the boards folder in the current working directory
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        currentPath += File.separator + "data" + File.separator + "jam";
        fileChooser.setInitialDirectory(new File(currentPath));
        fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Text Files", "*.txt"));

    }

    /**
     * The method that creates the elements for the game
     * and contains the event handlers for the game.
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        //initializations of scene and borderpane to hold all other componenets
        Scene scene = new Scene(border);

        //create top text
        FlowPane topText = new FlowPane();
        message.setText("Message: Game Started" + "\t");

        topText.getChildren().addAll(message);
        border.setTop(topText);

        //create grid of cars
        selected = false;
        for (int row=0; row<model.getBoardRow(); ++row) {
            for (int col=0; col<model.getBoardCol(); ++col) {
                //sets up each button
                Button button1 = new Button();
                String carLabel = String.valueOf(model.getBoard()[row][col]);
                if (carLabel.equals("X")) {
                    button1.setStyle("-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                            "-fx-font-weight: bold;" + "-fx-background-color: " + X_CAR_COLOR + ";");
                    button1.setText(carLabel);
                } else if (carLabel.equals(".")) {
                    button1.setStyle("-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                            "-fx-font-weight: bold;" + "-fx-background-color: " + "#DDDDDD" + ";");
                } else {
                    if(carColors.containsKey(carLabel)) {
                        button1.setStyle("-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                                    "-fx-font-weight: bold;" + "-fx-background-color: " + "#" + carColors.get(carLabel) + ";");
                    } else {
                        Random ran = new Random();
                        int hexNum = ran.nextInt();
                        String hex = Integer.toHexString(hexNum);
                        hex = hex.substring(0, 6);
                        button1.setStyle("-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                                    "-fx-font-weight: bold;" + "-fx-background-color: " + "#" + hex + ";");
                        carColors.put(carLabel, hex);
                    }
                    button1.setText(carLabel);
                }
                button1.setMinSize(ICON_SIZE, ICON_SIZE);
                button1.setMaxSize(ICON_SIZE, ICON_SIZE);
                button1.setPrefSize(50., 50.0);
                button1.setBorder(Border.stroke(Color.DIMGRAY));
                carGrid.add(button1, col, row);
                //event handler for each button
                button1.setOnAction((event) -> {
                    int pressedCol = GridPane.getColumnIndex(button1);
                    int pressedRow = GridPane.getRowIndex(button1);
                    if(selected) {
                        selected = false;
                        model.moveCar(pressedRow, pressedCol, selectedTile);
                    } else {
                        if(model.selectCar(pressedRow, pressedCol)) {selected = true;}
                        selectedTile = new Coordinate(pressedRow, pressedCol);
                    }
                });
            }
        }
        border.setCenter(carGrid);

        //create bottom buttons
        HBox bottomButtons = new HBox();
        Button loadBoard = new Button("Load Game");
        Button reset = new Button("Reset");
        Button hint = new Button("Hint");
        bottomButtons.getChildren().addAll(loadBoard, reset, hint);
        border.setBottom(bottomButtons);

        //set scene and show stage, and set allignment
        bottomButtons.setAlignment(Pos.CENTER);
        carGrid.setAlignment(Pos.CENTER);
        topText.setAlignment(Pos.CENTER);


        stage.setScene(scene);
        stage.setTitle("Jam GUI");
        stage.setResizable(false);
        stage.show();

        //event handlers for bottom three buttons
        loadBoard.setOnAction((event) -> {
            //open up a window for the user to interact with.
            File selectedFile = fileChooser.showOpenDialog(stage);
            String selectedFileString = "data/jam/" + selectedFile.getName();
            model.loadBoardFromFile(selectedFileString);
        });
        reset.setOnAction((event) -> {
            model.reset = true;
            model.loadBoardFromFile(model.getCurrentFile());
        });
        hint.setOnAction((event) -> {
            model.getHint();
        });
    }

    /**
     * Used to update the display of the game depending on the message announced
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param msg optional data the server.model can send to the observer
     */
    @Override
    public void update(JamModel model, String msg) {
        if (msg.equals(JamModel.LOADED)){ // game is loaded successfully
            displayBoard(); // renders the board
            stage.sizeToScene();
            message.setText("Game Loaded");
            return;
        }else if (msg.equals(JamModel.LOAD_FAILED)){ //Game failed to load
            message.setText("Error Loading Game");
            return;
        } else if (msg.startsWith(JamModel.SELECTED)){
            //Model is selecting car
            message.setText(msg);
            return;
        }
        message.setText(msg);
        displayBoard(); // renders the board
    }

    /**
     * The method that displays the visual representation of the board by creating a new GridPane of the updated Board
     */
    public void displayBoard(){
        //displays works on displaying the correct value of the board one tile at a time
        carGrid = new GridPane();
        for (int row=0; row<model.getBoardRow(); ++row) {
            for (int col=0; col<model.getBoardCol(); ++col) {
                Button button1 = new Button();
                String carLabel = String.valueOf(model.getBoard()[row][col]);
                if (carLabel.equals("X")) {
                    button1.setStyle("-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                            "-fx-font-weight: bold;" + "-fx-background-color: " + X_CAR_COLOR + ";");
                    button1.setText(carLabel);
                } else if (carLabel.equals(".")) {
                    button1.setStyle("-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                            "-fx-font-weight: bold;" + "-fx-background-color: " + "#DDDDDD" + ";");
                } else {
                    if(carColors.containsKey(carLabel)) {
                        button1.setStyle("-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                "-fx-font-weight: bold;" + "-fx-background-color: " + "#" + carColors.get(carLabel) + ";");
                    } else {
                        Random ran = new Random();
                        int hexNum = ran.nextInt();
                        String hex = Integer.toHexString(hexNum);
                        hex = hex.substring(0, 6);
                        button1.setStyle("-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                "-fx-font-weight: bold;" + "-fx-background-color: " + "#" + hex + ";");
                        carColors.put(carLabel, hex);
                    }
                    button1.setText(carLabel);
                }
                button1.setMinSize(ICON_SIZE, ICON_SIZE);
                button1.setMaxSize(ICON_SIZE, ICON_SIZE);
                button1.setPrefSize(50., 50.0);
                button1.setBorder(Border.stroke(Color.DIMGRAY));
                carGrid.add(button1, col, row);
                button1.setOnAction((event) -> {
                    int pressedCol = GridPane.getColumnIndex(button1);
                    int pressedRow = GridPane.getRowIndex(button1);
                    if(selected) {
                        selected = false;
                        model.moveCar(pressedRow, pressedCol, selectedTile);
                    } else {
                        if(model.selectCar(pressedRow, pressedCol)) {selected = true;}
                        selectedTile = new Coordinate(pressedRow, pressedCol);
                    }
                });
            }
        }
        border.setCenter(carGrid);
    }

    /**
     * Launches the GUI
     * @param args filename of the initial puzzle
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
