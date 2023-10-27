package srg.resources;

/**
 * ResourceContainer class
 */
public class ResourceContainer {
    /**
     * The maximum capacity of the ResourceContainer
     */
    public static final int MAXIMUM_CAPACITY = 10;
    /**
     * The type of the ResourceContainer
     */
    private ResourceType type;
    /**
     * The amount of the ResourceContainer
     */
    private int amount;

    /**
     * Constructs ResourceContainer object with input amount of input type of resources
     * @param type The type of the resource in the container
     * @param amount The amount of the resource in the container
     * @throws java.lang.IllegalArgumentException If the type is FUEL and this constructor was not called from a constructor of the FuelContainer class
     */
    public ResourceContainer(ResourceType type, int amount)
            throws java.lang.IllegalArgumentException {
        this.type = type;
        this.amount = amount;
        if (!this.canStore(type) && (type.equals(ResourceType.FUEL))) {
            throw new java.lang.IllegalArgumentException("Error Here");
        }
    }

    /**
     * Defines if the input type can be stored in this container
     * @param type The type of the resource to be stored in the container
     * @return If the type is Fuel return false, and it is not return true
     */
    public boolean canStore(ResourceType type) {
        if (type.equals(ResourceType.FUEL)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get the amount of the resource in the container
     * @return The amount of the resource in the container
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Set the amount of the resource in the container
     * @param amount The amount of resource to set in the container
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Gets the type of the resource Container
     * @return The type of the resource Container
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * Gives information about the resource container
     * @return The information about the resource container in String type
     */
    @Override
    public String toString() {
        return getType() + ": " + getAmount();
    }

    /**
     * Gets the short name of the container type
     * @return The short name of the container type
     */
    public String getShortName() {
        return getType().name();
    }
}
