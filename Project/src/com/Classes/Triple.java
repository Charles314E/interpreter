package com.Classes;

import com.Methods.stringMethods;

public class Triple<T, U, V> {

    private final T first;
    private final U second;
    private final V third;

    public Triple(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst() { return first; }
    public U getSecond() { return second; }
    public V getThird() { return third; }

    public String toString() {
        return stringMethods.encapsulatedTuple("<", ">", ", ", first, second, third);
    }
}