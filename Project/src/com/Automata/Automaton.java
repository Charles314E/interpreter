package com.Automata;

import com.Methods.stringMethods;

import java.util.*;

public abstract class Automaton<T> {
    protected boolean silent = true;
    protected State<T> start;
    protected State<T> current;
    protected ArrayList<State<T>> end = new ArrayList<>();
    protected ArrayList<State<T>> states = new ArrayList<>();

    public void reset() {
        current = start;
    }
    public abstract int traverse(T[] input);

    public abstract void transition(T input);

    public State<T> getStart() {
        return start;
    }
    public void setStart(State<T> state) {
        start = state;
    }
    public ArrayList<State<T>> getEndings() {
        return end;
    }

    public ArrayList<State<T>> getStates() {
        return states;
    }

    public int getNewID() {
        int id = 0;
        for (State<T> s : states) {
            if (s.getID() > id) {
                id = s.getID();
            }
        }
        //methods.silentPrintLine(methods.tuple("ID", id, states.size()), silent);
        return id + 1;
    }
    public State<T> getState(int id) {
        for (State<T> state : states) {
            if (state.getID() == id) {
                return state;
            }
        }
        return null;
    }
    public State<T> addState(State<T> state) {
        Collections.sort(states);
        if (states.isEmpty()) {
            start = state;
        }
        state.setID(getNewID());
        state.setAutomaton(this);
        boolean duplicate = false;
        for (State<T> s : states) {
            if (s == state) {
                duplicate = true;
                break;
            }
        }
        if (!duplicate) {
            states.add(state);
        }
        stringMethods.silentPrintLine("ADD_STATE : " + stringMethods.tuple(state.getID(), states.size()), true);
        if (state.isEndingState()) {
            end.add(state);
        }
        Collections.sort(states);
        return state;
    }
    public void addEndingState(State<T> state) {
        end.add(state);
    }

    public abstract boolean addTransition(State<T> state, Edge<T> edge);

    public void printStates(boolean silent) {
        for (State<T> s : states) {
            stringMethods.silentPrintLine(s.toString(false), silent);
        }
        stringMethods.silentPrintLine(silent);
    }
    public void printStates() {
        printStates(silent);
    }

    public String toString() {
        return getClass().getSimpleName() + start.toString(true);
    }
}
