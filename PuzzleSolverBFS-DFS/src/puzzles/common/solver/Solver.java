package puzzles.common.solver;

import java.util.*;

/**
 * The class that holds the BFS method and is used perform parts of solving all puzzles
 *
 * @author Parker Noffke
 */
public class Solver{

    /**
     * Method that performs the BFS to find the solution. Calls Configuration's methods to perform
     * needed actions which go to the corresponding puzzle's configuration class.
     * @param start The starting Configuration for a puzzle
     * @return a LinkedList path to get to the solution
     */
    public static List<Configuration> solve(Configuration start) {
        //Initializing the method
        Map<Configuration, Configuration> predecessor = new HashMap<>();
        LinkedList<Configuration> queue = new LinkedList<Configuration>();
        Configuration solution = null;
        int totalConfig = 1;

        predecessor.put(start, null);
        queue.add(start);

        while(!queue.isEmpty()) {
            Configuration parent = queue.remove(0);
            if(parent.isSolution()) {
                //solution found
                solution = parent;
                break;
            } else {
                for (Configuration neighbor : parent.getNeighbors()) {
                    //add to total configurations for each neighbor
                    totalConfig++;
                    //add unique neighbors to predecessor and queue
                    if(!predecessor.containsKey(neighbor)) {
                        //System.out.println(neighbor);
                        predecessor.put(neighbor, parent);
                        queue.add(neighbor);
                    }
                }
            }
        }
        //Print total configurations
        System.out.println("\nTotal configs: " + totalConfig);
        //Print unique configurations
        System.out.println("Unique configs: " + predecessor.size());
        //didn't find finished solution
        if (solution == null) {
            System.out.println("No solution");
            return null;
        } else {
            List<Configuration> path = new LinkedList<Configuration>();
            //create path
            path.add(solution);
            Configuration prevConfig = predecessor.get(solution);
            while(prevConfig != null) {
                path.add(0, prevConfig);
                prevConfig = predecessor.get(prevConfig);
            }
            // return shortest path
            return path;
        }

    }
}

