package puzzles.tilt.model;

// TODO: implement your TiltConfig for the common solver

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class TiltConfig implements Configuration {
    Board currentboard;

    char prevdir;

    public TiltConfig(Board currentboard) {
        this.currentboard = currentboard;
    }
    public TiltConfig(Board currentboard, char dir) {
        this.currentboard = currentboard;
        this.prevdir = dir;
    }

    public String specialToString(){
        String string = "";
        int currentRow = -1;


        //prints the tiles
        for(Tile t : currentboard){
            if (currentRow!=t.getY() && t.getY() != 0){ //newline for new rows.
                currentRow=t.getY();
                string += "\n";
            }
            char symbol = t.getState();

            string += (symbol+" ");

        }
        return string;
    }

    @Override
    public boolean isSolution() {
        for (Tile t: currentboard) {
            if(t.getState() == 'G'){
                return false;
            }
        }
        return true;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighbors = new ArrayList<>();

        Board up = new Board(currentboard);
        Board down = new Board(currentboard);
        Board left = new Board(currentboard);
        Board right = new Board(currentboard);

        up.tiltUp();
        down.tiltDown();
        left.tiltLeft();
        right.tiltRight();

        TiltConfig upConfig = new TiltConfig(up, 'u');
        TiltConfig downConfig = new TiltConfig(down, 'd');
        TiltConfig leftConfig = new TiltConfig(left, 'l');
        TiltConfig rightConfig = new TiltConfig(right, 'r');

        neighbors.add(downConfig);
        neighbors.add(leftConfig);
        neighbors.add(upConfig);
        neighbors.add(rightConfig);


        return neighbors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TiltConfig that = (TiltConfig) o;
        return currentboard.equals(that.currentboard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentboard);
    }

    @Override
    public String toString() {
        String string = "";
        string+=prevdir;

        return string;

    }
}
