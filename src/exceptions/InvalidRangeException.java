package src.exceptions;

/**
 * This exception should be thrown when the dictionary has words of size <6.
 * 
 * In our implementation, this will never be thrown, since we filter out small
 * words from the beginning.
 */
public class InvalidRangeException extends Exception {
    /**
     * Creates a new InvalidRangeException instance.
     * 
     * @param message an informational message that accompanies the
     *                InvalidRangeException.
     */
    public InvalidRangeException(String message) {
        super(message);
    }
}
