package lms.grid;

import java.util.HashMap;
import java.util.Map;

/**
 * The GameGrid is responsible for managing the state and initialisation of the game's grid.
 * It provides the Map structure to hold the coordinates of each node in the grid. It also
 * maintains the size of the grid using a range variable. The range value donates how many
 * nodes each hexagonal grid node extends to.
 *
 * @version 1.0
 * <p>
 * Summary: Initializes a grid of the game.
 * @ass2
 */

public class GameGrid {
    /**
     * size of grid
     */
    private int range;

    /**
     * Map of grid that contains Coordinate and GridComponent
     */
    private Map<Coordinate, GridComponent> grid;

    /**
     * Helper method:
     * Generates a grid with the given range, starting from the origin (the centre) and maintaining a
     * balanced shape for the entire mapping structure.
     * This has been provided to support you with the hexagonal coordinate logic.
     *
     * @param range The range of the map.
     * @provided
     */
    public GameGrid(int range) {
        this.range = range;
        this.grid = generate(range);
    }

    /**
     * Get a copy of the grid of the game.
     *
     * @return Map A copy of the grid of the game.
     */
    public Map<Coordinate, GridComponent> getGrid() {
        Map<Coordinate, GridComponent> copyGrid = new HashMap<>();

        for (Map.Entry<Coordinate, GridComponent> entry : grid.entrySet()) {
            Coordinate coordinate = entry.getKey();
            GridComponent component = entry.getValue();

            copyGrid.put(coordinate, component);
        }

        return copyGrid;
    }

    /**
     * Get the range of the grid that was stored when the GameGrid instance was constructed.
     *
     * @return int The range of the grid.
     */
    public int getRange() {
        return this.range;
    }

    /**
     * Set the GridComponent at the given coordinate.
     *
     * @param coordinate The coordinate of the GridComponent.
     * @param component  The GridComponent to be set.
     */
    public void setCoordinate(Coordinate coordinate, GridComponent component) {
        this.grid.put(coordinate, component);
    }

    private Map<Coordinate, GridComponent> generate(int range) {
        Map<Coordinate, GridComponent> tempGrid = new HashMap<>();
        for (int q = -range; q <= range; q++) { // From negative to positive (inclusive)
            for (int r = -range; r <= range; r++) { // From negative to positive (inclusive)
                for (int s = -range; s <= range; s++) { // From negative to positive (inclusive)
                    if (q + r + s == 0) {
                        // Useful to default to error
                        tempGrid.put(new Coordinate(q, r, s), () -> "ERROR");
                    }
                }
            }
        }
        return tempGrid;
    }
}
