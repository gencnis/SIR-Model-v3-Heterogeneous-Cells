/**
 *  Defines an interface for a family of cellular automata classes. Each CellularAutomaton should have a Grid of Cells.
 *  Each Cell should have a current State and a next State defined by an enum which represents its cellular automaton's
 *  rules. <br>
 *  <br>
 *  @author New College OOP, spring 2021
 */
public interface CellularAutomaton {
    /** Update the Grid and Vis with a single update step (e.g. 1 generation in the Game of Life) */
    void update();

    /** Update the Grid and Vis in a loop */
    void simulate();
}
