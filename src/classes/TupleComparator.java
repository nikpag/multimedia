package src.classes;

import java.util.Comparator;

/**
 * This comparator is used in order to compare two Tuples of the form
 * (character, probability), according to their probability.
 */
public class TupleComparator implements Comparator<Tuple> {
    /**
     * Compares two tuples of the form (character, probability), according to their
     * probability.
     * 
     * @param t1 the first tuple to be compared.
     * @param t2 the second tuple to be compared.
     * @return 0 if the two tuples' probabilities are equal, a value less than 0 if
     *         the probability of t1 is greater than the probability of t2, and a
     *         value greater than 0 if the probability of t1 is less than the
     *         probability of t2.
     */
    public int compare(Tuple t1, Tuple t2) {
        Float p1 = t1.getProbability();
        Float p2 = t2.getProbability();
        return -p1.compareTo(p2);
    }
}
