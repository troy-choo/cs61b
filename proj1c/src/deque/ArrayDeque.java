package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayDeque<T> implements Deque<T> {

    private T[] items;
    private int size;
    private int nextFirst, nextLast;
    private static final int START_SIZE = 8;
    private static final int ARRAY_LIMIT_NUMBER = 16;

    public ArrayDeque() {
        items = (T[]) new Object[START_SIZE];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    @Override
    public void addFirst(T x) {
        if (size == items.length) {
            resize();
        }
        items[nextFirst] = x;
        nextFirst = (nextFirst - 1 + items.length) % items.length;
        size++;
    }

    @Override
    public void addLast(T x) {
        if (size == items.length) {
            resize();
        }
        items[nextLast] = x;
        nextLast = (nextLast + 1) % items.length;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        int currentIndex = (nextFirst + 1) % items.length;
        for (int i = 0; i < size; i++) {
            returnList.add(items[currentIndex]);
            currentIndex = (currentIndex + 1) % items.length;
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (items.length >= ARRAY_LIMIT_NUMBER && (double) size / items.length < 0.25) {
            resize();
        }
        nextFirst = (nextFirst + 1) % items.length;
        T removed = items[nextFirst];
        items[nextFirst] = null;
        size--;

        return removed;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (items.length >= ARRAY_LIMIT_NUMBER && (double) size / items.length < 0.25) {
            this.resize();
        }
        nextLast = (nextLast - 1 + items.length) % items.length;
        T removed = items[nextLast];
        items[nextLast] = null;
        size--;

        return removed;
    }

    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return items[(nextFirst + 1 + index) % items.length];
    }

    private void resize() {
        T[] newItems;
        if (size == items.length) {
            newItems = (T[]) new Object[items.length * 2];
        } else {
            newItems = (T[]) new Object[items.length / 2];
        }

        int newNextFirst = newItems.length / 2;
        int index = newNextFirst + 1;

        for (int i = 0; i < size; i++) {
            int currentIndex = (nextFirst + 1 + i) % items.length;
            newItems[index] = items[currentIndex];
            index = (index + 1) % newItems.length;
        }
        int newNextLast = index;
        nextFirst = newNextFirst;
        nextLast = newNextLast;
        items = newItems;
    }

    @Override
    public T getRecursive(int index) {
        return get(index);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    return null;
                }
                T item = toList().get(index);
                index++;
                return item;
            }
        };

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Deque<?> ArrayDeque) {
            if (this.size != ArrayDeque.size()) {
                return false;
            }
            if (!this.toList().equals(ArrayDeque.toList())) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        List<T> list = toList();
        return list.toString();
    }
}
