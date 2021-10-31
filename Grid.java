/**
 *  Defines an interface for a family of grid classes related to cellular automata. <br>
 *  <br>
 *  @author New College OOP, spring 2021
 */
public interface Grid {

    /**
     * Accessor for grid size.
     * @return size (int) the number of cells along each side of a square grid.
     */
    int getSize();

    /**
     * Accessor for a copy of all of the cells' current states. Returns a grid of States rather than the grid of cells
     * in order to avoid exposing references to the cells themselves, while still providing up-to-date information about
     * the cellular automaton's current state.
     * @return the set of all cells' current states (State[][])
     */
    State[][] getPopulation();

    /**
     * Ask each cell within the grid to update itself
     */
    void update();  // used to be named step()

    // void populate( );   // no parameters?
    // private void formNeighborhoods();
    //String toString();

}
