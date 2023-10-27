package lms.logistics;

import lms.grid.Coordinate;

import java.util.Objects;

/**
 * Class to manage the name of an Item object. Provides implementations for equals, hashcode, and toString.
 */
public class Item {
    /**
     * String of the item.
     */
    private String name;

    /**
     * Constructor of Item
     * @param name Name of the time.
     * @throws IllegalArgumentException if name is empty or null
     */
    public Item(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            this.name = name;
        }
    }

    /**
     * Default and expected implementation specific to the needs of the comparison requirements.
     * Indicates whether some other object is "equal to" this one.
     * This implementation of equals compares the given object to the current object for equivalence based on
     * the values of their respective properties.
     * If the two objects have the same class,
     * and their properties have the same values (as determined by the equals method of each property),
     * then they are considered equal.
     * Note that the comparison is symmetric, meaning that a.equals(b) will return true if and
     * only if b.equals(a) returns true for any non-null object reference b.
     * @param o the object to compare for equality
     * @return true if the given object is equal to this object; false otherwise
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Item that = (Item) o;
        return this.name == that.name;
    }

    /**
     * Hashcode implementation, where hashcode is calculated based on the item's name
     * @return A hashcode calculated based on the item's name.
     */
    public int hashCode() {
        return Objects.hash(this.name);
    }

    /**
     * A String representation of the Item.
     * @return the Item name.
     */
    public String toString() {
        return this.name;
    }
}
