package puzzles.water;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Parker Noffke
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     *
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(
                    ("Usage: java Water amount bucket1 bucket2 ...")
            );
        } else {
            int desiredGal = Integer.parseInt(args[0]);
            int[] buckets = new int[args.length-1];
            //reads bucket capacities into an Array
            for (int i = 1; i < args.length; i++) {
                buckets[i - 1] = Integer.parseInt(args[i]);
            }
            WaterConfig starter = new WaterConfig(desiredGal, buckets);
            //creating an ArrayList version of the bucket capacities so it can be printed properly
            ArrayList<Integer> startBuckets = new ArrayList<Integer>();
            for (int bucket : buckets) {
                startBuckets.add(bucket);
            }
            System.out.println("Amount: " + desiredGal + ", Buckets: " + startBuckets);
            //creates the path
            List<Configuration> path = Solver.solve(starter);
            if (path == null) {
                //solution wasn't found
                System.out.println("No solution");
            } else {
                //prints out path in desired format
                int stepNum = 0;
                for (Configuration element : path) {
                    System.out.println("Step " + stepNum + ": " + element.toString());
                    stepNum++;
                }
            }
        }
    }
}
