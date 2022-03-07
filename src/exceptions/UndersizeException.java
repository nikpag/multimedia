package src.exceptions;

/* Every dictionary must have at least 20 words */
public class UndersizeException extends Exception {
    public UndersizeException(String message) {
        super(message);
    }
}
