package srg.ship;

import srg.exceptions.NoPathException;
import srg.ports.ShipYard;
import srg.ports.SpacePort;
import srg.ports.Store;
import srg.resources.FuelGrade;

import srg.exceptions.InsufficientResourcesException;
import java.util.*;

/**
 * NavigationRoom Class
 */
public class NavigationRoom extends Room implements Damageable {
    /**
     * Tier of the NavigationRoom
     */
    private RoomTier roomTier;
    /**
     * GalaxyMap of the Navigation
     */
    public List<SpacePort> galaxyMap;
    /**
     * Current port of the ship
     */
    private SpacePort currentPort;

    /**
     * Constructs NavigationRoom with tier and galaxMap
     * @param roomTier Room tier of the Navigation Room
     * @param galaxyMap Galaxy map to navigate
     */
    public NavigationRoom(RoomTier roomTier, List<SpacePort> galaxyMap) {
        super(roomTier);
        this.roomTier = roomTier;
        this.galaxyMap = galaxyMap;
        this.currentPort = galaxyMap.get(0);
    }

    /**
     * Returns spaceport object of the current port
     * @return spaceport object of the current port
     */
    public SpacePort getCurrentPort() {
        return this.currentPort;
    }

    /**
     * Calculates the distance from other ports and compares with the maximum fly distance to find if the port is able to fly from the current port.
     * @return List of the ports which is in the fly range
     */
    public List<SpacePort> getPortsInFlyRange() {
        List<SpacePort> portsInFlyRange = new ArrayList<>();
        for (SpacePort s : galaxyMap) {
            if (getCurrentPort().getPosition().distanceTo(s.getPosition())
                    <= getMaximumFlyDistance() && !s.equals(getCurrentPort())) {
                portsInFlyRange.add(s);
            }
        }
        return portsInFlyRange;
    }

    /**
     * Returns the maximum fly distance by the tier of the navigation Room
     * @return Maximum distance of the ship for flying
     */
    public int getMaximumFlyDistance() {
        int maxFlyDistance = 0;
        switch (getTier()) {
            case BASIC:
                maxFlyDistance = 200;
                break;
            case AVERAGE:
                maxFlyDistance = 400;
                break;
            case PRIME:
                maxFlyDistance = 600;
                break;
        }
        return maxFlyDistance;
    }


    /**
     * Calculates the distance from other ports and compares with the maximum jump distance to find if the port is able to jump from the current port.
     * @return List of the ports which is in the jump range
     */
    public List<SpacePort> getPortsInJumpRange() {
        List<SpacePort> portsInJumpRange = new ArrayList<>();
        for (SpacePort s : galaxyMap) {
            int amountFuel = getFuelNeeded(s);
            if (amountFuel <= getMaximumJumpDistance() && amountFuel > getMaximumFlyDistance()) {
                portsInJumpRange.add(s);
            }
        }
        return portsInJumpRange;
    }

    /**
     * Returns the maximum jump distance by the tier of the navigation Room
     * @return Maximum distance of the ship for jumping
     */
    public int getMaximumJumpDistance() {
        int maxJumpDistance = 0;
        switch (getTier()) {
            case BASIC:
                maxJumpDistance = 500;
                break;
            case AVERAGE:
                maxJumpDistance = 750;
                break;
            case PRIME:
                maxJumpDistance = 1000;
                break;
        }
        return maxJumpDistance;
    }

    /**
     * Returns the actions that can be performed in the navigation room
     * @return List of the actions that can be performed is this navigation room
     */
    @Override
    public List<String> getActions() {
        List<String> actionList = new ArrayList<>();
        String result = "";
        int amountFuel = 0;
        for (SpacePort s : getPortsInFlyRange()) {
            amountFuel = getFuelNeeded(s);
            result = "fly to \"" + s.getName() + "\": PORT: \"" + s.getName()
                    + "\" " + s.getClass().getSimpleName() + " at " + s.getPosition()
                    + " [COST: " + amountFuel + " TRITIUM FUEL]";
            actionList.add(result);
        }
        for (SpacePort s : getPortsInJumpRange()) {
            result = "jump to \"" + s.getName() + "\" [COST: 1 HYPERDRIVE CORE]";
            actionList.add(result);
        }

        return actionList;
    }

    /**
     * Calculates the distance between current port and the input port to calculate the fuel needed
     * @param port the port to compare with the current port
     * @return the fuel needed to fly between the two ports which is same as the distance between the two ports
     */
    public int getFuelNeeded(SpacePort port) {
        return getCurrentPort().getPosition().distanceTo(port.getPosition());
    }

    /**
     * Give information about whether the current port is a shipyard or not
     * @return If the current port is shipyard it returns the current port as shipyard object,
     *         If the current port is not shipyard it returns null.
     */
    public ShipYard getShipYard() {
        if (getCurrentPort() instanceof ShipYard) {
            return (ShipYard) getCurrentPort();
        } else {
            return null;
        }
    }

    /**
     * Give information about whether the current port is a store or not
     * @return If the current port is store it returns the current port as store object,
     *         If the current port is not store it returns null.
     */
    public Store getStore() {
        if (getCurrentPort() instanceof Store) {
            return (Store) getCurrentPort();
        } else {
            return null;
        }
    }

    /**
     * Moves the ship to the destination, applies damage to rooms, consumes the required resource from the cargoHold when it is possible.
     * @param portName The port name of the destination
     * @param cargoHold The cargoHold object which contains the resources
     * @throws InsufficientResourcesException If there is not enough resources in the cargoHold or navigation room/cargoHold is broken.
     * @throws NoPathException If the named SpacePort cannot be found in the list of the ports which can fly from the current port
     */
    public void flyTo(String portName, CargoHold cargoHold) throws
            InsufficientResourcesException, NoPathException {
        int count = 0;
        for (SpacePort s : getPortsInFlyRange()) {
            if (s.equals(getSpacePortFromName(portName))) {
                if (cargoHold.isBroken() || this.isBroken()
                        || (cargoHold.getTotalAmountByType(FuelGrade.TRITIUM))
                        < getFuelNeeded(s)) {
                    throw new InsufficientResourcesException("Error Here");
                } else {
                    cargoHold.consumeResource(FuelGrade.TRITIUM, getFuelNeeded(s));
                    currentPort = s;
                    cargoHold.damage();
                    this.damage();
                }
                break;
            }
            count++;
        }
        if (count == getPortsInFlyRange().size()) {
            throw new NoPathException("Error Here");
        }
    }

    /**
     * Moves the ship to the destination, applies damage to rooms, consumes the required resource from the cargoHold when it is possible.
     * @param portName The port name of the destination
     * @param cargoHold The cargoHold object which contains the resources
     * @throws InsufficientResourcesException If there is not enough resources in the cargoHold or navigation room/cargoHold is broken.
     * @throws NoPathException If the named SpacePort cannot be found in the list of the ports which can jump from the current port
     */
    public void jumpTo(String portName, CargoHold cargoHold) throws
            InsufficientResourcesException, NoPathException  {
        int count = 0;
        for (SpacePort s : getPortsInJumpRange()) {
            if (s.equals(getSpacePortFromName(portName))) {
                if (cargoHold.isBroken() || this.isBroken()
                        || (cargoHold.getTotalAmountByType(FuelGrade.HYPERDRIVE_CORE)) <= 0) {
                    throw new InsufficientResourcesException("Error Here");
                } else {
                    cargoHold.consumeResource(FuelGrade.HYPERDRIVE_CORE, 1);
                    currentPort = s;
                    cargoHold.damage();
                    this.damage();
                }
                break;
            }
            count++;
        }
        if (count == getPortsInJumpRange().size()) {
            throw new NoPathException("Error Here");
        }
    }

    /**
     * Finds the port object with the String type name
     * @param name The name of the port
     * @return The Spaceport which has the same name with the input
     * @throws NoPathException If there is no Spaceport that matches the input name
     */
    public SpacePort getSpacePortFromName(String name) throws NoPathException {
        for (SpacePort s : galaxyMap) {
            if (name.equals(s.getName())) {
                return s;
            }
        }
        throw new NoPathException("Error Here");
    }

    /**
     * Returns basic information about this Navigation Room in String type
     * @return the information of this Navigation Room in String type
     */
    @Override
    public String toString() {
        return "ROOM: NavigationRoom(" + getTier()
                + ") health: " + getHealth() + "%, needs repair: " + needsRepair();
    }
}
