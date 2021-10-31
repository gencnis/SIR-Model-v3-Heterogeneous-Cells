import java.awt.*;
import javax.swing.*;

/**
 *  Defines a visualization tool useful for a Susceptible-Infectious-Recovered (SIR) epidemiological model
 *  of viral spread, in the style of a cellular automaton (CA). <br>
 *  <br>
 *  Can be run directly for testing purposes. The execution command below will:
 *  Create a 10x10 grid of cells, and progresses through 7 days of the SIR model, demonstrating infection and recovery. <br>
 *  <br>
 *  Compilation:  javac SIRVis.java <br>
 *  Execution:    java SIRVis   <br>
 *  <br>
 *  @author Nisanur Genc, 2021
 *  Credit for help: Caitrin Eaton
 */
public class SIRVis {

    // Dimensions
    private final int windowSize = 600;   //  number of pixels along each side of the square window
    private final int cellSize;           //  number of pixels along each side of each square cell within the window
    private final int gridSize;           //  number of cells along each side of the grid
    private long frameTime = 500;         //  duration of each fram (day) in milliseconds

    // Graphics components
    private JFrame window;                 //  the window in which the model's animation is displayed
    private JPanel[][] panel;              //  the grid of rectangles used to visualize all of the cells

    // SIR components
    private SIRGrid grid;                 //  the grid of references to the actual cell objects themselves
    private SIRState[][] state;           //  a grid in which each cell's state is represented as an enumerated value

    // Cell color code: Susceptible (white), Infectious (green), Recovered (black), Invalid state (light gray)
    private final static Color[] colorCode = { Color.WHITE, Color.RED, Color.GREEN, Color.LIGHT_GRAY};
    private final static Color GRIDLINE = Color.LIGHT_GRAY;

    /**
     * SIRVis constructor, creates an animated visualization for the given grid of cells. <br>
     * @param world SIRGrid, the 2D collection of cells that defines this particular SIR cellular automaton
     */
    public SIRVis( SIRGrid world ) {
        // Determine the dimensions of components that will be visualized
        this.grid = world;
        this.gridSize = world.getSize();
        this.cellSize = this.windowSize / this.gridSize;

        // Configure the graphics window
        this.window = new JFrame();
        this.panel = new JPanel[ this.gridSize ][ this.gridSize ];
        initWindow();
    }

    /**
     * Set the window's title string for the current frame of the animation
     * @param day (int) current day of the animation
     */
    private void setTitle( int day ){
        
        int[] demo = grid.getDemographics(); // gets the demographics as an integer array
        int popSize = grid.getSize() * grid.getSize(); // gets the population size
        
        double s = (double) demo[ SIRState.SUSCEPTIBLE.ordinal() ] / popSize * 100; // figures out about the percentage of susceptibles
        double i = (double) demo[ SIRState.INFECTIOUS.ordinal() ] / popSize * 100; // figures out about the percentage of susceptibles
        double r = (double) demo[ SIRState.RECOVERED.ordinal() ] / popSize * 100; // figures out about the percentage of susceptibles
        
        String title = String.format("Day %d, S: %.2f%%, I: %.2f%%, R: %.2f%%", day, s, i, r);

        this.window.setTitle( title );
    }

    /**
     * Initialize the graphics window and its contents.
     */
    private void initWindow() {

        // Configure the window itself
        window.setSize( windowSize, windowSize );

        // Configure the window's title
        setTitle( 0 );

        // Configure the collection of rectangles that visualize all the cells
        window.setLayout( new GridLayout(gridSize, gridSize) );
        state = (SIRState[][]) grid.getPopulation();
        Color c;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++ ){
                panel[row][col] = new JPanel();
                panel[row][col].setBorder(BorderFactory.createLineBorder( GRIDLINE ));
                window.add( panel[row][col] );
                if ( state[row][col] != null ) {
                    c = colorCode[ state[row][col].ordinal() ];
                } else {
                    c = colorCode[ SIRState.values().length ];  // Unrecognized state! Highlight in red.
                }
                panel[row][col].setBackground( c );
            }
        }

        // Make the graphics window visible
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        long initial_delay_ms = 2000;
        window.repaint( initial_delay_ms );

        // Hold the initial population steady for 2 seconds before starting the simulation
        try {
            Thread.sleep(initial_delay_ms);
        } catch (InterruptedException e) { }
    }

    /**
     * Control the speed of the animation. <br>
     * @param milliseconds long, the duration of a single frame, in milliseconds
     */
    public void setFrameTime(long milliseconds ){
        this.frameTime = milliseconds;
    }

    /**
     * Update the graphical representation of the grid to reflect the grid's current state.
     * @param day (int) current day in the simulation
     */
    public void update( int day ) {
        // Update the current state map
        this.state = (SIRState[][]) this.grid.getPopulation();

        // Use the window's title to track population demographics
        setTitle( day );

        // Configure the color of each cell according to its state: occupied or unoccupied
        Color c;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++ ){
                if (state[row][col] != null ) {
                    c = colorCode[ state[row][col].ordinal() ];
                } else {
                    c = colorCode[ SIRState.values().length ];  // Unrecognized state! Highlight in red.
                }
                panel[row][col].setBackground( c );
            }
        }
        window.repaint( frameTime );
        try {
            Thread.sleep( frameTime );
        } catch (InterruptedException e) { }
    }

    /**
     * Test the SIRVis class by simulating a small population over the course of 1 week. <br>
     * @param args String[] command line arguments
     */
    public static void main(String[] args) {

        // Initialize a SIRGrid object to drive the game mechanics
        double percentInfected = 0.10;
        int gridSize = 10;
        int maxDays = 7;
        SIRGrid testGrid = new SIRGrid( gridSize );
        testGrid.populate( percentInfected, 0.30, 0.40 );

        // Initialize a SIRVis object to show the user what's going on in the game
        SIRVis testVis = new SIRVis( testGrid );
        testVis.setFrameTime( 1000 );

        // Run the simulation until either no infectious cells remain or the time limit has been reached
        int day = 0;
        while( day < 7 ){
            testGrid.update();
            System.out.println( testGrid );
            testVis.update( day );
            day++;
        }
    }
}
