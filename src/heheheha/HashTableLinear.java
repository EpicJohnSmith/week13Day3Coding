package heheheha;

public class HashTableLinear<K, V> {

    private static class Entry<K, V> {
        K key;
        V value;
        Entry(K k, V v) { key = k; value = v; }
        @Override public String toString() { return key + "=" + value; }
    }

    private enum SlotState { EMPTY_SINCE_START, OCCUPIED, EMPTY_AFTER_DELETE }

    private Entry<K,V>[] table;
    private SlotState[] states;

    private int capacity;
    private int size;
    private double loadFactorThreshold;

    public HashTableLinear(int initialCapacity, double loadFactor) {
        capacity = initialCapacity <= 0 ? 16 : initialCapacity;
        loadFactorThreshold = loadFactor;
        initiate();
    }

    public HashTableLinear() {
        this(16, 0.7);
    }

    @SuppressWarnings("unchecked")
    private void initiate() {
        table = (Entry<K,V>[]) new Entry[capacity];
        states = new SlotState[capacity];
        size = 0;

        for (int i = 0; i < capacity; i++)
            states[i] = SlotState.EMPTY_SINCE_START;
    }

    public int getHashing(K key) {
        return Math.floorMod(key.hashCode(), capacity);
    }

    private int probing(int idx) {
        return (idx + 1) % capacity;
    }

    public void add(K key, V value) {
        if (key == null) throw new IllegalArgumentException("Null key not allowed");

        int idx = getHashing(key);
        int start = idx;
        int firstDeleted = -1;

        while (true) {
            if (states[idx] == SlotState.EMPTY_SINCE_START) {
                if (firstDeleted != -1) idx = firstDeleted;
                table[idx] = new Entry<>(key, value);
                states[idx] = SlotState.OCCUPIED;
                size++;
                break;
            } 
            else if (states[idx] == SlotState.EMPTY_AFTER_DELETE) {
                if (firstDeleted == -1) firstDeleted = idx;
            } 
            else if (states[idx] == SlotState.OCCUPIED &&
                     table[idx].key.equals(key)) {
                table[idx].value = value; // update existing
                return;
            }

            idx = probing(idx);
            if (idx == start) throw new RuntimeException("Table is full.");
        }

        if ((double)size / capacity > loadFactorThreshold)
            resize(capacity * 2);
    }

    public V get(K key) {
        int idx = getHashing(key);
        int start = idx;

        while (states[idx] != SlotState.EMPTY_SINCE_START) {
            if (states[idx] == SlotState.OCCUPIED &&
                table[idx].key.equals(key)) {
                return table[idx].value;
            }
            idx = probing(idx);
            if (idx == start) break;
        }
        return null;
    }

    public boolean remove(K key) {
        int idx = getHashing(key);
        int start = idx;

        while (states[idx] != SlotState.EMPTY_SINCE_START) {
            if (states[idx] == SlotState.OCCUPIED &&
                table[idx].key.equals(key)) {

                table[idx] = null;
                states[idx] = SlotState.EMPTY_AFTER_DELETE;
                size--;
                return true;
            }
            idx = probing(idx);
            if (idx == start) break;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Entry<K,V>[] oldTable = table;
        SlotState[] oldStates = states;
        int oldCap = capacity;

        capacity = newCapacity;
        table = (Entry<K,V>[]) new Entry[newCapacity];
        states = new SlotState[newCapacity];
        size = 0;

        for (int i = 0; i < newCapacity; i++)
            states[i] = SlotState.EMPTY_SINCE_START;

        for (int i = 0; i < oldCap; i++) {
            if (oldStates[i] == SlotState.OCCUPIED)
                add(oldTable[i].key, oldTable[i].value);
        }
    }

    public void printState() {
        System.out.println("=== HashTableLinear capacity=" +
                capacity + " size=" + size + " ===");

        for (int i = 0; i < capacity; i++) {
            System.out.print(i + ": ");
            switch (states[i]) {
                case EMPTY_SINCE_START -> System.out.println("EMPTY");
                case EMPTY_AFTER_DELETE -> System.out.println("DELETED");
                case OCCUPIED -> System.out.println("OCCUPIED(" + table[i] + ")");
            }
        }

        System.out.println("===========================\n");
    }
}