import java.util.Random;

/**
 *  Defines the cells used in SIR Model: object-oriented implementation <br>
 * <br>
 *  Can be run directly for testing purposes. The execution command below will:
 *  Create a 3x3 grid of cells, and progresses through 2 iterations of the game,
 *  demonstrating cell survival, birth, and death. <br>
 *   <br>
 *  Compilation:  javac SIRCell.java <br>
 *  Execution:    java SIRCell <br>
 * <br>
 *  @author Nisanur Genc, 2021
 *  Credit for help: Caitrin Eaton
 */
public class SIRCell implements Cell {

    // Possible states common to all Cells
    public final static int NEIGHBORHOOD_SIZE = 8;

    // Attributes particular to this Cell
    protected SIRState currentState;
    protected SIRState nextState;
    protected int totalNeighbors;
    protected int susceptibleNeighbors;
    protected SIRCell[] neighborhood;
    protected double recoveryRate;
    protected double infectionRate;

    /**
     * Construct a new cell for the SIR Model
     * @param initState SIRState, whether the cell is created infectious or susceptible
     */
    public SIRCell( SIRState initState, double recoveryRate, double infectionRate ) {
        this.currentState = initState;
        this.nextState = this.currentState;
        this.recoveryRate=recoveryRate;
        this.infectionRate=infectionRate;
        this.totalNeighbors = 0;
        this.susceptibleNeighbors = 0;
        this.neighborhood = new SIRCell[ NEIGHBORHOOD_SIZE ]; // references are initially null
    }

    /**
     * Accessor for a cell's current state
     * @return currentState SIRState, whether the cell is currently infectious, recovered or susceptible
     */
    public SIRState getCurrentState(){
        return this.currentState;
    }

    /**
     * Accessor for a cell's next state
     * @return nextState SIRState, whether the cell is currently infectious, recovered or susceptible
     */
    public SIRState getNextState(){
        return this.nextState;
    }

    /**
     * Accessor for a cell's current state
     * @return susceptibleNeighbors int, how many neighbors the cell believes are currently susceptible
     */
    public int getSusceptibleNeighbors(){
        return this.susceptibleNeighbors;
    }

    /**
    * Accessor for the infection rate of a cell's neighbor
    * @return infectionRate double, what the infection rate for that cell's neigbor is
    */
    public double getInfectionRate(){
        return this.infectionRate;
    }

    /**
     * Allows the user to add another cell to this cell's neighborhood
     * @param cell SIRCell, a reference to a neighboring cell object
     */
    public void addNeighbor(Cell cell) {
        // Make sure there's room in the neighborhood
        if (this.totalNeighbors < NEIGHBORHOOD_SIZE) {
            // Add the new neighbor to the neighborhood
            this.neighborhood[totalNeighbors] = (SIRCell) cell;
            this.totalNeighbors++;
            // Keep the susceptible neighbors counter up to date
            if (cell.getCurrentState() == SIRState.SUSCEPTIBLE) {
                this.susceptibleNeighbors++;
            }
        } else{
            System.out.println("Warning: new neighbor not added; neighborhood full.");
        }
    }

    /**
     * Updates a cell's next state according to the rules of the SIR Model:
     *   -- A suspectible cell can either stay susceptible or get infectious depending on the infection rate
     *   -- An infectious cell will either recover or stay infectious depending on the recovery rate
     *   -- A recovered cell will not be able to get infectious again
     */
    public void updateNextState(){
        double randomNumber;
        Random randomNumberGenerator = new Random();
        // this.countSusceptibleNeighbors();
        if(this.currentState == SIRState.SUSCEPTIBLE) {
            for(int i = 0; i<this.totalNeighbors; i++ ){
                if(this.neighborhood[i].getCurrentState() == SIRState.INFECTIOUS){
                    randomNumber = randomNumberGenerator.nextDouble();
                    if(randomNumber < this.neighborhood[i].getInfectionRate()) {
                        this.nextState = SIRState.INFECTIOUS;
                    }
                }
            }
        } else if (this.currentState == SIRState.INFECTIOUS) {
            randomNumber = randomNumberGenerator.nextDouble();
            if(randomNumber < this.recoveryRate) {
                this.nextState = SIRState.RECOVERED;
            }
        }
    }

    /**
     * Updates a cell's current state using a next state previously determined by updateNextState()
     */
    public void updateCurrentState(){
        this.currentState = this.nextState;
    }

    /**
     * Iterates over the neighborhood, counting how many neighbors are currently alive.
     */
    private void countSusceptibleNeighbors(){
        int susceptible = 0;
        for(int i = 0; i<this.totalNeighbors; i++ ){
            if (this.neighborhood[i].getCurrentState() == SIRState.SUSCEPTIBLE ) {
                susceptible++;
            }
        }
        this.susceptibleNeighbors = susceptible;
    }

    //* Test that we can construct a Cell object, and take a look at references
     
    private static void testCell(){
        System.out.println( "\nTEST CELL" );
        SIRCell testCell = new SIRCell(SIRState.RECOVERED, 0.1, 0.2);
        System.out.println( "\nConstructed new " + testCell);
        System.out.println( "after definition, neighborhood = " + testCell.neighborhood );
        System.out.println( "neighborhood[0] = " + testCell.neighborhood[0] );
    } 

    /**
     * Test that Cells can be placed into a 2D structure, and take a closer look at references.
     * @return a 2D array of Cell objects whose Cell[] neighbors fields have not yet been populated
     */ 
    private static SIRCell[][] testGrid(){
        System.out.println( "\nTEST GRID" );
        SIRCell[][] testGrid = new SIRCell[3][3];
        SIRState initState = SIRState.RECOVERED;
        for( int row=0; row<3; row++){
            for( int col=0; col<3; col++){
                if (row%2==0 || col%2==1){
                    initState = SIRState.SUSCEPTIBLE;
                } else {
                    initState = SIRState.RECOVERED;
                }
                testGrid[row][col] = new SIRCell(initState, 0.1, 5.1);
            }
        }
        System.out.println( "\ntestGrid = " + testGrid);
        System.out.println( "testGrid[0] = " + testGrid[0] );
        System.out.println( "testGrid[0][0] = " + testGrid[0][0] );
        System.out.println( "testGrid[0][0].neighborhood = " + testGrid[0][0].neighborhood );
        System.out.println( "testGrid[0][0].neighborhood[0] = " + testGrid[0][0].neighborhood[0] );
        return testGrid;
    }


    /**
     * Test that Cells in a 2D grid structure can track their neighbros in the Cell[] neighbors field.
     * @param testGrid (Cell[][]) a 2D array of Cell objects whose Cell[] neighbors fields have not yet been populated
     */
    private static void testNeighbors( SIRCell[][] testGrid ) {
        System.out.println( "\nTEST NEIGHBORS" );
        // Populate each cell's Cell[] neighbors field
        for( int row=0; row<3; row++){
            for( int col=0; col<3; col++){
                for(int dr=-1; dr<2; dr++) {
                    for (int dc = -1; dc < 2; dc++){
                        if( dr!=0 || dc!=0 ){    // A cell shouldn't be its own neighbor.
                            if((row+dr>=0 && row+dr<3) && (col+dc>=0 && col+dc<3)) {    // Watch out for out of bounds exceptions.
                                // This neighbor is safe to add
                                testGrid[row][col].addNeighbor(testGrid[row + dr][col + dc]);
                            }
                        }
                    }
                }
            }
        }

        System.out.println( "\ntestGrid = " + testGrid);
        System.out.println( "testGrid[0] = " + testGrid[0] );
        System.out.println( "testGrid[0][0] = " + testGrid[0][0] );
        System.out.println( "testGrid[0][0].neighborhood = " + testGrid[0][0].neighborhood );
        System.out.println( "testGrid[0][0].neighborhood[0] = " + testGrid[0][0].neighborhood[0] );
    }

    /**
     * Test that cell update works properly, according to each cell object's neighbors and current state.
     *  @param testGrid (Cell[][]) a 2D array of Cell objects whose Cell[] neighbors fields have not yet been populated
     */
    private static void testUpdate( SIRCell[][] testGrid ){
        System.out.println( "\nTEST UPDATE" );

        // Initial State
        System.out.println("\nINITIAL STATE, T = 0");
        for( int row=0; row<3; row++){
            for( int col=0; col<3; col++){
                testGrid[row][col].updateCurrentState();
                System.out.print(testGrid[row][col].getCurrentState() + "    ");
            }
            System.out.println("");
        }

        // Simulate the next 2 days by updating twice
        for(int t=0; t<2; t++) {
            // Calculate each cell's next state based on current state + susceptible neigbors
            System.out.println("\nNEXT STATE, T = " + t);
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    testGrid[row][col].updateNextState();
                    System.out.print(testGrid[row][col].nextState + "    ");
                }
                System.out.println("");
            }
            // Update each cell's current state to match its previously determined next state
            System.out.println("\nCURRENT STATE, T = " + (t+1));
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    testGrid[row][col].updateCurrentState();
                    System.out.print(testGrid[row][col].getCurrentState() + "    ");
                }
                System.out.println("");
            }
        }
    }

    /**
     * Test the SIRCell class's functionality.
     * @param args String[], command line arguments
     */ 
    public static void main( String[] args ){
        testCell();
        SIRCell[][] testGrid = testGrid();
        testNeighbors( testGrid );
        testUpdate( testGrid );
    }
    
}

