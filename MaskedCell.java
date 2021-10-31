import java.util.Random;

/**
 *  Defines the masked cells used in SIR Model: object-oriented implementation <br>
 * <br>
 *  Can be run directly for testing purposes. The execution command below will:
 *  Create a 3x3 grid of cells, and progresses through 2 iterations of the game,
 *  demonstrating cell being susceptible, infectious or recovered. <br>
 *   <br>
 *  Compilation:  javac MaskedCell.java <br>
 *  Execution:    java MaskedCell <br>
 * <br>
 *  @author Nisanur Genc, 2021
 *  Credit for help: Caitrin Eaton
 */
public class MaskedCell extends SIRCell {

    protected final double transmissionCoefficient = 0.30;
    protected final double contractionCoefficient = 0.80;

    /**
     * Construct a new cell for the SIR Model
     * @param initState SIRState, whether the cell is created infectious or susceptible
     */
    public MaskedCell( SIRState initState, double recoveryRate, double infectionRate ) {
        super(initState, recoveryRate, infectionRate);
        //this.neighborhood = new MaskedCell[ NEIGHBORHOOD_SIZE ]; // references are initially null
    }

    /**
    * Accessor for the infection rate of a cell's neighbor
    * @return infectionRate * transmissionCoefficient double, what the infection rate for that cell's neigbor is with the mask
    */
    public double getInfectionRate(){
        return this.infectionRate * transmissionCoefficient;
    }

    /**
     * Updates a cell's next state according to the rules of the SIR Model:
     *   -- A suspectible cell can either stay susceptible or get infectious depending on the infection rate with the contraction coefficient
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
                    if(randomNumber < (this.neighborhood[i].getInfectionRate()*contractionCoefficient) ) {
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
     * Test that Cells can be placed into a 2D structure, and take a closer look at references.
     * @return a 2D array of Cell objects whose Cell[] neighbors fields have not yet been populated
     */ 
    private static MaskedCell[][] testGrid(){
        System.out.println( "\nTEST GRID" );
        MaskedCell[][] testGrid = new MaskedCell[3][3];
        SIRState initState = SIRState.RECOVERED;
        for( int row=0; row<3; row++){
            for( int col=0; col<3; col++){
                if (row%2==0 || col%2==1){
                    initState = SIRState.SUSCEPTIBLE;
                } else {
                    initState = SIRState.RECOVERED;
                }
                testGrid[row][col] = new MaskedCell(initState, 0.1, 5.1);
            }
        }
        System.out.println( "\ntestGrid = " + testGrid);
        System.out.println( "testGrid[0] = " + testGrid[0] );
        System.out.println( "testGrid[0][0] = " + testGrid[0][0] );
        System.out.println( "testGrid[0][0].neighborhood = " + testGrid[0][0].neighborhood );
        System.out.println( "testGrid[0][0].neighborhood[0] = " + testGrid[0][0].neighborhood[0] );
        return null;
    }

}

