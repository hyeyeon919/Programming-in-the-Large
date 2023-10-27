package srg.resources;

/**
 * FuelContainer class which is a child class of the ResourceContainer
 */
public class FuelContainer extends ResourceContainer {

    /**
     * Final variable of the maximum capacity of the FuelContainer
     */
    public static final int MAXIMUM_CAPACITY = 1000;
    /**
     * The grade of the Fuel
     */
    private FuelGrade grade;
    /**
     * The amount of the Fuel
     */
    private int amount;

    /**
     * Constructs fuel container with fuel resource of the input grade and amount
     * @param grade The grade of the fuel resource in the fuel container
     * @param amount The amount of the fuel resource in the fuel container
     */
    public FuelContainer(FuelGrade grade, int amount) {
        super(ResourceType.FUEL, amount);
        this.grade = grade;
        this.amount = amount;
    }

    /**
     * Gets the fuel grade of the fuel container
     * @return The grade of the fuel container
     */
    public FuelGrade getFuelGrade() {
        return this.grade;
    }

    /**
     * Defines if the resource of the input type can be stored in this fuel container
     * @param type The type of the resource to be stored in the container
     * @return If the input type is FUEL type returns true, and if not returns false
     */
    @Override
    public boolean canStore(ResourceType type) {
        if (type.equals(ResourceType.FUEL)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gives basic information about this fuel container
     * @return Basic information about this fuel container in String type
     */
    @Override
    public String toString() {
        return getType() + ": " + getAmount() + " - " + getFuelGrade();
    }

    /**
     * Gets short name of this fuel container grade
     * @return short name of this fuel container grade
     */
    @Override
    public String getShortName() {
        return getFuelGrade().name();
    }
}
