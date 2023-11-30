package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;
/**
 * Holds all methods, and state for Configurations for the String puzzle
 *
 * @author Parker Noffke
 */

public class StringsConfig implements Configuration {
    /**
     * The String the puzzle starts at
     */
    public static String start;
    /**
     * The solution string for the puzzle
     */
    public static String finish;
    /**
     * the string that the instantiation of StringsConfig is at
     */
    private String current;

    /**
     * The constructor used for initializing the strings puzzle
     * @param s The starting String for the puzzle
     * @param f The desired String for the puzzle
     */
    public StringsConfig(String s, String f) {
        start = s;
        finish = f;
        this.current = start;
    }

    /**
     * The constructor used to create neighbors
     * @param current The version of the string for that neighbor
     */
    public StringsConfig(String current) {
        this.current = current;
    }


    /**
     * The method used to check if a StringsConfig has reached the solution
     * @return Whether the StringsConfig has reached the solution
     */
    @Override
    public boolean isSolution() {
        boolean result = false;
        if (current.equals(finish)) {
            result = true;
        }
        return result;
    }

    /**
     * Creates an arraylist of all the immediate neighbors
     * of the current string and returns it.
     * @return An ArrayList of all the immediate neighbors of the current string
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbors = new ArrayList<Configuration>();
        //for each letter in the string being worked with
        for (int index = 0; index < current.length(); index++) {
            //add the neighbor that changes the index letter forward alphabetically
            int newLetterForward = current.charAt(index) + 1;
            if (newLetterForward == 91) {newLetterForward = 65;}
            String newWord = current.substring(0,index) + (char)newLetterForward + current.substring(index + 1);
            neighbors.add(new StringsConfig(newWord));
            //add the neighbor that changes the index letter backward alphabetically
            int newLetterBack = current.charAt(index) - 1;
            if (newLetterBack == 64) {newLetterBack = 90;}
            String newWordBack = current.substring(0,index) + (char)newLetterBack + current.substring(index + 1);
            neighbors.add(new StringsConfig(newWordBack));
        }
        return neighbors;
    }

    /**
     * checks if one StringsConfig is equal to another StringsConfig by comparing their current String
     * @param other the other StringsConfig being looked at
     * @return Whether both StringsConfig's current strings are equal
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof StringsConfig) {
            StringsConfig otherConfig = (StringsConfig) other;
            result = this.current.equals(otherConfig.current);
        }
        return result;
    }

    /**
     * Returns the hashcode which is the hashcode of the current String
     * @return the hashcode of the current String
     */
    @Override
    public int hashCode() {
        return current.hashCode();
    }

    /**
     * Returns the current String
     * @return the current String
     */
    @Override
    public String toString() {
        return current;
    }
}
