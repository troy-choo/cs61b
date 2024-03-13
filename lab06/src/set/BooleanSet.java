package set;

/**
 * Represent a set of nonnegative ints from 0 to maxElement (inclusive) for some initially
 * specified maxElement.
 */
public class BooleanSet implements SimpleSet {

    private boolean[] contains;
    private int size;

    /** Initializes a set of ints from 0 to maxElement. */
    public BooleanSet(int maxElement) {
        contains = new boolean[maxElement + 1];
        size = 0;
    }

    /** Adds k to the set. */
    @Override
    public void add(int k) {
        if (k >= 0 && k < contains.length && !contains[k]) {
            contains[k] = true;
            size++;
        }
    }

    /** Removes k from the set. */
    @Override
    public void remove(int k) {
        if (k >= 0 && k < contains.length && contains[k]) {
            contains[k] = false;
            size--;
        }
    }

    /** Return true if k is in this set, false otherwise. */
    @Override
    public boolean contains(int k) {
        return contains[k];
    }


    /** Return true if this set is empty, false otherwise. */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /** Returns the number of items in the set. */
    @Override
    public int size() {
        // TODO: Implement this method.
        return size;
    }

    /** Returns an array containing all of the elements in this collection. */
    @Override
    public int[] toIntArray() {
        int[] arr = new int[size];
        int index = 0;
        for (int i = 0; i < contains.length; i++) {
            if (contains[i]) {
                arr[index] = i;
                index++;
            }
        }
        return arr;
    }
}
