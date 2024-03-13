import java.util.Iterator;
import java.util.LinkedList;

public class HashMap<K, V> implements Map61BL<K, V> {
    LinkedList<Entry>[] map;
    static int arrayLength;
    static double threshold;
    int size;

    public HashMap() {
        arrayLength = 16;
        threshold = 0.75;
        map = new LinkedList[arrayLength];
        size = 0;
    }

    public HashMap(int initialCapacity) {
        arrayLength = initialCapacity;
        threshold = 0.75;
        map = new LinkedList[arrayLength];
        size = 0;
    }

    public HashMap(int initialCapacity, double loadFactor) {
        arrayLength = initialCapacity;
        threshold = (double) loadFactor;
        map = new LinkedList[arrayLength];
        size = 0;
    }

    public int capacity() {
        return this.arrayLength;
    }
    @Override
    public void clear() {
        map = new LinkedList[arrayLength];
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        Entry comparedEntry = new Entry(key, null);
        LinkedList<Entry> a = map[comparedEntry.hashCode()];
        if (a != null) {
            for (Entry e : a) {
                if (comparedEntry.keyEquals(e)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        if (containsKey(key)) {
            Entry compareEntry = new Entry(key, null);
            LinkedList<Entry> a = map[compareEntry.hashCode()];
            for (Entry e : a) {
                if (compareEntry.keyEquals(e)) {
                    return (V) e.value;
                }
            }
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        Entry newEntry = new Entry(key, value);
        if (!containsKey(key)) {
            if (resize()) {
                resizeHelper();
            }
            int index = newEntry.hashCode();
            if (map[index] == null) {
                map[index] = new LinkedList<>();
            }
            map[index].add(newEntry);
            size++;
        } else {
            int index = newEntry.hashCode();
            V val = get(key);
            Entry entry = map[index].get(map[index].indexOf(new Entry(key, val)));
            entry.value = value;
        }
    }

    @Override
    public V remove(K key) {
        if (containsKey(key)) {
            Entry tempEntry = new Entry(key, null);
            LinkedList a = map[tempEntry.hashCode()];
            V value = get(key);
            Entry removedEntry = new Entry(key, value);
            a.remove(removedEntry);
            map[tempEntry.hashCode()] = null;
            size--;
            return value;
        }
        return null;
    }

    @Override
    public boolean remove(K key, V value) {
        if (containsKey(key) && get(key) == value) {
            remove(key);
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }

    public boolean resize() {
        double loadFac = (double) (this.size + 1) /arrayLength;
        if (loadFac > threshold) {
            return true;
        }
        return false;
    }

    public void resizeHelper() {
        arrayLength = arrayLength * 2;
        LinkedList<Entry>[] prevHashmap = map;
        map = new LinkedList[arrayLength];
        size = 0;

        for (int i = 0; i < prevHashmap.length; i++) {
            LinkedList<Entry> a = prevHashmap[i];
            if (a == null) {
                continue;
            }
            for (Entry entry : a) {
                if (entry != null) {
                    int index = entry.hashCode();
                    if (map[index] == null) {
                        map[index] = new LinkedList<Entry>();
                        map[index].add(entry);
                        size++;
                    }
                }
            }
        }
    }

    private static class Entry<K, V> {

        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry) other).key)
                    && value.equals(((Entry) other).value));
        }

        @Override
        public int hashCode() {
            int val = key.hashCode();
            return Math.floorMod(val, arrayLength);
        }
    }
}