package puzzles.jam.model;

import java.util.Objects;

/**
 * The class that stores a (row, col) pair
 * and allows changes to that pair
 * @author Parker Noffke
 */
public class Coordinate {
    /**
     * The row of the coordinate pair
     */
    private int row;
    /**
     * The column of the coordinate pair
     */
    private int col;

    /**
     * the constructor used to make a Coordinate
     * @param row the row of the coordinate pair
     * @param col the column of the coordinate pair
     */
    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the row of the coordinate pair
     * @return the row of the coordinate pair
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column of the coordinate pair
     * @return the column of the coordinate pair
     */
    public int getCol() {
        return col;
    }

    /**
     * Changes the row of the coordinate pair
     * @param row what the row of the coordinate pair is being changes to
     */
    public void changeRow(int row) {this.row = row;}

    /**
     * Changes the column of the coordinate pair
     * @param col what the column of the coordinate pair is being changes to
     */
    public void changeCol(int col) {this.col = col;}

    /**
     * Tests whether two Coordinates are equal,
     * meaning they have the same row, col pair
     * @param o The other Coordinate being compared to
     * @return True iff the two Coordinates have the same row, col pair
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return row == that.row && col == that.col;
    }

    /**
     * Returns the hashcode of the Coordinate
     * @return The hashcode of the Coordinate
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * Returns a String representation of the Coordinate
     * @return a String representation of the Coordinate
     */
    @Override
    public String toString() {
        return "(" + getRow() + "," + getCol() + ")";
    }
}
