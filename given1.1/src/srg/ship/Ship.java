package srg.ship;

import srg.cli.given.IO;
import srg.cli.given.PurchaseCommand;
import srg.cli.given.ShipCommand;
import srg.exceptions.InsufficientCapcaityException;
import srg.exceptions.InsufficientResourcesException;
import srg.exceptions.NoPathException;
import srg.resources.FuelContainer;
import srg.resources.FuelGrade;
import srg.resources.ResourceContainer;
import srg.resources.ResourceType;
import srg.ports.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a spaceship, which has a unique name, a unique ID, a registered owner, a CargoHold and a NavigationRoom.
 * @version 1.0
 * @ass1
 */
public class Ship {
    /**
     * The name of the ship
     */
    private String name;
    /**
     * The owner of the ship
     */
    private String owner;
    /**
     * The id of the ship
     */
    private String id;
    /**
     * The tier of the cargoHold
     */
    private RoomTier cargoHoldTier;
    /**
     * The tier of the navigation room
     */
    private RoomTier navigationRoomTier;
    /**
     * The object of the cargoHold
     */
    private CargoHold cargoHold;
    /**
     * The object of the navigation room
     */
    private NavigationRoom navigationRoom;

    /**
     * Construct a ship with inputs
     * @param name Name of the ship
     * @param owner Names of the owner
     * @param id Id of the ship
     * @param cargoHoldTier Tier of the cargoHold
     * @param navigationRoomTier Tier of the navigationRoom
     * @param galaxyMap Galaxy map that ship can use to navigate
     */
    public Ship(String name, String owner, String id, RoomTier cargoHoldTier,
                RoomTier navigationRoomTier, List<SpacePort> galaxyMap) {
        this.name = name;
        this.owner = owner;
        this.id = id;
        this.cargoHoldTier = cargoHoldTier;
        this.navigationRoomTier = navigationRoomTier;
        cargoHold = new CargoHold(cargoHoldTier);
        navigationRoom = new NavigationRoom(navigationRoomTier, galaxyMap);
        cargoHold.getResources().add(new ResourceContainer(ResourceType.REPAIR_KIT, 5));
        cargoHold.getResources().add(new FuelContainer(FuelGrade.TRITIUM, 100));
        cargoHold.getResources().add(new FuelContainer(FuelGrade.HYPERDRIVE_CORE, 5));
    }

    /**
     * Returns the room object which has the same name with the input
     * @param name Name of the room to get
     * @return Room object  by name input
     * @throws IllegalArgumentException If the input name is the invalid name
     */
    public Room getRoomByName(String name) throws IllegalArgumentException {
        if (name.equals("NavigationRoom")) {
            return navigationRoom;
        }
        if (name.equals("CargoHold")) {
            return cargoHold;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Prints out information about this ship
     * @return String holding information about the ship in particular format
     */
    public String toString() {
        return  "SHIP: " + name + " (" + id + ") owned by " + owner + "\n----\n" +
                cargoHold.toString() +
                "\n" + navigationRoom.toString();
    }

    /**
     * Returns the list of actions this ship can perform
     * @return List of actions of this ship can perform
     */
    public List<String> getActions() {
        List<String> actionList = new ArrayList<>();
        actionList.addAll(cargoHold.getActions());
        actionList.addAll(navigationRoom.getActions());
        return actionList;
    }

    /**
     * This method is provided as it interfaces with the command line interface.
     *
     * @param ioHandler Handles IO
     * @param command   A command to the ship
     */
    public void performCommand(IO ioHandler, ShipCommand command) {
        try {
            processCommand(ioHandler, command);
        } catch (InsufficientResourcesException error) {
            ioHandler.writeLn("Unable to perform action due to broken component or " +
                    "insufficient resources."
                    + System.lineSeparator() + error.getMessage());
        } catch (IllegalArgumentException | NoPathException | InsufficientCapcaityException error) {
            ioHandler.writeLn(error.getMessage());
        }
    }

    /**
     * This method is provided as it interfaces with the command line interface.
     * @param ioHandler Handles IO
     * @param command   A command to the ship
     * @throws InsufficientResourcesException
     *      If an action cannot be performed due to a lack or resources or a broken Room.
     * @throws NoPathException
     *      If a specified SpacePort cannot be found, or cannot be reached.
     * @throws InsufficientCapcaityException
     *      If resources cannot be added because there is not enough capacity in the CargoHold.
     */
    public void processCommand(IO ioHandler, ShipCommand command)
            throws InsufficientResourcesException, NoPathException,
            InsufficientCapcaityException {
        switch (command.type) {
            case SHOW_ROOM -> {
                ioHandler.writeLn(getRoomByName(command.value).toString());
            }
            case FLY_TO -> {
                navigationRoom.flyTo(command.value, cargoHold);
            }
            case JUMP_TO -> {
                navigationRoom.jumpTo(command.value, cargoHold);
            }
            case REPAIR_ROOM -> {
                // Ignore whether CargoHold may be broken
                cargoHold.consumeResource(ResourceType.REPAIR_KIT, 1);
                getRoomByName(command.value).resetHealth();

            }
            case UPGRADE_ROOM -> {
                ShipYard shipYard = navigationRoom.getShipYard();
                if (shipYard == null) {
                    ioHandler.writeLn("Can only upgrade when docked at a ShipYard.");
                    return;
                }

                shipYard.upgrade(getRoomByName(command.value));
            }
            case PURCHASE_ITEM -> {
                PurchaseCommand purchaseCommand = (PurchaseCommand) command;
                Store store = navigationRoom.getStore();
                if (store == null) {
                    ioHandler.writeLn("Can only purchase items at a Store.");
                    return;
                }
                ResourceContainer resource = store.purchase(purchaseCommand.item,
                        purchaseCommand.amount);
                //print test
                cargoHold.storeResource(resource);
                //print test
            }
            case SHOW_PORT -> {
                ioHandler.writeLn(navigationRoom.getCurrentPort().toString());
                ioHandler.writeLn(String.join(System.lineSeparator(),
                        navigationRoom.getCurrentPort().getActions()));
            }
            case SHOW_ACTIONS -> {
                ioHandler.writeLn(String.join(System.lineSeparator(), getActions()));
            }
        }

    }
}
