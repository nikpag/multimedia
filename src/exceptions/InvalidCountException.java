package src.exceptions;

/**
 * This exception should be thrown when there are duplicate entries in the
 * dictionary.
 * 
 * In our implementation, this will never be thrown, since we filter out
 * duplicates from the beginning.
 */
public class InvalidCountException extends Exception {
    /**
     * Creates a new InvalidCountException instance.
     * 
     * @param message an informational message that accompanies the
     *                InvalidCountException.
     */
    public InvalidCountException(String message) {
        super(message);
    }
}
