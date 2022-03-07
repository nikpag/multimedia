package src.classes;

import java.util.Comparator;

public class TupleComparator implements Comparator<Tuple> {
    public int compare(Tuple t1, Tuple t2) {
        Float p1 = t1.probability;
        Float p2 = t2.probability;
        return -p1.compareTo(p2);
    }
}
