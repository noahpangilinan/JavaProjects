package puzzles.water;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Holds all methods, and state for Configurations for the Water puzzle
 *
 * @author Parker Noffke
 */
public class WaterConfig implements Configuration{
    /**
     * the desired gallons for a bucket to have (solution)
     */
    public static int desiredGal;
    /**
     * an integer array of the capcities of each bycket
     */
    public static int[] capacities;
    /**
     * an integer array of the current water levels of each bycket
     */
    private int[] current;

    /**
     * constructor used for initializing the water puzzle
     * @param d the desired gallons for a bucket to have (solution)
     * @param c an integer array of the capcities of each bycket
     */
    public WaterConfig(int d, int[] c) {
        desiredGal = d;
        this.capacities = c;
        current = new int[capacities.length];
    }

    /**
     * cosntructor for creating a neighbor
     * @param curr
     */
    public WaterConfig(int[] curr) { current = curr;}

    /**
     * The method used to check if a WaterConfig has reached the solution (desired bucket capacity
     * @return Whether the WaterConfig has reached the solution (desired bucket capacity)
     */
    @Override
    public boolean isSolution() {
        boolean result = false;
        for (Integer bucket : current) {
            if (bucket == desiredGal) {result = true;}
        }
        return result;
    }

    /**
     * Creates an arraylist of all the immediate neighbors
     * of the current water levels and returns it.
     * @return An ArrayList of all the immediate neighbors of the current water levels
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbors = new ArrayList<Configuration>();
        for (int index = 0; index < current.length; index++) {
            for(int otherIndex = 0; otherIndex < current.length; otherIndex++) {
                //check if the bucket is going to operating on itself
                if (!(index == otherIndex)) {
                    //pour the index bucket into the other indexBucket
                    int[] neighbor1 = Arrays.copyOf(current, current.length);
                    int space = capacities[otherIndex] - neighbor1[otherIndex];
                    int amount = Math.min(neighbor1[index], capacities[otherIndex] - current[otherIndex]);
                    neighbor1[index] -= amount;
                    neighbor1[otherIndex] += amount;
                    neighbors.add(new WaterConfig(neighbor1));
                }
            }
            //dump the index bucket
            int[] neighbor2 = Arrays.copyOf(current, current.length);
            neighbor2[index] = 0;
            neighbors.add(new WaterConfig(neighbor2));
            //fill the index bucket
            int[] neighbor3 = Arrays.copyOf(current, current.length);
            neighbor3[index] = capacities[index];
            neighbors.add(new WaterConfig(neighbor3));
        }
        return neighbors;
    }

    /**
     * checks if one WaterConfig is equal to another WaterConfig by comparing their current water levels
     * @param o the other WaterConfig being looked at
     * @return Whether both WaterConfig's current water levels are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaterConfig that = (WaterConfig) o;
        return Arrays.equals(current, that.current);
    }

    /**
     * Returns the hashcode which is the hashcode of the current Array
     * @return the hashcode of the current Array
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(current);
    }

    /**
     * Returns a String version of the bucket's current water levels
     * @return a String version of the bucket's current water levels
     */
    public String toString() {
        return Arrays.toString(current);
    }
}
