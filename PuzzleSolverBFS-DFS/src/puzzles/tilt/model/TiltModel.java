package puzzles.tilt.model;

import puzzles.common.Observer;
import puzzles.common.solver.Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TiltModel implements Iterable<Tile>{
    public static String LOADED = "loaded";
    private Random rng = null;

    /**
     * Message sent when a board has failed to load.
     */
    public static String LOAD_FAILED = "loadFailed";
    public static String HINT_PREFIX = "Hint:";


    private int moves;
    Board unchangedBoard;
    static Board board;





    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private TiltConfig currentConfig;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<TiltModel, String> observer) {
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

    public boolean loadBoardFromFile(File file)  {
        try {
            Scanner in = new Scanner(file);
            this.board = new Board(in.nextInt());
            Iterator<Tile> it = board.iterator();


            while (it.hasNext()) {
                Tile t = it.next();
                char v = in.next().charAt(0);

                try{
                    t.setTile(v);
                    if(t.getState() == 'G'){
                        this.getBoard().addGreenTile();
                    }
                    if(t.getState() == 'B'){
                        this.getBoard().addBlueTiles();
                    }

                } catch (Exception E){
                    announce(LOAD_FAILED);
                    return false; //invalid file
                }
            }
            moves = 0;
            announce(LOADED);
            this.unchangedBoard = this.board;

            return true;
        }catch (FileNotFoundException e) {
            announce(LOAD_FAILED);
            return false; //invalid file
        }


    }

    public void addMove(){
        moves++;
    }

    public boolean won(){
        int counter = 0;
        for (Tile t: board) {
            if(t.getState() == 'G') {
                counter++;
            }

        }
        return (counter == 0);
    }

    public void generateRandomBoard(){
        int steps = 0;
        generateRandomBoard(steps);

    }
    void generateRandomBoard(int steps){
        Boolean x;
        this.board = new Board();
        this.board.getTile((int) Math.round(Math.random()*4), (int) Math.round(Math.random()*4)).setTile('O');

        for (int i = 0; i < (int) Math.round(Math.random()*3); i++) {
            x = true;
            while (x) {
                Tile y = this.board.getTile((int) Math.round(Math.random() * 4), (int) Math.round(Math.random() * 4));
                if (y.getState() == '.') {
                    this.board.getTile(y.getX(), y.getY()).setTile('*');
                    x = false;
                }
            }
        }
        for (int i = 0; i < (int) Math.round(Math.random()*2)+1; i++) {
            this.board.addGreenTile();
            x = true;

            while (x) {
                Tile y = this.board.getTile((int) Math.round(Math.random() * 4), (int) Math.round(Math.random() * 4));
                if (y.getState() == '.') {
                    this.board.getTile(y.getX(), y.getY()).setTile('G');
                    x = false;
                }
            }
        }
        for (int i = 0; i < (int) Math.round(Math.random()*2)+1; i++) {
            x = true;
            this.board.addBlueTiles();


            while (x) {
                Tile y = this.board.getTile((int) Math.round(Math.random() * 4), (int) Math.round(Math.random() * 4));
                if (y.getState() == '.') {
                    this.board.getTile(y.getX(), y.getY()).setTile('B');
                    x = false;
                }
            }
        }

        //System.out.println(this);
        moves = 0;
        announce(LOADED);
    }


    public int getDimension(){
        return this.board.getBoardSize();
    }

    public void tiltUp(){
        this.board.tiltUp();
    }
    public void tiltDown(){
        this.board.tiltDown();



    }
    public void tiltRight(){
        this.board.tiltRight();
    }
    public void tiltLeft(){
        this.board.tiltLeft();
    }

    public Board getBoard() {
        return this.board;
    }

    private void announce(){
        announce("");
    }
    /**
     * Announce to observers the model has changed;
     */
    private void announce( String arg ) {
        for ( var obs : this.observers ) {
            obs.update( this, arg );
        }
    }



    public boolean gameOver(){
        for (Tile t : board){
            if (t.getState() == 'G'){
                return  false;
            }
        }
        return true;
    }

    public String getHint(){
        String ret;
        Solver solver = new Solver();
        TiltConfig start = new TiltConfig(this.board);
        if(((LinkedList)solver.solve(start)) != null) {

             ret = ((LinkedList) solver.solve(start)).get(1).toString();
        }
        else{
             ret = "No solution";
        }
        announce(HINT_PREFIX);
        // return this.board.getTile(next.source.x,next.source.y);
        return ret;
    }



    public Tile getTile(int x, int y){
        return board.getTile(x,y);
    }

    public static void main(String[] args){
        TiltModel l = new TiltModel();
        l.generateRandomBoard();
        int i = 0;
        for (Tile t : l){

            System.out.print(t);
            i++;
            if(i == 5){
                System.out.println();
                i = 0;
            }
        }
        System.out.println();
        l.generateRandomBoard();
        i = 0;
        for (Tile t : l){

            System.out.print(t);
            i++;
            if(i == 5){
                System.out.println();
                i = 0;
            }
        }
        //l.tiltLeft();
        //System.out.println();
        //i = 0;
        //for (Tile t : l){

        //    System.out.print(t);
        //    i++;
        //    if(i == 5){
        //        System.out.println();
        //       i = 0;
        //    }
        //}
    }








    @Override
    public Iterator<Tile> iterator() {
        return board.iterator();
    }
}
