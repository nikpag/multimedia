package src.classes;

/**
 * A tuple holding a character and its corresponding probability.
 */
public class Tuple {
    /**
     * Creates a new Tuple instance.
     * 
     * @param ch   the character of the Tuple to be initialized.
     * @param prob the probability of the Tuple to be initialized.
     */
    public Tuple(char ch, float prob) {
        character = ch;
        probability = prob;
    }

    /**
     * Gets the character of the tuple.
     * 
     * @return the character of the tuple.
     */
    public char getCharacter() {
        return character;
    }

    /**
     * Gets the probability of the tuple.
     * 
     * @return the probability of the tuple.
     */
    public float getProbability() {
        return probability;
    }

    /**
     * Sets the probability of the tuple equal to p.
     * 
     * @param p the probability to be set.
     */
    public void setProbability(float p) {
        probability = p;
    }

    private char character; /* The character/letter held in the tuple. */
    private float probability; /* The probability of the corresponding character. */
}
