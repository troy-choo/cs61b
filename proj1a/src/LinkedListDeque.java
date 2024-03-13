import java.util.ArrayList;
import java.util.List;

public class LinkedListDeque<T> implements Deque<T> {

    private Node<T> sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node<>();
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    private class Node<T> {
        private T value;
        private Node<T> prev;
        private Node<T> next;

        public Node(T value) {
            this.value = value;
            this.prev = null;
            this.next = null;
        }

        public Node() {
            value = null;
            prev = this;
            next = this;
        }
    }
    @Override
    public void addFirst(T x) {
        Node<T> newNode = new Node<>(x);
        newNode.next = sentinel.next;
        newNode.prev = sentinel;
        sentinel.next.prev = newNode; /**Not sure*/
        sentinel.next = newNode;
        size++;
    }

    @Override
    public void addLast(T x) {
        Node<T> newNode = new Node<>(x);
        newNode.next = sentinel;
        newNode.prev = sentinel.prev;
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        Node<T> currentNode = sentinel.next;
        while (currentNode != sentinel) {
            returnList.add(currentNode.value);
            currentNode = currentNode.next;
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

        Node<T> firstNode = sentinel.next;
        sentinel.next = firstNode.next;
        firstNode.next.prev = sentinel;
        size--;

        return firstNode.value;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        Node<T> lastNode = sentinel.prev;
        sentinel.prev = lastNode.prev;
        lastNode.prev.next = sentinel;
        size--;

        return lastNode.value;
    }

    @Override
    public T get(int index) {
        Node<T> currentNode = sentinel.next;
        if (index >= size || index < 0) {
            return null;
        }
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next;
        }

        return currentNode.value;
    }

    @Override
    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }

        return getRecursiveHelper(sentinel.next, index);
    }

    private T getRecursiveHelper(Node<T> current, int index) {
        if (index == 0) {
            return current.value;
        }

        return getRecursiveHelper(current.next, index - 1);
    }
}
