package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.List;

/**
 * Main class for the strings puzzle.
 *
 * @author Parker Noffke
 */
public class Strings{
    /**
     * Run an instance of the strings puzzle.
     *
     * @param args [0]: the starting string;
     *             [1]: the finish string.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            String start = args[0];
            String finish = args[1];
            //create new starting config
            StringsConfig starter = new StringsConfig(start, finish);
            System.out.println("Start: " + start + ", End: " + finish);
            //get the shortest path to solve the problem
            List<Configuration> path = Solver.solve(starter);
            if (path == null) {
                //no solution found
                System.out.println("No solution");
            } else {
                //print out steps to get to solution
                int stepNum = 0;
                for (Configuration element : path) {
                    System.out.println("Step " + stepNum + ": " + element.toString());
                    stepNum++;
                }
            }
        }
    }
}

