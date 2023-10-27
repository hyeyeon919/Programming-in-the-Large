package srg.ports;

import srg.ship.CargoHold;
import srg.ship.NavigationRoom;
import srg.ship.Room;

import java.util.*;

/**
 * ShipYard class
 */
public class ShipYard extends SpacePort {
    /**
     * Name of the Shipyard
     */
    private String name;
    /**
     * Position of the ShipYard
     */
    private Position position;
    /**
     * The List of the ports that can be upgraded
     */
    private List<String> canUpgrade;

    /**
     * Constructs shipyard object with the name, position, list of the ports that can be updated
     * @param name The name of the Shipyard
     * @param position The position of the Shipyard
     * @param canUpgrade The list of the ports that can be updated
     */
    public ShipYard(String name, Position position, List<String> canUpgrade) {
        super(name, position);
        this.name = name;
        this.position = position;
        this.canUpgrade = canUpgrade;
    }

    /**
     * Gets actions that can be performed at this spaceport
     * @return The list of the actions that can be performed at this spaceport in String type
     */
    public List<String> getActions() {
        List<String> actionList = new ArrayList<>();
        String result = "";
        for (String s : canUpgrade) {
            result = "upgrade " + s;
            actionList.add(result);
        }
        return actionList;
    }

    /**
     * Checks if the room can be upgraded and upgrades the room
     * @param room The room to get upgrade
     * @throws java.lang.IllegalArgumentException If the room is not in the list of the room that can be upgraded
     */
    public void upgrade(Room room) {
        if (room instanceof NavigationRoom) {
            if (canUpgrade.contains("NavigationRoom")) {
                room.upgrade();
            } else {
                throw new IllegalArgumentException("");
            }
        } else if (room instanceof CargoHold) {
            if (canUpgrade.contains("CargoHold")) {
                room.upgrade();
            } else {
                throw new IllegalArgumentException("");
            }
        } else if (room instanceof Room) {
            if (canUpgrade.contains("Room")) {
                room.upgrade();
            } else {
                throw new IllegalArgumentException("");
            }
        }
    }

    /**
     * Gives the basic information about this Shipyard
     * @return Basic information about this Shipyard in String type
     */
    @Override
    public String toString() {
        return "PORT: " + getName() + "Shipyard at" + getPosition();
    }
}
