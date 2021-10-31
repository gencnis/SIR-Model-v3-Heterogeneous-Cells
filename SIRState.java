/**
 * The possible states for individual cells in an SIR model.
 * Each SIRCell object must be in 1 of these states.  <br>
 * <br>
 * @author Nisanur Genc, 2021
 */
public enum SIRState implements State {
    SUSCEPTIBLE, INFECTIOUS, RECOVERED
}
