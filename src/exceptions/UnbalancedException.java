package src.exceptions;

/**
 * This exception is thrown when less than 20% of the words in the dictionary
 * have >=9 letters.
 */
public class UnbalancedException extends Exception {
    /**
     * Creates a new UnbalancedException instance.
     * 
     * @param message an informational message that accompanies the
     *                UnbalancedException.
     */
    public UnbalancedException(String message) {
        super(message);
    }
}
