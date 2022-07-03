package src.exceptions;

/**
 * This exception is thrown when there is no "description" field in the JSON
 * returned by the Works API.
 */
public class NoDescriptionFieldException extends Exception {
    /**
     * Creates a new NoDescriptionFieldException instance.
     * 
     * @param message an informational message that accompanies the
     *                NoDescriptionFieldException.
     */
    public NoDescriptionFieldException(String message) {
        super(message);
    }
}
