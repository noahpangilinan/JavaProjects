package puzzles.jam.model;

import java.util.Objects;

/**
 * The class that stores the state of a car
 * and provides utility for the object
 * @author Parker Noffke
 */
public class Car {
    /**
     * The label of the car
     */
    private char label;
    /**
     * The (row, col) pair that the car starts at
     */
    private Coordinate start;
    /**
     * The (row, col) pair that the car ends at
     */
    private Coordinate end;
    /**
     * True iff the car is horizontal
     */
    private boolean horizontal;

    /**
     * The constructor for the car
     * @param label the label that the car will display as
     * @param s the (row, col) pair that the car will start at
     * @param e the (row, col) pair that the car will end at
     */
    public Car(char label, Coordinate s, Coordinate e){
        this.label = label;
        this.start = new Coordinate(s.getRow(),s.getCol());
        this.end = new Coordinate(e.getRow(),e.getCol());
        if(start.getRow() == end.getRow()) {
            horizontal = true;
        } else {horizontal = false;}
    }

    /**
     * returns the label of the car
     * @return the label of the car
     */
    public char getLabel() {return this.label;}

    /**
     * Returns the (row, col) pair that the car starts at
     * @return the (row, col) pair that the car starts at
     */
    public Coordinate getStart() {return this.start;}

    /**
     * Returns the (row, col) pair that the car ends at
     * @return the (row, col) pair that the car ends at
     */
    public Coordinate getEnd() {return this.end;}

    /**
     * Returns whether the car is horizontal or not
     * @return whether the car is horizontal or not
     */
    public boolean isHorizontal() {return this.horizontal;}

    /**
     * Tests whether two Cars are equal,
     * meaning they have the label, start Coordinate, and end Coordinate
     * @param o The other Car being compared to
     * @return True iff the two Cars have the same label, start Coordinate, and end Coordinate
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return label == car.label && horizontal == car.horizontal && start.equals(car.start) && end.equals(car.end);
    }

    /**
     * Returns the hashcode of the Car
     * @return The hashcode of the Car
     */
    @Override
    public int hashCode() {
        return Objects.hash(label, start, end, horizontal);
    }

    /**
     * Returns a String representation of the Car
     * @return a String representation of the Car
     */
    @Override
    public String toString() {
        return "Car: " + "Label: " + label + " Start coord: " + start + " End coord: " + end + " Horizontal: " + isHorizontal();
    }

}
