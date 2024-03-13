/** A data structure to represent a Linked List of Integers.
 * Each SLList represents one node in the overall Linked List.
 */

public class SLList<T> {

    /** The object stored by this node. */
    public T item;
    /** The next node in this SLList. */
    public SLList<T> next;

    /** Constructs an SLList storing ITEM and next node NEXT. */
    public SLList(T item, SLList<T> next) {
        this.item = item;
        this.next = next;
    }

    /** Constructs an SLList storing ITEM and no next node. */
    public SLList(T item) {
        this(item, null);
    }

    /**
     * Returns [position]th item in this list. Throws IllegalArgumentException
     * if index out of bounds.
     *
     * @param position, the position of element.
     * @return The element at [position]
     */
    public T get(int position) {
        //TODO: your code here!
        SLList<T> curr = this;
        int currPosition = 0;

        while (curr != null) {
            if (currPosition == position){
                return curr.item;
            }
            curr = curr.next;
            currPosition += 1;

        }

        throw new IllegalArgumentException("Position out of range.");
    }

    /**
     * Returns the string representation of the list. For the list (1, 2, 3),
     * returns "1 2 3".
     *
     * @return The String representation of the list.
     */
    public String toString() {
        //TODO: your code here!
        String stringRepresentation = "";
        SLList<T> curr = this;

        while (curr != null) {
            stringRepresentation += curr.item + " ";
            curr = curr.next;
        }

        return stringRepresentation.trim();
    }

    /**
     * Returns whether this and the given list or object are equal.
     *
     * NOTE: A full implementation of equals requires checking if the
     * object passed in is of the correct type, as the parameter is of
     * type Object. This also requires we convert the Object to an
     * SLList<T>, if that is legal. The operation we use to do this is called
     * casting, and it is done by specifying the desired type in
     * parenthesis. This has already been implemented for you.
     *
     * @param obj, another list (object)
     * @return Whether the two lists are equal.
     */

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SLList)) {
            return false;
        }
        SLList<T> otherLst = (SLList<T>) obj;
        SLList<T> currThis = this;
        SLList<T> currOther = otherLst;

        while (currThis != null && currOther != null) {
            if (!currThis.item.equals(currOther.item)) {
                return false;
            }
            currThis = currThis.next;
            currOther = currOther.next;

        }
        return currThis == null && currOther == null;
    }

    /**
     * Adds the given value at the end of the list.
     *
     * @param value, the int to be added.
     */
    public void add(T value) {
        //TODO: your code here!
        SLList<T> newNode = new SLList<>(value);

        if (next == null) {
            next = newNode;
        } else {
            SLList<T> curr = this;
            while (curr.next != null) {
                curr = curr.next;
            }
            curr.next = newNode;
        }
    }
}