package srg.ship;

import srg.exceptions.InsufficientCapcaityException;
import srg.resources.FuelContainer;
import srg.resources.FuelGrade;
import srg.resources.ResourceContainer;
import srg.resources.ResourceType;

import srg.exceptions.InsufficientResourcesException;
import java.lang.IllegalArgumentException;
import java.util.*;

/**
 * CargoHold Class
 */
public class CargoHold extends Room {
    /**
     * Tier of the cargoHold
     */
    private RoomTier roomTier;
    /**
     * The list of the resourceContainer
     */
    private List<ResourceContainer> resourceContainersList = new ArrayList<>();

    /**
     * Constructs cargoHold of the input tier
     * @param roomTier Tier of the cargoHold
     */
    public CargoHold(RoomTier roomTier) {
        super(roomTier);
        this.roomTier = roomTier;
    }

    /**
     * Calculate the remaining capacity of the cargoHold by calculating the difference between the maximum capacity and the number of the containers in the cargoHold
     * @return The number of the containers that can be added in the current cargoHold
     */
    public int getRemainingCapacity() {
        return getMaximumCapacity() - getResources().size();
    }

    /**
     * Gives the maximum capacity of the cargoHold which differs by the tier of the cargoHold
     * @return The number of the containers that can be held in the cargoHold by tier
     */
    public int getMaximumCapacity() {
        if (getTier().equals(RoomTier.BASIC)) {
            return 5;
        }
        if (getTier().equals(RoomTier.AVERAGE)) {
            return 10;
        } else {
            return 15;
        }
    }

    /**
     * Gets the list of the containers in the cargoHold
     * @return List of the containers in the cargoHold
     */
    public List<ResourceContainer> getResources() {
        return this.resourceContainersList;
    }

    /**
     * Stores new container in the cargoHold
     * @param resource The container to add in the cargoHold
     * @throws InsufficientCapcaityException If the cargoHold is currently full to store a new container
     */
    public void storeResource(ResourceContainer resource) throws InsufficientCapcaityException {
        if (getRemainingCapacity() > 0) {
            getResources().add(resource);
        } else {
            throw new InsufficientCapcaityException("Error Here");
        }
    }

    /**
     * Generates a list of the Resource containers which is input type
     * @param type The type of the Resource container to gather
     * @return The list of the Resource containers which is input type
     */
    public List<ResourceContainer> getResourceByType(ResourceType type) {
        List<ResourceContainer> getResourceByTypeList = new ArrayList<>();
        for (ResourceContainer r : this.getResources()) {
            if (r.getType().equals(type)) {
                getResourceByTypeList.add(r);
            }
        }
        return getResourceByTypeList;
    }

    /**
     * Generates a list of the Fuel containers which is input grade
     * @param grade The grade of the Fuel containers to gather
     * @return The list of the Fuel containers which is input grade
     */
    public List<ResourceContainer> getResourceByType(FuelGrade grade) {
        List<ResourceContainer> getFuelResourceByTypeList = new ArrayList<>();
        for (ResourceContainer r : getResources()) {
            if (r instanceof FuelContainer) {
                FuelContainer f = (FuelContainer) (r);
                if (f.getFuelGrade().equals(grade)) {
                    getFuelResourceByTypeList.add(r);
                }
            }
        }
        return getFuelResourceByTypeList;
    }

    /**
     * Calculates the total amount of the particular resource that can be used from the cargoHold
     * @param type The type of the resource to calculate total amount
     * @return Total amount of the input type resource in cargoHold
     */
    public int getTotalAmountByType(ResourceType type) {
        int sumAmountResourceType = 0;
        for (ResourceContainer r : this.getResources()) {
            if (r.getType().equals(type)) {
                sumAmountResourceType += r.getAmount();
            }
        }
        return sumAmountResourceType;
    }

    /**
     * Calculates the total amount of the particular Fuel resource that can be used from the cargoHold
     * @param grade The grade of the Fuel resource to calculate total amount
     * @return Total amount of the input grade resource in cargoHold
     */
    public int getTotalAmountByType(FuelGrade grade) {
        int sumAmountFuelType = 0;
        for (ResourceContainer r : this.getResources()) {
            if (r instanceof FuelContainer) {
                FuelContainer f = (FuelContainer) (r);
                if (f.getFuelGrade().equals(grade)) {
                    sumAmountFuelType += f.getAmount();
                }
            }
        }
        return sumAmountFuelType;
    }

    /**
     * Consumes the amount of the input type resource from the cargoHold and update the cargoHold to the status after consuming the resource
     * @param type The type of the resource to consume from the cargoHold
     * @param amount The amount of the resource to consume from the cargoHold
     * @throws InsufficientResourcesException If the amount is greater than the total amount of the resource of the input type in the cargoHold
     * @throws IllegalArgumentException If the type of the resource is Fuel
     */
    public void consumeResource(ResourceType type, int amount)
            throws InsufficientResourcesException {
        if (type.equals(ResourceType.FUEL)) {
            throw new IllegalArgumentException();
        }
        if (this.getTotalAmountByType(type) < amount) {
            throw new InsufficientResourcesException("Error Here");
        } else {
            int consumeResourceAmount = amount;
            List<ResourceContainer> consumeResourceList = this.getResourceByType(type);
            for (ResourceContainer container : consumeResourceList) {
                int containerAmount = container.getAmount();
                if (containerAmount > consumeResourceAmount) {
                    getResources().remove(container);
                    getResources().add(new ResourceContainer(type, containerAmount
                            - consumeResourceAmount));
                    consumeResourceAmount -= containerAmount;
                    break;
                }
                if (containerAmount < consumeResourceAmount) {
                    getResources().remove(container);
                    consumeResourceAmount -= containerAmount;
                } else {
                    getResources().remove(container);
                    consumeResourceAmount -= containerAmount;
                    break;
                }
            }
            if (consumeResourceAmount > 0) {
                throw new InsufficientResourcesException("Error Here");
            }
        }
    }

    /**
     * Consumes the amount of the input grade Fuel resource from the cargoHold and update the cargoHold to the status after consuming the fule resource
     * @param grade The grade of the fuel resource to consume from the cargoHold
     * @param amount The amount of the fuel resource to consume from the cargoHold
     * @throws InsufficientResourcesException If the amount is greater than the total amount of the fuel resource of the input grade in the cargoHold
     */
    public void consumeResource(FuelGrade grade, int amount) throws InsufficientResourcesException {
        if (getTotalAmountByType(grade) < amount) {
            throw new InsufficientResourcesException("Error Here");
        } else {
            int consumeResourceAmount = amount;
            List<ResourceContainer> consumeResourceList = this.getResourceByType(grade);
            for (int i = 0; i < consumeResourceList.size(); i++) {
                ResourceContainer container = consumeResourceList.get(i);
                int containerAmount = container.getAmount();
                if (containerAmount > consumeResourceAmount) {
                    getResources().remove(container);
                    getResources().add(new FuelContainer(grade, containerAmount
                            - consumeResourceAmount));
                    break;
                }
                if (containerAmount < consumeResourceAmount) {
                    getResources().remove(container);
                    consumeResourceAmount -= containerAmount;
                } else {
                    getResources().remove(container);
                    break;
                }

            }
        }
    }

    /**
     * Returns basic information about cargoHold object
     * @return Basic information about cargoHold and resources in the cargoHold in String type
     */
    @Override
    public String toString() {
        String result =  "ROOM: CargoHold(" + this.getTier() + ") health: "
                + this.getHealth() + "%, needs repair: "
                + this.needsRepair() + ", capacity: " + getMaximumCapacity()
                + ", items: " + resourceContainersList.size();
        for (ResourceContainer r : this.getResources()) {
            result += "\n    " + (r).toString();
        }
        return result;
    }

    /**
     * Get the list of the actions that can be performed from this cargoHold
     * @return List of the actions that can be performed from this cargoHold
     */
    @Override
    public List<String> getActions() {
        List<String> actionList = new ArrayList<>();
        for (ResourceContainer r : resourceContainersList) {
            if (r.getType().equals(ResourceType.REPAIR_KIT)) {
                actionList.add("repair NavigationRoom [COST: 1 REPAIR_KIT]");
                actionList.add("repair CargoHold [COST: 1 REPAIR_KIT]");
            }
        }
        return actionList;
    }
}
