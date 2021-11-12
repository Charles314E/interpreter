package com.Automata;

import java.util.HashMap;

public class DFA<T> extends Automaton<T> {
    private final HashMap<State<T>, HashMap<T, Edge<T>>> transitionMatrix = new HashMap<>();
    
    public DFA(State<T> start) {
        this.start = start;
        addState(this.start);
    }

    public DFA(State<T> start, boolean silent) {
        this(start);
        this.silent = silent;
    }

    public int traverse(T[] input) {
        return traverse(input, 0);
    }
    public int traverse(T[] input, int start) {
        reset();
        int i = start;
        while (i < input.length) {
            if (current.eat(input[i])) {
                i += 1;
            }
            else {
                if (current.isEndingState()) {
                    return i;
                }
                return -1;
            }
        }
        if (current.isEndingState()) {
            return i;
        }
        return -1;
    }
    public void transition(T input) {
        current = getEdge(current, input).getDestination();
    }
    public Edge<T> getEdge(State<T> state, T input) {
        return transitionMatrix.get(state).get(input);
    }

    public boolean addTransition(State<T> state, Edge<T> edge) {
        HashMap<T, Edge<T>> map = transitionMatrix.get(state);
        if (map != null) {
            map.put(edge.getInput(), edge);
            transitionMatrix.put(state, map);
            return true;
        }
        map = new HashMap<>();
        map.put(edge.getInput(), edge);
        transitionMatrix.put(state, map);
        return false;
    }
}
