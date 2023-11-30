package puzzles.tilt.solver;


import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.model.TiltConfig;
import puzzles.tilt.model.TiltModel;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Tilt {
    public static void main(String[] args) {
        /*
        * runs the solver on a specific file from args
        * prints out list of steps to solve
        * */
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
        }
        else {

            TiltModel model = new TiltModel();
            model.loadBoardFromFile(new File(args[0]));
            Solver solver = new Solver();
            List<Configuration> list = solver.solve(new TiltConfig(model.getBoard()));
            int i = -1;
            for (Configuration e : list) {
                i++;
                System.out.println("Step " + i + ":");
                System.out.println(((TiltConfig) e).specialToString());
                System.out.println();
            }
        }




    }
}
