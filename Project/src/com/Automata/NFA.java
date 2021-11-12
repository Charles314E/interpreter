package com.Automata;

import com.N1_Lexer.Regex.*;
import com.Classes.Triple;
import com.Methods.stringMethods;

import java.util.*;

public class NFA<T extends Character> extends Automaton<T> {
    private final HashMap<State<T>, HashMap<T, Set<Edge<T>>>> transitionMatrix = new HashMap<>();
    private final HashMap<State<T>, Set<State<T>>> closures = new HashMap<>();
    private final ArrayList<Set<State<T>>> dfaSets = new ArrayList<>();
    private final HashMap<Set<State<T>>, State<T>> dfaStates = new HashMap<>();
    private final Set<T> language = new HashSet<>();
    private DFA<T> dfa;

    public NFA(State<T> start) {
        this.start = start;
        current = this.start;
        addState(this.start);
    }
    public NFA(State<T> start, boolean silent) {
        this(start);
        this.silent = silent;
    }

    public void computeClosures() {
        for (State<T> state : states) {
            closures.put(state, state.closure(new HashSet<>(), 0, true));
        }
    }
    public Set<State<T>> collapseStates(Set<State<T>> closure, T language, boolean silent) {
        stringMethods.silentPrintLine(" : COLLAPSE_STATES : ", silent);
        Set<State<T>> q2 = new HashSet<>();
        for (State<T> state : closure) {
            for (Edge<T> edge : state.getOutput()) {
                if (edge.getInput() == language) {
                    q2.add(edge.getDestination());
                }
            }
        }
        stringMethods.silentPrintLine("  " + language + " : " + q2, silent);
        return q2;
    }

    public DFA<T> getDFA() {
        return dfa;
    }
    public void createDFA(boolean silent) {
        stringMethods.silentPrintLine(": CREATE_DFA : ", silent);
        dfa = new DFA<>(new State<>(null), silent);
        dfa.getStart().setAutomaton(dfa);
        Set<State<T>> states = new HashSet<>(closures.get(start));
        dfaStates.put(states, dfa.getStart());
        dfaSets.add(states);
        HashMap<Set<State<T>>, State<T>> stateSet;
        for (int i = 0; i < dfaStates.size(); i += 1) {
            stateSet = createDFAState(dfaSets.get(i), i, silent);
            for (Map.Entry<Set<State<T>>, State<T>> state : stateSet.entrySet()) {
                if (!dfaStates.containsKey(state.getKey())) {
                    dfaStates.put(state.getKey(), state.getValue());
                    dfaSets.add(state.getKey());
                }
            }
        }
        stringMethods.silentPrintLine(silent);
        for (Map.Entry<Set<State<T>>, State<T>> state : dfaStates.entrySet()) {
            stringMethods.silentPrintLine(state.getValue() + " = " + state.getKey(), silent);
        }
    }

    public HashMap<Set<State<T>>, State<T>> createDFAState(Set<State<T>> nfaStateSet, int i, boolean silent) {
        HashMap<Set<State<T>>, State<T>> stateSet = new HashMap<>();
        Set<State<T>> stateClosures, newState, ns;
        State<T> linkState;
        Set<State<T>> closure;
        stringMethods.silentPrintLine("- CURRENT_STATE : " + nfaStateSet, silent);
        for (T c : language) {
            stateClosures = new HashSet<>();
            stringMethods.silentPrintLine("    LANG : " + c, silent);
            for (State<T> state : nfaStateSet) {
                if (state.findInputPath(c)) {
                    closure = state.closure(new HashSet<>(), i, true);
                    stringMethods.silentPrintLine("      " + state + " : CLOSURE : " + closure, silent);
                    stateClosures.addAll(closure);
                }
                else {
                    stringMethods.silentPrintLine("      " + state + " : CLOSURE : <null>", silent);
                }
            }
            newState = collapseStates(stateClosures, c, true);
            ns = new HashSet<>();
            for (State<T> state : newState) {
                closure = state.closure(new HashSet<>(), i, true);
                ns.addAll(closure);
            }
            newState.addAll(ns);
            if (!stateSet.containsKey(newState)) {
                stringMethods.silentPrintLine("      NEW_STATE : " + newState + " : " + dfaSets.contains(newState), silent);
                if (newState.size() > 0) {
                    if (dfaSets.contains(newState)) {
                        linkState = dfaStates.get(newState);
                    } else {
                        linkState = new State<>(dfa);
                        for (State<T> state : newState) {
                            if (state.isEndingState()) {
                                linkState.makeEndingState();
                            }
                        }
                        dfa.addState(linkState);
                        stateSet.put(newState, linkState);
                    }
                    dfaStates.get(nfaStateSet).addLeavingEdge(new Edge<>(c, linkState));
                }
            }
            else {
                stringMethods.silentPrintLine("      OLD_STATE : " + newState, silent);
            }
        }
        return stateSet;
    }
    public int traverse(T[] input) {
        return -1;
    }
    public void transition(T input) {}
    public boolean addTransition(State<T> state, Edge<T> edge) {
        boolean good = true;
        HashMap<T, Set<Edge<T>>> map = transitionMatrix.get(state);
        if (map == null) {
            map = new HashMap<>();
            good = false;
        }
        Set<Edge<T>> edges = map.get(edge.getInput());
        if (edges == null) {
            edges = new HashSet<>();
            good = false;
        }
        map = new HashMap<>();
        edges.add(edge);
        map.put(edge.getInput(), edges);
        transitionMatrix.put(state, map);
        addState(state);
        return good;
    }
    public Triple<State<T>, T, State<T>> populate(RegexChild<T> regex, State<T> start, boolean silent) {
        return populate(regex, start, 0, silent);
    }
    public Triple<State<T>, T, State<T>> populate(RegexChild<T> regex, State<T> start, int i, boolean silent) {
        Automaton<T> nfa = start.getAutomaton();
        nfa.addState(start);
        Triple<State<T>, T, State<T>> s;
        State<T> end;
        T type = null;
        if (regex instanceof RegexAlternation) {
            stringMethods.silentPrintLine(stringMethods.indent(i) + regex + " : ALTERNATION", silent);
            end = new State<>(nfa);
            int j = 1;
            for (RegexChild<T> child : regex.getChildren()) {
                s = populate(child, new State<>(nfa), i + 1, silent);
                type = s.getSecond();
                start.addLeavingEdge(new Edge<>(type, s.getFirst()), i + 1, silent);
                s.getThird().addLeavingEdge(new Edge<>(null, end), i + 1, silent);
                stringMethods.silentPrintLine(stringMethods.indent(i + 1) + "ALTERNATION_LEAVING_" + j + " : " + child + " " + stringMethods.tuple(start, s.getFirst(), s.getThird(), end), silent);
                j += 1;
            }
            type = null;
        }
        else if (regex instanceof RegexConcatenation) {
            stringMethods.silentPrintLine(stringMethods.indent(i) + regex + " : CONCATENATION", silent);
            end = start;
            State<T> newStart;
            int j = 1;
            for (RegexChild<T> child : regex.getChildren()) {
                newStart = new State<>(nfa);
                s = populate(child, end, i + 1, silent);
                type = s.getSecond();
                s.getThird().addLeavingEdge(new Edge<>(type, newStart), i + 1, silent);
                end = newStart;
                stringMethods.silentPrintLine(stringMethods.indent(i + 1) + "CONCATENATION_LEAVING_" + j + " : " + child + " " + stringMethods.tuple(start, s.getFirst(), s.getThird(), end), silent);
                j += 1;
            }
            type = null;
        }
        else if (regex instanceof RegexRepetition) {
            stringMethods.silentPrintLine(stringMethods.indent(i) + regex + " : REPETITION", silent);
            s = populate(((RegexRepetition<T>) regex).getPhrase(), start, i + 1, silent);
            type = s.getSecond();
            State<T> trans = s.getThird().addLeavingEdge(new Edge<>(type, new State<>(nfa)), i + 1, silent);
            trans.addLeavingEdge(new Edge<>(null, start), i + 1, silent);
            stringMethods.silentPrintLine(stringMethods.indent(i + 1) + "REPETITION_LEAVING", silent);
            end = start;
            type = null;
        }
        else if (regex instanceof RegexSymbol) {
            stringMethods.silentPrintLine(stringMethods.indent(i) + regex + " : SYMBOL", silent);
            end = start;
            try {
                type = (((RegexSymbol<T>) regex).getSymbol());
                language.add(type);
            }
            catch (ClassCastException e) {
                stringMethods.silentPrintLine(stringMethods.indent(i + 1) + "BROKEN_SYMBOL_LEAVING", silent);
            }
        }
        else {
            stringMethods.silentPrintLine(stringMethods.indent(i) + regex + " : OTHER", silent);
            end = start;
        }
        nfa.addState(end);
        return new Triple<>(start, type, end);
    }

    public Set<T> getLanguage() {
        return language;
    }
}
