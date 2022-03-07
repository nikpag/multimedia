package src.exceptions;

/**
 * Every word must be included only once in the dictionary.
 * 
 * Normally, this will not be thrown because we filter out duplicate words from
 * the beginning.
 */
public class InvalidCountException extends Exception {
    public InvalidCountException(String message) {
        super(message);
    }
}
