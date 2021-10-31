import java.util.Random;
import java.util.Arrays;

/**
 *  Defines the grid used in SIR Model: object-oriented implementation <br>
 * <br>
 *  Can be run directly for testing purposes. The execution command below will:
 *  Creates a 10x10 grid of cells, and progresses through 10 iterations of the game,
 *  demonstrating a day in the simulation. <br>
 * <br>
 *  Compilation:  javac SIRGrid.java <br>
 *  Execution:    java SIRGrid <br>
 * <br>
 *  @author Nisanur Genc, 2021
 *  Credit for help: Caitrin Eaton
 */
public class SIRGrid implements Grid {

    // State-related grid attributes
    protected int flux;
    protected final int size;
    protected SIRCell[][] population;
    protected double infectionRate;
    protected double recoveryRate;
    protected int sus, inf, rec;
    protected int[] stats;

    /**
     * Construct a new grid for the SIR Model.
     * @param size int, the width (and height) of the grid, in cells
     */
    public SIRGrid(int size) {
        this.size = size;
        this.flux = 0;
        this.infectionRate = infectionRate;
        this.population = new SIRCell[size][size]; // references are initially null
        // this.stats = new int[]; // references are initially null
    }

    /**
     * Read the size of the square population array.
     * @return size int, the number of cells along the width (and height) of the square population array.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Read the current flux of this SIR Model, e.g. to determine if it has reached stasis.
     * @return flux int, the number of cells that experienced state changes in the most recent day.
     */
    public int getFlux() {
        return this.flux;
    }

    /**
     * Read the current susceptible of this SIR Model, for the susceptible counter.
     * @return sus int, the number of cells that are susceptible.
     */
    public int getSUS() {
        return this.sus;
    }
    /**
     * Read the current infectious of this SIR Model, for the infectious counter.
     * @return inf int, the number of cells that are infectious.
     */
    public int getINF() {
        return this.inf;
    }
    /**
     * Read the current recovered of this SIR Model, for the recovered counter.
     * @return rec int, the number of cells that are recovered.
     */
    public int getREC() {
        return this.rec;
    }


    /**
     * MASKED
     * Randomly initialize the grid's population.
     * @param density double, the percent of occupied cells
     */
    public void populate(double density, double infectionRate, double recoveryRate) {
        double occupancy;
        Random occupier = new Random();
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                occupancy = occupier.nextDouble();
                if (occupancy < density) {
                    this.population[row][col] = new SIRCell(SIRState.INFECTIOUS, recoveryRate, infectionRate);
                    this.flux++;
                } else {
                    this.population[row][col] = new SIRCell(SIRState.SUSCEPTIBLE, recoveryRate, infectionRate);
                }
            }
        }
        formNeighborhoods();
    }

    /**
     * MASKLESS
     * Randomly initialize the grid's population.
     * @param density double, the percent of occupied cells
     
    public void populate(double density, double infectionRate, double recoveryRate) {
        double occupancy;
        Random occupier = new Random();
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                occupancy = occupier.nextDouble();
                if (occupancy < density) {
                    this.population[row][col] = new SIRCell(SIRState.INFECTIOUS, recoveryRate, infectionRate);
                    this.flux++;
                } else {
                    this.population[row][col] = new SIRCell(SIRState.SUSCEPTIBLE, recoveryRate, infectionRate);
                }
            }
        }
        formNeighborhoods();
    } */

    /**
     * Connect cells to their neighbors.
     */
    protected void formNeighborhoods() {

        // Each cell in the grid has its own neighborhood
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {

                // Add this cell's 8 Moore neighbors to its neighborhood.
                for (int dr = -1; dr < 2; dr++) {
                    for (int dc = -1; dc < 2; dc++) {

                        // A cell shouldn't be its own neighbor.
                        if (dr != 0 || dc != 0) {

                            // Watch out for out of bounds exceptions.
                            if ((row + dr >= 0 && row + dr < this.size) && (col + dc >= 0 && col + dc < this.size)) {

                                // This neighbor is safe to add!
                                this.population[row][col].addNeighbor(this.population[row + dr][col + dc]);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Pretty printing grid objects.
     * @return summary String, a description of the SIRGrid object's current state
     */
    public String toString() {
        String summary = "\nFlux: " + this.flux;
        for (int row = 0; row < this.size; row++) {
            summary += "\n";
            for (int col = 0; col < this.size; col++) {
                if ( this.population[row][col] == null ) {
                    summary += 'N';
                } else if ( this.population[row][col].getCurrentState() == SIRState.SUSCEPTIBLE ) {
                    summary += 'O';
                } else if ( this.population[row][col].getCurrentState() == SIRState.RECOVERED )  {
                    summary += ' ';
                } else {
                    summary += 'I';
                }
            }
        }
        return summary;
    }

    /**
     * Apply the rules of John Conway's Game of Life to progress the simulation by 1 time step for our SIR Model.
     */
    public void update( ){

        int flux = 0;
        int sus = 0;
        int inf = 0;
        int rec = 0;

        // Ask each cell to predict its own next state, tracking the number of cells that will change (flux)
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                
                /* Keep track of changing cells in order to detect stasis

                // DIFFERENT IMPLEMENTATION FOR THE CODE ABOVE:

                if (this.population[row][col].getCurrentState() != this.population[row][col].getNextState()) {
                    flux++;
                } */

                if (this.population[row][col].getCurrentState() == SIRState.INFECTIOUS){
                    if(this.population[row][col].getNextState() == SIRState.INFECTIOUS){
                        flux++;
                        inf++;
                    }
                }
                else if (this.population[row][col].getCurrentState() == SIRState.SUSCEPTIBLE){
                        flux++;
                        sus++;
                } else {
                    rec++;
                }

                this.population[row][col].updateNextState();
            }
        }

        // Ask each cell to update its own state
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                this.population[row][col].updateCurrentState();
            }
        }

        // Update grid-level attributes related to recent changes in the cell population
        this.flux = flux;
        // Update the susceptible, infectious, recovered numbers
        this.sus = sus;
        this.inf = inf;
        this.rec = rec;
        }

    /**
     * Retrieve a copy of the population's current state fields, using a boolean 2D array to protect the cells
     * themselves from unwanted manipulation.
     * @return snapshot boolean[][], a grid of current states
     */
    public State[][] getPopulation(){
        SIRState[][] snapshot = new SIRState[this.size][this.size];
        for (int row=0; row<this.size; row++){
            for (int col=0; col<this.size; col++){
                snapshot[row][col] = this.population[row][col].getCurrentState();
            }
        }
        return snapshot;
    }

    /**
     * Create an array and add the current susceptible, infectious and recovered people number (stats) to it.
     * @return stats int[][], an int array for the stats
     */
    public int[] getDemographics(){

        int[] stats = new int[3];
        
        for (int i = 0; i < stats.length; i++) {
            stats[0] = sus;
            stats[1] = inf;
            stats[2] = rec;
        }

        return stats;
    }

    /**
     * Create an array and add the current susceptible, infectious and recovered people number (stats) to it.
     * @return stats int[][], an int array for the stats
     */
    public double[] getPercentages(){
        int[] demo = getDemographics();
        int popSize = getSize() * getSize();
        
        double s = (double) demo[ SIRState.SUSCEPTIBLE.ordinal() ] / popSize * 100;
        double i = (double) demo[ SIRState.INFECTIOUS.ordinal() ] / popSize * 100;
        double r = (double) demo[ SIRState.RECOVERED.ordinal() ] / popSize * 100;

        double[] percentages = {s, i, r};
    
        return percentages;
    }


    /**
     * Since a SIR object's job is to simulate the grid over time, the grid itself does not need
     * time information or a simulate() function. It only needs to update() itself.
     * ... But a "private" simulation-like behavior is still helpful for testing the update() method.
     
    // public void simulate( boolean verbose ) {
    private void testUpdate( ){
        System.out.println( "\nTEST UPDATE:\n");
        this.flux = this.size * this.size;
        int generation = 0;
        int maxDays = 10;
        while(generation < maxDays){
            update();
            generation++;
            System.out.println( "\nGENERATION: " + generation );
            System.out.println( this );
        }
    }

    /**
     * Test the MaskedCell class by running a small simulation.
     * @param args String[], command line arguments
     
    public static void main( String[] args ){
        double population_density = 0.33;
        SIRGrid testGrid = new SIRGrid( 10 );
        System.out.println( "\nNEWLY CONSTRUCTED GRID OBJECT:\n" + testGrid );
        //testGrid.setMaxGenerations( 10 );
        // testGrid.populate( population_density, 0.1, 0.1 );
        System.out.println( "\nINITIAL POPULATION:\n" + testGrid );
        // testGrid.testUpdate( );
        System.out.println( "\nFINAL POPULATION:\n" + testGrid );
        
    } */
}
