package srg.ports;

import java.util.*;

/**
 * SpacePort class
 */
public class SpacePort {
    /**
     * Name of the SpacePort
     */
    private String name;
    /**
     * Position of the Spaceport
     */
    private Position position;

    /**
     * Constructs SpacePort object with the name of the spaceport and the position of the spaceport
     * @param name The name of the spaceport
     * @param position The position of the spaceport
     */
    public SpacePort(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    /**
     * Gets the name of the spaceport
     * @return The name of the spaceport
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the position of the Spaceport
     * @return The position of the spaceport
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Gives the basic information about this spaceport
     * @return Basic information about this spaceport in String type
     */
    @Override
    public String toString() {
        return "PORT: \"" + getName() + "\" SpacePort at " + getPosition();
    }

    /**
     * Gets actions that can be performed in this spaceport
     * @return The list of the actions that can be performed only in this spaceport
     */
    public List<String> getActions() {
        List<String> actionList = new ArrayList<>();
        return actionList;
    }
}
