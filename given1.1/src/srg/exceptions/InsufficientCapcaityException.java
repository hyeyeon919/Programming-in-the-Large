package srg.exceptions;

/**
 * InsufficientCapcaityException class which occurs when there is not enough capacity
 */
public class InsufficientCapcaityException extends Throwable {
    /**
     * The constructor of the Exception with no error message
     */
    public InsufficientCapcaityException() {
        super();
    }

    /**
     * The constructor of the Exception with error message
     * @param message Detail of the exception
     */
    public InsufficientCapcaityException(String message) {
        super(message);
    }
}
