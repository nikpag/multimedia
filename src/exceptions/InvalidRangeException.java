package src.exceptions;

/**
 * A dictionary shouldn't have words with <6 letters.
 * 
 * Normally, this will not be thrown because we filter out small words from the
 * beginning.
 */
public class InvalidRangeException extends Exception {
    public InvalidRangeException(String message) {
        super(message);
    }
}
