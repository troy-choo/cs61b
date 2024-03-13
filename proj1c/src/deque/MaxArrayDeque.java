package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        comparator = c;
    }

    public T max() {
        if (comparator == null) {
            return null;
        }
        return max(comparator);
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        T maxVal = get(0);
        for (T t : this) {
            if (c.compare(t, maxVal) > 0) {
                maxVal = t;
            }
        }
        return maxVal;
    }


}
