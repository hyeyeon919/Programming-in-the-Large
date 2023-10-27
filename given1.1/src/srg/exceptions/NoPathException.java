package srg.exceptions;

/**
 * NoPathException class which occurs when the path to the input port can not be found
 */
public class NoPathException extends Throwable {
    /**
     * The constructor of the Exception with no error message
     */
    public NoPathException() {
        super();
    }

    /**
     * The constructor of the Exception with error message
     * @param message Detail of the exception
     */
    public NoPathException(String message) {
        super(message);
    }
}
