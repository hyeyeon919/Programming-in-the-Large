package srg.ports;

/**
 * Position class
 */
public class Position {
    /**
     * x coordinate of the position
     */
    public final int x;
    /**
     * y coordinate of the position
     */
    public final int y;
    /**
     * z coordinate of the position
     */
    public final int z;

    /**
     * Constructs position object with x, y, z coordinates
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

    }

    /**
     * Calculates the difference between the input position and this position
     * @param other The position to calculate with this position
     * @return The distance between this position and input position
     */
    public int distanceTo(Position other) {
        int distance = (int) Math.floor(Math.pow(Math.pow(other.x - x, 2)
                + Math.pow(other.y - y, 2) + Math.pow(other.z - z, 2), 0.5));
        return distance;
    }

    /**
     * Display the position information
     * @return The information of the position in String type
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
