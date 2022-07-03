package src.exceptions;

/**
 * This exception is thrown if the dictionary has fewer than 20 words.
 */
public class UndersizeException extends Exception {
    /**
     * Creates a new UndersizeException instance.
     * 
     * @param message an informational message that accompanies the
     *                UndersizeException.
     */
    public UndersizeException(String message) {
        super(message);
    }
}
