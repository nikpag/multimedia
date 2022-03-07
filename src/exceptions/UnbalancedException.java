package src.exceptions;

/* At least 20% of the words should have >=9 letters */
public class UnbalancedException extends Exception {
    public UnbalancedException(String message) {
        super(message);
    }
}
