package puzzles.tilt.model;

import java.util.Arrays;
import java.util.Iterator;

/**
 * A board of NxN tiles. You will likely not need to interact with this directly.
 */
public class Board  implements Iterable<Tile>{
    int moves = 0;

    static int BOARD_SIZE = 5;
    int greenTiles = 0;
    int blueTiles = 0;

    private Tile[][] tiles;
    Board(int size) {
        this.BOARD_SIZE = size;
        tiles= new Tile[BOARD_SIZE][BOARD_SIZE];
        for(int x = 0; x < Board.BOARD_SIZE; x++){
            for(int y = 0; y < Board.BOARD_SIZE; y++){
                tiles[x][y]= new Tile(x,y,this,'.');
            }
        }
    }
    Board() {
        tiles= new Tile[BOARD_SIZE][BOARD_SIZE];
        for(int x = 0; x < Board.BOARD_SIZE; x++){
            for(int y = 0; y < Board.BOARD_SIZE; y++){
                tiles[x][y]= new Tile(x,y,this,'.');
            }
        }
    }

    Board(Board b){
        this(BOARD_SIZE); // init array
        //deep copy
        this.blueTiles = 0;
        this.greenTiles = 0;
        for(int x = 0; x < Board.BOARD_SIZE; x++){
            for(int y = 0; y < Board.BOARD_SIZE; y++){
                tiles[x][y].setTile(b.tiles[x][y].getState());
                if(tiles[x][y].getState() == 'B'){
                    addBlueTiles();
                }
                if(tiles[x][y].getState() == 'G'){
                    addGreenTile();
                }
            }
        }

    }

    public static int getBoardSize() {
        return BOARD_SIZE;
    }


    public void addGreenTile() {
        this.greenTiles++;
    }

    public void addBlueTiles() {
        this.blueTiles++;
    }

    public static void main(String[] args) {
        Board b = new Board(BOARD_SIZE);
        Board a = new Board(BOARD_SIZE);
        //a.toggleTile(1,1);


        System.out.println(a.equals(b));
        for (Tile t : a){
            System.out.println(t);
            for (Tile n :t.getNeighbors()){
                System.out.println("\t"+n);
            }
        }
    }

    Tile[][] getTiles() {
        return tiles;
    }

    Tile getTile(int x, int y) {
        return tiles[x][y];
    }



    @Override
    public Iterator<Tile> iterator() {
        return new BoardIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board tiles1 = (Board) o;
        for( int x = 0; x <BOARD_SIZE; x ++){
            for( int y = 0; y <BOARD_SIZE; y ++){
                //System.out.println(tiles1.tiles[x][y]+" : "+tiles[x][y]);
                if (tiles1.tiles[x][y].getState() != tiles[x][y].getState()){
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        if(tiles == null){
            return 0;
        }
        int ret = 1;
        for(int row = 0; row<BOARD_SIZE; row++) {
            //System.out.println(Arrays.hashCode(tiles[row]));
            ret += 31 * ret + Arrays.hashCode(tiles[row]);
        }
        return ret;
    }
    public void tiltUp(){
        Board preMove = new Board(this);
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(tiles[i][j].getState() == 'G' || tiles[i][j].getState() == 'B'){

                    if(j >0){
                        int[] coords = {i,j};

                        int[] copycoords = {i,j};

                        copycoords = tiltUp(copycoords);

                        if(tiles[copycoords[0]][copycoords[1]].getState() != 'O') {

                            tiles[copycoords[0]][copycoords[1]].setTile(tiles[i][j].getState());
                        }
                        if(!(copycoords[1] == j)) {
                            tiles[i][j].setTile('.');
                        }

                    }

                }
            }
        }
        int counter = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(tiles[i][j].getState() == 'B'){
                    counter++;

                }

            }


        }
        this.moves++;
        if(counter != blueTiles){
            this.tiles = preMove.getTiles();
            this.moves--;
        }

    }
    private int[] tiltUp(int[] t){
        int[] set= new int[2];
        set[0] = t[0];
        set[1] = t[1];

        int x = t[0];
        int y = t[1];

        if(y-1 >= 0 && tiles[x][y-1].getState() == '.'){
            if(y > 0){
                set[1]--;
                return tiltUp(set);
            }
            //set[1]--;
            return set;

        }
        else{
            if(y-1 >= 0 && tiles[x][y-1].getState() == 'O'){
                set[1]--;
            }
            //set[1]++;
            return set;
        }

    }



    public void tiltDown(){
        Board preMove = new Board(this);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = BOARD_SIZE-1; j >= 0; j--) {
                if(tiles[i][j].getState() == 'G' || tiles[i][j].getState() == 'B'){

                    if(j < BOARD_SIZE){
                        int[] coords = {i,j};

                        int[] copycoords = {i,j};

                        copycoords = tiltDown(copycoords);

                        if(tiles[copycoords[0]][copycoords[1]].getState() != 'O') {

                            tiles[copycoords[0]][copycoords[1]].setTile(tiles[i][j].getState());
                        }
                        if(!(copycoords[1] == j)) {
                            tiles[i][j].setTile('.');
                        }
                    }

                }
            }
        }
        int counter = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(tiles[i][j].getState() == 'B'){
                    counter++;

                }

            }


        }
        this.moves++;
        if(counter != blueTiles){
            this.tiles = preMove.getTiles();
            this.moves--;
        }

    }
    private int[] tiltDown(int[] t){
        int[] set= new int[2];
        set[0] = t[0];
        set[1] = t[1];

        int x = t[0];
        int y = t[1];

        if(y+1 < BOARD_SIZE && tiles[x][y+1].getState() == '.'){
            if(y < BOARD_SIZE-1){
                set[1]++;
                return tiltDown(set);
            }
            //set[1]--;
            return set;

        }
        else{
            if(y+1 < BOARD_SIZE && tiles[x][y+1].getState() == 'O'){
                set[1]++;
            }

            //set[1]++;
            return set;
        }

    }

    public void tiltRight(){
        Board preMove = new Board(this);

        for (int i = BOARD_SIZE - 1; i >= 0; i--) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(tiles[i][j].getState() == 'G' || tiles[i][j].getState() == 'B'){

                    if(j < BOARD_SIZE){
                        int[] coords = {i,j};

                        int[] copycoords = {i,j};

                        copycoords = tiltRight(copycoords);

                        if(tiles[copycoords[0]][copycoords[1]].getState() != 'O') {

                            tiles[copycoords[0]][copycoords[1]].setTile(tiles[i][j].getState());
                        }
                        if(!(copycoords[0] == i)) {
                            tiles[i][j].setTile('.');
                        }
                    }

                }
            }
        }
        int counter = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(tiles[i][j].getState() == 'B'){
                    counter++;

                }

            }


        }
        this.moves++;
        if(counter != blueTiles){
            this.tiles = preMove.getTiles();
            this.moves--;
        }

    }
    private int[] tiltRight(int[] t){
        int[] set= new int[2];
        set[0] = t[0];
        set[1] = t[1];

        int x = t[0];
        int y = t[1];

        if(x+1 < BOARD_SIZE && tiles[x+1][y].getState() == '.'){
            if(x < BOARD_SIZE-1){
                set[0]++;
                return tiltRight(set);
            }
            //set[1]--;
            return set;

        }
        else{
            if(x+1 < BOARD_SIZE && tiles[x+1][y].getState() == 'O'){
                set[0]++;
            }
            //set[1]++;
            return set;
        }

    }
    public void tiltLeft(){
        Board preMove = new Board(this);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(tiles[i][j].getState() == 'G' || tiles[i][j].getState() == 'B'){

                    if(j >= 0){
                        int[] coords = {i,j};

                        int[] copycoords = {i,j};

                        copycoords = tiltLeft(copycoords);
                        if(tiles[copycoords[0]][copycoords[1]].getState() != 'O') {

                            tiles[copycoords[0]][copycoords[1]].setTile(tiles[i][j].getState());
                        }
                        if(!(copycoords[0] == i)) {
                            tiles[i][j].setTile('.');
                        }
                    }

                }
            }
        }
        int counter = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(tiles[i][j].getState() == 'B'){
                    counter++;

                }

            }


        }
        this.moves++;
        if(counter != blueTiles){
            this.tiles = preMove.getTiles();
            this.moves--;
        }

    }

    public int getMoves() {
        return moves;
    }

    private int[] tiltLeft(int[] t){
        int[] set= new int[2];
        set[0] = t[0];
        set[1] = t[1];

        int x = t[0];
        int y = t[1];

        if(x-1 >= 0 && tiles[x-1][y].getState() == '.'){
            if(x > 0){
                set[0]--;
                return tiltLeft(set);
            }
            //set[1]--;
            return set;

        }
        else{
            if(x-1 >= 0 && tiles[x-1][y].getState() == 'O'){
                set[0]--;
            }
            //set[1]++;
            return set;
        }





    }
    @Override
    public String toString() {
        String string = "";
        int currentRow = -1;
        for(Tile t : this){
            if (currentRow!=t.getY()){
                //newline for new rows.
                string +="\n";
                currentRow=t.getY();
            }
            char symbol = t.getState();

            string += (symbol+" ");

        }

        return string;

    }



    /**
     * Iterates Left to Right; Top to Bottom
     */
    private class BoardIterator implements Iterator<Tile>{
        int x;
        int y;

        public BoardIterator() {
            x=-1;
            y=0;
        }

        @Override
        public Tile next() {
            if (x>=BOARD_SIZE-1 ){
                y += 1;
                x=0;
            }else{
                x+=1;
            }
            // System.out.printf("%d,%d",x,y);
            return tiles[x][y];
        }

        @Override
        public boolean hasNext() {
            //System.out.printf("HAS NEXT: %2$d < %1$d %3$d < %1$d%n",BOARD_SIZE,x,y);
            return x<BOARD_SIZE-1 || y<BOARD_SIZE-1;
        }



    }
}

