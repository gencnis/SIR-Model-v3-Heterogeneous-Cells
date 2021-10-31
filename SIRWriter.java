import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 *  Defines a CSV writing tool useful for a Susceptible-Infectious-Recovered (SIR) epidemiological model
 *  of viral spread, in the style of a cellular automaton (CA). Uses the SIR model's characteristics
 *  (infectionRate, recoveryRate, maxDays, gridSize) to autogenerate descriptive filenames.<br>
 *  <br>
 *  Not meant to be executed directly. Run SIR.java to test.
 *  <br>
 *  Compilation:  javac SIRWriter.java <br>
 *  Execution:    N/A   <br>
 *  <br>
 *  @author Nisanur Genc, 2021
 *  Credit for help: Caitrin Eaton
 */
public class SIRWriter {

    BufferedWriter writer;  // stream for writing to the output CSV file
    String fileName;        // name of the output CSV file, defaults to: SIR_infectionRate*1000_recoveryRate*1000_maxDays_gridSize.csv
    Path outPath;           // path to the output CSV file
    String header;          // first row written to the output CSV file, explaining the name of each column

    /**
     * Fields are initialized in open(), when the SIR model is able to pass in its characteristics.
     */
    public SIRWriter(){
        writer = null;
        fileName = "";
        outPath = null;
        header = "";
    }

    /**
     * In case the user would like to dictate a specific file name.
     * @param fileName (String) a user-specified name for the output CSV file.
     */
    public SIRWriter( String fileName ){
        writer = null;
        this.fileName = fileName;
        outPath = null;
        header = "";
    }

    /**
     * Create an output stream for recording an SIR model's daily demographics.
     * @param maxDays (int) maximum allowed duration of the simulation, in days.
     * @param infectionRate (double) the daily probability that an infectious individual will infect each susceptible neighbor
     * @param recoveryRate (double) the daily probability that an infectious individual will recover
     * @param size (int) number of individuals along each side of the "city" city
     */
    public void open( int maxDays, double infectionRate, double recoveryRate, int size ){
        // Use the SIR model's fields to make the filename help users organize the results across trials
        if (fileName.length() == 0) {
            fileName = String.format("SIR_%03d_%03d_%03d_%03d.csv", (int) (infectionRate * 1000), (int) (recoveryRate * 1000), maxDays, size);
        }
        outPath = Paths.get( fileName );

        // Start the output file with a header line at the top, which names each column of data
        SIRState state;
        try {
            writer = Files.newBufferedWriter(outPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            header = "Day";
            for( int i=0; i<SIRState.values().length; i++){
                state = SIRState.values()[ i ];
                header += String.format(",%s (total),%s (%%)", state, state);
            }
            header += "\n";
            writer.write( header );
            System.out.println( "Output file name: " + fileName );
        } catch(IOException e) {
            writer = null;
            System.out.println( "WARNING: SIRWriter unable to create output file." );
        }
    }

    /**
     * Record the day's results in the output file.
     * @param day (int) the current day in the simulation
     * @param totals (int[]) number of cells in each CA State
     * @param percentages (double[]) percent of cells in each CA State
     */
    public void update( int day, int[] totals, double[] percentages ){
        if( writer != null ) {
            String stats = "" + day;
            for( int state = 0; state < SIRState.values().length; state++ ){
                stats += "," + totals[state] + "," + percentages[state];
            }
            stats += "\n";
            try {
                writer.write( stats );
            } catch(IOException e){
                System.out.println("WARNING: SIRWriter failed to record day " + day );
            }
        }
    }

    /**
     * Safely close the output file.
     */
    public void close(){
        if (writer != null ) {  // The file can only be closed if it was successfully opened
            try { writer.close(); }
            catch (IOException e) {
                System.out.println( "WARNING: SIRWriter Failed to close the output stream." );
            }
        }
    }
}
