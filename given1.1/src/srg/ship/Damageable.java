package srg.ship;

/**
 * Damageable interface
 */
public interface Damageable {
    /**
     * Threshold to decide if the room needs repairment
     */
    static final int REPAIR_THRESHOLD = 30;
    /**
     * Health Multiplier to get Health which differs by tier
     */
    static final int HEALTH_MULTIPLIER = 5;
    /**
     * Damage Rate to get Damage rate for the damage functions
     */
    static final int DAMAGE_RATE = 5;

    /**
     * Gives the current health status
     * @return The Health status in percentage
     */
    abstract int getHealth();

    /**
     * Gives information of the room about whether it needs Repair or not comparing the REPAIR_THRESHOLD and current health
     * @return If the room needs repair, it returns True and if not it returns False
     */
    default boolean needsRepair() {
        if (getHealth() <= REPAIR_THRESHOLD) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Applies damage in rooms when traveling other ports
     */
    abstract void damage();

    /**
     * Defines whether the room is broken or not
     * @return If the health of the room is lower than 1, it returns true and if it is larger than 0 it returns false
     */
    default boolean isBroken() {
        if (getHealth() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Resets the health to maximum
     */
    abstract void resetHealth();

    /**
     * Sets the new damage rate with differs by tier
     * @param newDamageRate new Damage rate
     */
    abstract void setDamageRate(int newDamageRate);
}
