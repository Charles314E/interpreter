package com.N1_Lexer.Regex;

import com.Automata.NFA;
import com.Automata.State;
import com.Classes.Triple;
import com.Methods.stringMethods;

import java.util.ArrayList;

public class RegexBase<T extends Character> extends Regex<T> {
    private final boolean silent;
    protected NFA<T> nfa;
    protected RegexChild<T> base;
    public static final int SUCCESS = 0;
    public static final int WRONG_CLOSE = 1;
    public static final int WRONG_OPEN = 2;
    public static final int NO_OPEN = 3;

    public RegexBase(String pattern, boolean silent) {
        this.silent = silent;
        this.pattern = pattern.toCharArray();
        parse();
        stringMethods.silentPrintLine(stringMethods.tuple(pattern, base), silent);
        stringMethods.silentPrintLine(stringMethods.tuple(pattern, createNFA(silent)), silent);
        stringMethods.silentPrintLine(nfa, silent);
        nfa.printStates(silent);
        stringMethods.silentPrintLine(silent);
    }
    public boolean check(String input) {
        return false;
    }
    public int check(T[] input, int start) {
        return nfa.getDFA().traverse(input, start);
    }
    private int parse() {
        ArrayList<RegexBlockAttributes<T>> attributes = new ArrayList<>();
        attributes.add(new RegexBlockAttributes<>());
        StringBuilder str = new StringBuilder();
        char c;
        boolean escaped;
        for (int i = 0; i < pattern.length; i += 1) {
            RegexBlockAttributes<T> open = findOpenBlock(attributes);
            c = pattern[i];
            str.append(c);
            escaped = false;
            if (i > 0) {
                if (pattern[i - 1] == '\\') {
                    escaped = true;
                }
            }
            if (!escaped) {
                if (c == '[' || c == '(') {
                    attributes.add(new RegexBlockAttributes<>(i, c));
                    try {
                        open.addChild(attributes.get(attributes.size() - 1));
                    }
                    catch (NullPointerException e) {
                        return -1;
                    }
                } else if (c == ']' || c == ')') {
                    if (open != null) {
                        if (open.getClosingBracket() != null) {
                            if (open.getClosingBracket() == c) {
                                open.close(i);
                                open.setPhrase(str.substring(open.getStart(), open.getEnd() + 1));
                            } else {
                                return WRONG_CLOSE;
                            }
                        } else {
                            return WRONG_OPEN;
                        }
                    } else {
                        return NO_OPEN;
                    }
                }
            }
        }
        RegexBlockAttributes<T> base = attributes.get(0);
        base.close(pattern.length);
        base.setPhrase(str.substring(0, pattern.length));
        this.base = base.analyse();
        stringMethods.silentPrintLine(base, silent);
        return SUCCESS;
    }
    private RegexBlockAttributes<T> findOpenBlock(ArrayList<RegexBlockAttributes<T>> blocks) {
        if (blocks.size() > 0) {
            for (int j = blocks.size() - 1; j > -1; j -= 1) {
                RegexBlockAttributes<T> r = blocks.get(j);
                if (r.getEnd() == -1) {
                    return r;
                }
            }
        }
        return null;
    }
    public Triple<State<T>, T, State<T>> createNFA(boolean silent) {
        nfa = new NFA<>(new State<>(nfa), silent);
        Triple<State<T>, T, State<T>> s = nfa.populate(base, nfa.getStart(), silent);
        stringMethods.silentPrintLine(silent);
        s.getThird().makeEndingState();
        nfa.computeClosures();
        stringMethods.silentPrintLine(" : CLOSURES :", silent);
        nfa.createDFA(silent);
        stringMethods.silentPrintLine(silent);
        stringMethods.silentPrintLine(nfa.getDFA(), silent);
        nfa.getDFA().printStates(silent);
        return s;
    }
    public NFA<T> getNFA() {
        return nfa;
    }
}
