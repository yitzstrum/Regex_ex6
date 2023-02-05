package oop.ex6.utils;

/**
 * Pair class, holds two values
 * @param <V1> - first value
 * @param <V2> - second value
 */
public class Pair<V1, V2> {

    private V1 first;
    private V2 second;

    /**
     * Constructor
     * @param first - first value
     * @param second - second value
     */
    public Pair(V1 first, V2 second) {
        this.first = first;
        this.second = second;
    }

    /**
     * @return the first value
     */
    public V1 getFirst() {
        return first;
    }

    /**
     * @return the second value
     */
    public V2 getSecond() {
        return second;
    }
}
