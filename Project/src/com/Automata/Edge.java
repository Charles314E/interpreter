package com.Automata;

public class Edge<T> {
    private final T input;
    private final State<T> destination;

    public Edge(T input, State<T> destination) {
        this.input = input;
        this.destination = destination;
    }

    public State<T> getDestination() {
        return destination;
    }
    public T getInput() {
        return input;
    }

    public boolean equals(Edge<T> other) {
        return (getInput() == other.getInput() && getDestination() == other.getDestination());
    }
    public String toString() {
        return "X -" + input + "-> " + destination.toString(true);
    }

}
