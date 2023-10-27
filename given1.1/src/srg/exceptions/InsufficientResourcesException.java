package srg.exceptions;

/**
 * InsufficientResourcesException class which occurs when there is not enough resource
 */
public class InsufficientResourcesException extends Throwable {
    /**
     * The constructor of the Exception with no error message
     */
    public InsufficientResourcesException() {
        super();
    }

    /**
     * The constructor of the Exception with error message
     * @param message Detail of the exception
     */
    public InsufficientResourcesException(String message) {
        super(message);
    }
}
