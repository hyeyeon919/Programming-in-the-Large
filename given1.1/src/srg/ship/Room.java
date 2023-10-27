package srg.ship;

import java.util.ArrayList;
import java.util.List;

/**
 * Room class
 */
public class Room implements Damageable {
    /**
     * The tier of the room
     */
    private RoomTier tier;
    /**
     * The new damage rate of the room
     */
    private int newDamageRate;
    /**
     * The initial health of the room
     */
    private int health = 100;
    /**
     * The room object
     */
    private Room room;

    /**
     * Constructs default Room object with is BASIC tier and sets all settings as the room of BASIC tier
     */
    public Room() {
        this.tier = RoomTier.BASIC;
        resetHealth();
        setDamageRate(RoomTier.BASIC.damageMultiplier * DAMAGE_RATE);
    }

    /**
     * Constructs Room of input tier and sets all settings as the room of the input tier
     * @param tier Tier of the room
     */
    public Room(RoomTier tier) {
        this.tier = tier;
        setDamageRate(tier.damageMultiplier * DAMAGE_RATE);
        resetHealth();
    }

    /**
     * Applies damage to the ship's room.
     */
    public void damage() {
        this.health = this.health - newDamageRate;
    }

    /**
     * Returns the list of actions this room can perform
     * @return List of actions of this room can perform
     */
    public List<String> getActions() {
        List<String> actionList = new ArrayList<>();
        return actionList;
    }

    /**
     * Returns an integer representing the current health as a percentage of the maximum, rounded down.
     * @return Health percentage
     */
    public int getHealth() {
        double result = ((this.health * 1.0)
                / (this.getTier().healthMultiplier * HEALTH_MULTIPLIER));
        return (int) Math.floor(result * 100);
    }

    /**
     * Returns tier of the Room object
     * @return Tier of the Room
     */
    public RoomTier getTier() {
        return this.tier;
    }

    /**
     * Recalculates maximum health and resets health to maximum.
     */
    public void resetHealth() {
        this.health = HEALTH_MULTIPLIER * getTier().healthMultiplier;
    }

    /**
     * Sets the damage rate of a Damageable object to newDamageRate
     * @param newDamageRate New Damage rate
     */
    @Override
    public void setDamageRate(int newDamageRate) {
        this.newDamageRate = newDamageRate;
    }

    /**
     * Returns information of the room in particular String format
     * @return Room information in String type
     */
    @Override
    public String toString() {
        return "ROOM: Room(" + getTier() + ") health: " + getHealth()
                + "%, needs repair: " + needsRepair();
    }

    /**
     * Upgrade settings of the room when the room tier is upgraded
     * If the room is already the highest tier which is PRIME then reset the health to maximum.
     */
    public void upgrade() {
        if (getTier().equals(RoomTier.AVERAGE)) {
            this.tier = RoomTier.PRIME;
        }
        if (getTier().equals(RoomTier.BASIC)) {
            this.tier = RoomTier.AVERAGE;
        }
        this.resetHealth();
        this.setDamageRate(this.tier.damageMultiplier * DAMAGE_RATE);
    }
}
