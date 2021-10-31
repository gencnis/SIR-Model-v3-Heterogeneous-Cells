/**
 *  SIR Model object-oriented implementation. This is the "top-level" class that ties all of the
 *  pieces of the cellular automaton together and drives simulation. <br>
 *  <br>
 *  Compilation:  javac SIR.java <br>
 *  Execution:    java SIR [double infectionRate] [double recoveryRate] [int maxDays] [int gridSize] [long frameDuration] <br>
 *  <br>
 *  Usage example:  java SIR 0.13 0.33 90 50 50 <br>
 *      Animates a 50x50 grid of cells, about 10% of which are occupied at startup, and animates
 *      the grid at a speed of 50 milliseconds per frame for up to 90 days with an 13% infection and 33% recovery rate. <br>
 *  <br>
 *  Usage example: java SIR <br>
 *      Animates a SIR Model with the default simulation parameters: a 50x50 grid of cells, about
 *      10% of which are occupied at startup, and animates the grid at a speed of 50 ms per day
 *      for up to 90 with an 16.6% infection rate and a 3.7% recovery rate.
 *  <br>
 *  @author Nisanur Genc, 2021
 *  Credit for help: Caitrin Eaton
 */
public class SIR implements CellularAutomaton {

    protected SIRGrid grid;
    protected SIRVis vis;
    protected SIRWriter writer;
    protected int generation;
    protected int maxDays;

    /**
     * Constructs a new SIR cellular automaton.
     * @param infectionRate (double) the percentage of a cell getting infected
     * @param recoveryRate (double) the percentage of a cell getting recovered
     * @param maxDays (int) the maximum number of days to simulate
     * @param gridSize (int) the width (and height) of the grid, in cells
     * @param frameDuration (long) the length of time (in milliseconds) for which each day should be displayed on-screen
     * @param populationDensity (double) the percent of cells that are initially occupied
     */
    public SIR( double infectionRate, double recoveryRate, int maxDays, int gridSize, long frameDuration, double populationDensity ){
        // Initialize the grid of cells
        grid = new SIRGrid( gridSize );
        grid.populate( populationDensity, infectionRate, recoveryRate );

        // Initialize the simulation time
        generation = 0;
        this.maxDays = maxDays;

        // Initialize the visualization window in which the grid of cells will appear
        vis = new SIRVis( grid );
        vis.setFrameTime( frameDuration );

        // Initialize the writer window for the data will be written into the file.
        writer = new SIRWriter();
        writer.open(maxDays, infectionRate, recoveryRate, gridSize); // opens the file

    }

    /**
     * Simulate a single day with updating it.
     */
    public void update() {
        generation++;
        grid.update();
        vis.update( generation );
        writer.update(generation, grid.getDemographics(), grid.getPercentages());
    }

    /**
     * Simulate the SIR model until either the cells stop changing (the grid has zero flux) or the maximum
     * number of days has been reached.
     */
    public void simulate() {
        generation = 0;
        
        // I AM NOT SURE ABOUT THE IMPLEMENTATION BELOW, IT DID NOT WORK FOR THE SIZE
        // I TRIED TO MAKE THE PROGRAM STOP WHEN ALL THE INFECTED ARE RECOVERED
        // while((generation < maxDays) && (grid.getFlux() > grid.getSize())

        while(generation < maxDays){
            update();
        }

        writer.close(); // closes the writer
    }

    /**
     * Runs initialized the SIR model with the values that were given.
     * @param args String[], command line arguments (optional):
     *             args[0] = infection rate (double) for the cells
     *             args[1] = recovery rate (double) for the cells
     *             args[2] = maximum simulation length (int) in number of days (frames) 
     *             args[3] = grid size (int) in cells
     *             args[4] = frame duration (long) in milliseconds
     */
    public static void main(String[] args) {

        // Establish default simulation parameters, in case the user doesn't supply all/any command line arguments.
        double populationDensity = 0.1; 
        // double populationDensity;
        double infectionRate = 0.166;
        double recoveryRate = 0.037;
        int maxDays = 90;
        int gridSize = 50;
        long frameDuration = 50;

        // Parse command line arguments, allowing the user to customize the simulation
        if (args.length > 0) {
            infectionRate = Double.parseDouble( args[0] );
        }
        if (args.length > 1) {
            recoveryRate = Double.parseDouble( args[1] );
        }
        if (args.length > 2) {
            maxDays = Integer.parseInt( args[2] );
        }
        if (args.length > 3) {
            gridSize = Integer.parseInt( args[3] );
        }
        if (args.length > 4) {
            frameDuration = Long.parseLong( args[4] );
        }

        // Use command line parameters (or default values, if still intact) to configure a new
        // the SIR model, comprised of a set of SIRCells in a SIRGrid and a SIRVis display.

        SIR game = new SIR(infectionRate, recoveryRate, maxDays, gridSize, frameDuration, populationDensity);

        // Run the simulation
        game.simulate( );
    }
}
