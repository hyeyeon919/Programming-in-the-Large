package lms.logistics;

import lms.exceptions.BadStateException;
import lms.exceptions.UnsupportedActionException;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Consumer;

/**
 * Maintains a doubly linked list to maintain the links for each node.
 * Has previous and next item.
 * The path can't have an empty node, as it will throw an illegal
 * argument exception.
 *
 * @version 1.0
 * @ass2
 */
public class Path {
    /**
     * Previous path
     */
    private Path previous;

    /**
     * Next path
     */
    private Path next;

    /**
     * Node of current Path
     */
    private Transport node;


    /**
     * Constructs a new Path object with the same Transport node, previous Path, and next Path as the specified Path object.
     *
     * @param path the Path object to copy.
     * @throws IllegalArgumentException if the path argument is null.
     */
    public Path(Path path) throws IllegalArgumentException {
        this.node = path.node;
        this.previous = path.previous;
        this.next = path.next;
    }

    /**
     * This method takes a Transport Consumer,
     * using the Consumer&lt;T&gt; functional interface from java.util.
     * It finds the tail of the path and calls
     * Consumer&lt;T&gt;'s accept() method with the tail node as an argument.
     * Then it traverses the Path until the head is reached,
     * calling accept() on all nodes.
     * <p>
     * This is how we call the tick method for all the different transport items.
     *
     * @param consumer Consumer&lt;Transport&gt;
     * @provided
     * @see java.util.function.Consumer
     */
    public void applyAll(Consumer<Transport> consumer) {
        Path path = tail(); // IMPORTANT: go backwards to aid tick
        do {
            consumer.accept(path.node);
            path = path.previous;
        } while (path != null);
    }

    /**
     * Constructs a new Path object with the specified Transport node,
     * and sets the previous and next Path objects in the path to null.
     * Throws an IllegalArgumentException if the node argument is null.
     *
     * @param node the Transport node for this Path.
     * @throws IllegalArgumentException if the node argument is null.
     */
    public Path(Transport node) throws IllegalArgumentException {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        this.node = node;
        this.previous = null;
        this.next = null;
    }

    /**
     * Constructs a new Path object with the specified Transport node,
     * and the previous and next Path objects in the path.
     * Throws an IllegalArgumentException if the node argument is null.
     *
     * @param node     the Transport node for this Path.
     * @param previous the previous Path object in the path.
     * @param next     the next Path object in the path.
     * @throws IllegalArgumentException if the node argument is null.
     */
    public Path(Transport node, Path previous, Path next) throws IllegalArgumentException {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        this.node = node;
        this.previous = previous;
        this.next = next;
    }

    /**
     * Returns the head of this Path, which is the first element in the path.
     * If this Path is the first element, it is returned as is.
     *
     * @return the head of this Path.
     */
    public Path head() {
        Path path = this;
        if (path.getPrevious() == null) {
            return path;
        } else {
            while (true) {
                if (path.getPrevious() == null) {
                    return path;
                } else {
                    path = path.getPrevious();
                }
            }
        }
    }

    /**
     * Accessor method for the transport node associated with this path.~
     *
     * @return the transport node associated with this path
     */
    public Transport getNode() {
        return this.node;
    }

    /**
     * Returns the tail of this Path, which is the last element in the path.
     * If this Path is the last element, it is returned as is.
     *
     * @return the tail of this Path.
     */
    public Path tail() {
        Path nextPath = this.getNext();
        if (nextPath == null) {
            return this;
        } else {
            while (true) {
                if (nextPath.getNext() == null) {
                    return nextPath;
                }
                nextPath = nextPath.getNext();
            }
        }
    }

    /**
     * Returns the previous Path object in the chain.
     *
     * @return the previous Path object in the chain, or null if this is the first Path object
     */
    public Path getPrevious() {
        if (this.previous == null) {
            return null;
        }
        return this.previous;
    }

    /**
     * Sets the previous path for this path.
     *
     * @param path the previous path to be set for this path
     */
    public void setPrevious(Path path) {
        this.previous = path;
    }

    /**
     * Returns the next Path object in the chain.
     *
     * @return the next Path object in the chain, or null if this is the last Path object
     */
    public Path getNext() {
        if (this.next == null) {
            return null;
        }
        return this.next;
    }

    /**
     * Sets the next path for this path.
     *
     * @param path the next path to be set for this path
     */
    public void setNext(Path path) {
        this.next = path;
    }

    /**
     * toString that provides a list of Path nodes from a Producer, along the belt to a Receiver.
     *
     * @return String representing the entirety of the best path links in the format:
     */
    public String toString() {
        String result = "START -> ";
        result += getString(this.head()) + " -> END";
        return result;
    }

    /**
     * Helper function for toString
     *
     * @param path path to get sub-path from
     * @return string format of the sub-paths
     */
    private String getString(Path path) {
        if (path.getNext() == null) {
            return path.getNode().toString();
        } else {
            return path.getNode().toString() + " -> " + getString(path.getNext());
        }
    }

    /**
     * Compares this Path object to the specified object for equality.
     * Returns true if and only if the specified object is also a Path object
     * and has the same Transport node as this Path object.
     *
     * @param o the object to compare this Path against
     * @return true if the specified object is equal to this Path object, false otherwise
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Path that = (Path) o;
        return this.previous == that.previous && this.next == that.next && this.node == that.node;
    }
}
