/**
 *  Defines an interface for a family of cell classes related to cellular automata. <br>
 *  <br>
 *  @author New College OOP, spring 2021
 */
public interface Cell {

    /** All Moore neighborhoods have 8 neighbors */
    public final int NEIGHBORHOOD_SIZE = 8;

    // Methods that all Cells must implement, regardless of what type of Cellular Automaton they represent

    /**
     * Add another cell to this cell's neighborhood
     * @param cell (Cell): a reference to a neighboring cell object
     */
    void addNeighbor( Cell cell );

    /**
     * Accessor for a cell's current state
     * @return currentState (e.g. LifeState.ALIVE or LifeState.DEAD, in the Game of Life)
     */
    State getCurrentState();

    /**
     * Accessor for a cell's next state
     * @return nextState (e.g. LifeState.ALIVE or LifeState.DEAD, in the Game of Life)
     */
    State getNextState();

    /**
     * Update a cell's current state to match its (previously decided) next state.
     */
    void updateCurrentState();

    /**
     * Apply the cellular automaton's rules in order to determine this cell's next state.
     */
    void updateNextState();
}
