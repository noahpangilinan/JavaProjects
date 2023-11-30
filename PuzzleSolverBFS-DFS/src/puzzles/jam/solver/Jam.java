package puzzles.jam.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.model.JamConfig;
import java.util.List;

/**
 * The main class used for running Jam which only solves puzzles.
 * Initiating the solving and prints the steps.
 * @author Parker Noffke
 */
public class Jam {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Jam filename");
        }
        String filename = args[0];
        System.out.println("File: " + filename);
        //creates initial config
        JamConfig puzzle = new JamConfig(filename);
        //solves puzzle
        List<Configuration> path = Solver.solve(puzzle);
        if (path == null) {
            //solution wasn't found
            System.out.println("No solution");
        } else {
            //prints out path in desired format
            int stepNum = 0;
            for (Configuration element : path) {
                System.out.println("Step " + stepNum + ":");
                if(element instanceof JamConfig) {
                    JamConfig currentConfig = (JamConfig) element;
                    currentConfig.displayBoard();
                    System.out.println();
                }
                stepNum++;
            }
        }
    }
}