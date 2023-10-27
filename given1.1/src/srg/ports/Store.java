package srg.ports;

import srg.exceptions.InsufficientResourcesException;
import srg.resources.FuelContainer;
import srg.resources.FuelGrade;
import srg.resources.ResourceContainer;
import srg.resources.ResourceType;
import srg.ship.CargoHold;
import srg.ship.RoomTier;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;

/**
 * Store class
 */
public class Store extends SpacePort {
    /**
     * Name of the Store
     */
    private String name;
    /**
     * Position of the Store
     */
    private Position position;
    /**
     * Average tier CargoHold of the store
     */
    private CargoHold cargoHold = new CargoHold(RoomTier.AVERAGE);
    /**
     * Initial resource
     * Hypercontainer in the store
     */
    private FuelContainer hyperContainer;
    /**
     * Initial resource
     * triContainer in the store
     */
    private FuelContainer triContainer;
    /**
     * Initial resource
     * repairContainer in the store
     */
    private ResourceContainer repairContainer;
    /**
     * List of the resource containers
     */
    private List<ResourceContainer> resourceContainersList;

    /**
     * Constructs Store object with its name and position
     * @param name The name of the store
     * @param position The position of the store
     */
    public Store(String name, Position position) {
        super(name, position);
        this.name = name;
        this.position = position;
        this.resourceContainersList = new ArrayList<>();

        this.hyperContainer = new FuelContainer(FuelGrade.HYPERDRIVE_CORE, 
                FuelContainer.MAXIMUM_CAPACITY);
        this.resourceContainersList.add(hyperContainer);

        this.triContainer = new FuelContainer(FuelGrade.TRITIUM, 
                FuelContainer.MAXIMUM_CAPACITY);
        this.resourceContainersList.add(triContainer);

        this.repairContainer = new ResourceContainer(ResourceType.REPAIR_KIT, 
                ResourceContainer.MAXIMUM_CAPACITY);
        this.resourceContainersList.add(repairContainer);

    }

    /**
     * Check if the store has the amount of the resource to purchase and if it does update the store status
     * @param item The item to purchase from the store
     * @param amount The amount of the item to purchase from the store
     * @return The container containing the amount of the resource to purchase from the store
     * @throws InsufficientResourcesException If the store does not have the enough amount of the resource
     * If the item is not in the store print "The specified resource does not exist." message
     */
    public ResourceContainer purchase(String item, int amount) 
            throws InsufficientResourcesException {
        if (resourceContainersList.isEmpty()) {
            throw new InsufficientResourcesException("Error Here");
        }
        if (item.equals("FUEL")) {
            throw new InsufficientResourcesException("The specified resource does not exist.");
        }
        if (item.equals("TRITIUM") || item.equals("REPAIR_KIT") || item.equals("HYPERDRIVE_CORE")) {
            List<Integer> indexArr = new ArrayList<>();
            for (int i = 0; i < resourceContainersList.size(); i++) {
                ResourceContainer container = resourceContainersList.get(i);
                if (container.getShortName().equals(item)) {
                    if (container.getAmount() < amount) {
                        amount -= container.getAmount();
                        indexArr.add(i);
                    } else if (container.getAmount() > amount) {
                        if (!indexArr.isEmpty()) {
                            for (int j : indexArr) {
                                resourceContainersList.remove(j);
                            }
                        }
                        resourceContainersList.get(i).setAmount(container.getAmount() - amount);
                        if (container instanceof FuelContainer) {
                            container.setAmount(amount);
                            return container;
                        } else {
                            return new ResourceContainer(container.getType(), amount);
                        }
                    } else {
                        if (!indexArr.isEmpty()) {
                            for (int j : indexArr) {
                                resourceContainersList.remove(j);
                            }
                        }
                        resourceContainersList.remove(container);
                        return container;
                    }
                }
            }
            if (amount > 0) {
                throw new InsufficientResourcesException();
            }
        } else {
            throw new InsufficientResourcesException("The specified resource does not exist.");
        }
        throw new InsufficientResourcesException("Error Here");
    }

    /**
     * Gets the actions that can be performed if this store
     * @return The list of the actions that can be performed in this store
     */
    @Override
    public List<String> getActions() {
        List<String> actionList = new ArrayList<>();
        String result = "";
        if (resourceContainersList.isEmpty()) {
            return actionList;
        }
        for (ResourceContainer r : resourceContainersList) {
            if (r.getShortName().equals("REPAIR_KIT")) {
                result = "buy " + r.getType() + " 1.." + r.getAmount();
                actionList.add(result);
            } else if (r instanceof FuelContainer) {
                FuelContainer f = (FuelContainer) r;
                result = "buy " + f.getFuelGrade() + " 1.." + f.getAmount();
                actionList.add(result);
            }
        }
        sort(actionList);
        return actionList;
    }

    /**
     * Gives basic information about this store
     * @return Basic information in the string type
     */
    @Override
    public String toString() {
        return "PORT: \"" + getName() + "\" Store at " + getPosition();
    }
}
