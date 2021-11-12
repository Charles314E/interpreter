package com.Automata;

import com.Methods.stringMethods;

import java.util.ArrayList;
import java.util.Set;

public class State<T> implements Comparable<State<T>> {
    private int id;
    protected Automaton<T> automaton;
    private final ArrayList<Edge<T>> output;
    private boolean ending;

    public State(Automaton<T> automaton) {
        this.output = new ArrayList<>();
        this.automaton = automaton;
    }

    public void setID(int id) {
        this.id = id;
    }
    public int getID() {
        return id;
    }

    public void setAutomaton(Automaton<T> automaton) {
        this.automaton = automaton;
    }
    public Automaton<T> getAutomaton() {
        return automaton;
    }
    public State<T> addLeavingEdge(Edge<T> edge) {
        return addLeavingEdge(edge, 0, true);
    }
    public State<T> addLeavingEdge(Edge<T> edge, int i, boolean silent) {
        output.add(edge);
        automaton.addTransition(this, edge);
        stringMethods.silentPrintLine(stringMethods.indent(i) + this + " -" + edge.getInput() + "-> " + edge.getDestination(), silent);
        return edge.getDestination();
    }
    public ArrayList<Edge<T>> getOutput() {
        return output;
    }
    public boolean isOpeningState() {
        return automaton.getStart() == this;
    }
    public boolean isEndingState() {
        return ending;
    }
    public void makeEndingState() {
        ending = true;
        automaton.addEndingState(this);
    }
    public boolean findInputPath(T c) {
        for (Edge<T> edge : output) {
            if (edge.getInput() == c) {
                return true;
            }
            else if (edge.getInput() == null) {
                if (edge.getDestination().findInputPath(c)) {
                    return true;
                }
            }
        }
        return false;
    }
    public Set<State<T>> closure(Set<State<T>> reach, T c, int i, boolean silent) {
        reach.add(this);
        stringMethods.silentPrintLine(i + " : " + id, silent);
        State<T> destination;
        for (Edge<T> edge : output) {
            destination = edge.getDestination();
            if (!reach.contains(destination)) {
                if (edge.getInput() == c || edge.getInput() == null) {
                    reach.add(destination);
                    reach = destination.closure(reach, c,i + 1, silent);
                }
            }
        }
        return reach;
    }
    public Set<State<T>> closure(Set<State<T>> reach, int i, boolean silent) {
        return closure(reach, null, i, silent);
    }

    public boolean eat(T in) {
        for (Edge<T> edge : output) {
            if (edge.getInput() == in) {
                automaton.transition(in);
                return true;
            }
        }
        return false;
    }

    public int compareTo(State<T> other) {
        return this.id - other.getID();
    }

    public boolean stateEquals(State<T> other) {
        if (output.size() == other.getOutput().size()) {
            Edge<T> e1;
            int j = -1;
            for (int i = 0; i < output.size(); i += 1) {
                e1 = output.get(i);
                for (Edge<T> e2 : other.getOutput()) {
                    if (j < i) {
                        if (e1.equals(e2)) {
                            j = i;
                        }
                    }
                }
                if (j < i) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public String toString(boolean silent) {
        StringBuilder sb = new StringBuilder();
        if (ending) {
            sb.append("<").append(id).append(">");
        }
        else if (automaton.start == this) {
            sb.append("*").append(id).append("*");
        }
        else {
            sb.append("{").append(id).append("}");
        }
        if (!silent) {
            sb.append("[");
            for (Edge<T> edge : output) {
                sb.append("(").append(edge).append(")");
            }
            sb.append("]");
        }
        return sb.toString();
    }
    public String toString() {
        return toString(true);
    }
}
