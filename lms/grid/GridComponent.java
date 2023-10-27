package lms.grid;

import lms.exceptions.BadStateException;
import lms.exceptions.UnsupportedActionException;

/**
 * An interface for components that can be encoded as strings and placed in a grid.
 *
 * @provided
 */
public interface GridComponent {

    /**
     * Returns an encoding of this component as a string.
     *
     * @return the encoding of this component as a string.
     */
    String getEncoding();
}
