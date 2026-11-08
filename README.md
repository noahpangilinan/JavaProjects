# Java Projects

Coursework from RIT's CSAPX (Computer Science for AP Students): object-oriented design, recursion,
quadtrees, backtracking, breadth-first search, and model-view-controller GUIs in JavaFX.

| Project | What it is |
| --- | --- |
| [`lab4`](lab4) | **ToyLand.** An inheritance hierarchy of toys (action figures, dolls, RC cars, robots, play dough) built through a factory, with a simulation that plays with each toy until it retires. JUnit tests in `src/test` |
| [`lab6`](lab6) | **Rich Image Tool.** Compresses square grayscale images into a quadtree format (`RITCompress`) and expands them back (`RITUncompress`) |
| [`lab7`](lab7) | **Magnets.** A backtracking solver for the magnets logic puzzle, with a generic `Backtracker` and JUnit tests |
| [`lab8`](lab8) | **Lights Out.** The Lights Out puzzle with a shared model, a text UI and a JavaFX GUI, plus a solver |
| [`PuzzleSolverBFS-DFS`](PuzzleSolverBFS-DFS) | Team project with Parker Noffke: one breadth-first `Solver` that works for any puzzle implementing `Configuration`, applied to Strings, Water Buckets, Jam (a Rush Hour style traffic puzzle) and Tilt, with text and JavaFX front ends for Jam and Tilt |
| [`MovieSort`](MovieSort) | A Python exercise from the same course: loads a large IMDb-style dataset into dictionaries and answers sorting and search queries by runtime, year, votes, ID or title |

## Running

Each Java folder is a standalone project with its sources under `src/`. Open the folder in an IDE, or
compile from the command line with JDK 17 or later. The GUI projects (`lab8`, `PuzzleSolverBFS-DFS`)
also need [JavaFX](https://openjfx.io/) on the module path.

```sh
cd PuzzleSolverBFS-DFS
javac -d out $(find src -name "*.java" ! -path "*gui*" ! -name module-info.java)
java -cp out puzzles.water.Water 4 3 5        # amount wanted, then bucket sizes
```

The command-line programs print their usage when run without arguments, for example
`java ToyLand toy-file`, `java Magnets filename debug` and `java Jam filename`.
