package com.N1_Lexer.Regex;

import com.Methods.stringMethods;

import java.util.ArrayList;
import java.util.Collections;

public class RegexBlockAttributes<T extends Character> {
    private String phrase;
    private int start = 0;
    private int end = -1;
    private char bracket;
    private final char[] reservedCharacters = {'*', '|', '-', '{', '}', '(', ')', '[', ']'};
    private final ArrayList<RegexBlockAttributes<T>> children = new ArrayList<>();
    public RegexBlockAttributes() {}
    public RegexBlockAttributes(int start, char bracket) {
        this.start = start;
        this.bracket = bracket;
    }

    public void setPhrase(String str) {
        StringBuilder sb = new StringBuilder();
        int n = 0;
        RegexBlockAttributes<T> child;
        for (int i = 0; i < children.size(); i += 1) {
            child = children.get(i);
            sb.append(str, n, child.start - start);
            sb.append("{");
            sb.append(i);
            sb.append("}");
            n = child.end + 1 - start;
        }
        sb.append(str.substring(n));
        phrase = sb.toString();
    }

    public void addChild(RegexBlockAttributes<T> child) {
        children.add(child);
    }

    public int getStart() {
        return start;
    }
    public int getEnd() {
        return end;
    }
    public void close(int end) {
        this.end = end;
    }

    public Character getClosingBracket() {
        return switch (bracket) {
            case '(' -> ')';
            case '[' -> ']';
            default -> null;
        };
    }

    public boolean reserved(char c) {
        if (Character.isLetterOrDigit(c)) {
            return false;
        }
        for (char rc : reservedCharacters) {
            if (rc == c) {
                return true;
            }
        }
        return false;
    }
    public boolean isSymbol(int i) {
        return (symbolLength(i) > 0);
    }
    public int symbolLength(int i) {
        if (i > 0 && i < phrase.length() - 1) {
            if (!(phrase.charAt(i - 1) == '{' && phrase.charAt(i + 1) == '}')) {
                if (phrase.charAt(i) == '\\') {
                    return 2;
                }
                if (phrase.charAt(i - 1) != '\\' && !reserved(phrase.charAt(i))) {
                    return 1;
                }
            }
        }
        else {
            if (phrase.charAt(i) == '\\') {
                return 2;
            }
            if (!reserved(phrase.charAt(i))) {
                return 1;
            }
        }
        return -1;
    }

    public <U extends Character> RegexChild<U>[] parsePhrase(Character ... operators) {
        //System.out.println(phrase);
        ArrayList<RegexIndex<U>> parsed = new ArrayList<>();
        int r = 0, s, o = 0;
        char c, c1, c2;
        RegexChild<U> r1, r2;
        if (bracket == '(' || bracket == '[') {
            o = 1;
        }
        //System.out.println(phrase);
        for (int i = o; i < phrase.length() - o; i += 1) {
            c = phrase.charAt(i);
            s = symbolLength(i);
            if (s > 0) {
                //System.out.println(i + " : SYMBOL : " + phrase.charAt(i + (s - 1)));
                Character k = phrase.charAt(i + (s - 1));
                try {
                    parsed.add(new RegexIndex<>(i, new RegexSymbol<U>((U) k), false));
                }
                catch (ClassCastException e) {
                    return null;
                }
                i += s - 1;
            }
            else if (c == '}') {
                r += 1;
                //System.out.println("r = " + r);
                parsed.add(new RegexIndex<>(i, children.get(r - 1).analyse(), true));
            }
            else if (stringMethods.arrayContains(c, operators)) {
                switch (c) {
                    case '|' -> {
                        //System.out.println(i + " : ALTERNATION");
                        switch (phrase.charAt(i - 1)) {
                            case '}':
                                r1 = children.get(r - 1).analyse();
                                break;
                            default:
                                if (isSymbol(i - 1)) {
                                    Character k = phrase.charAt(i - 1);
                                    try {
                                        r1 = new RegexSymbol<>((U) k);
                                    }
                                    catch (ClassCastException e) {
                                        return null;
                                    }
                                } else {
                                    return null;
                                }
                                break;
                        }
                        switch (phrase.charAt(i + 1)) {
                            case '{':
                                r2 = children.get(r).analyse();
                                break;
                            default:
                                if (isSymbol(i + 1)) {
                                    Character k = phrase.charAt(i + 1);
                                    try {
                                        r2 = new RegexSymbol<>((U) k);
                                    }
                                    catch (ClassCastException e) {
                                        return null;
                                    }
                                } else {
                                    return null;
                                }
                                break;
                        }
                        parsed.add(new RegexIndex<>(i, new RegexAlternation<>(r1, r2), false));
                    }
                    case '-' -> {
                        //System.out.println(i + " : SYMBOL_RANGE");
                        switch (phrase.charAt(i - 1)) {
                            case '}':
                                return null;
                            default:
                                if (isSymbol(i - 1)) {
                                    c1 = phrase.charAt(i - 1);
                                } else {
                                    return null;
                                }
                                break;
                        }
                        switch (phrase.charAt(i + 1)) {
                            case '{':
                                return null;
                            default:
                                if (isSymbol(i + 1)) {
                                    c2 = phrase.charAt(i + 1);
                                } else {
                                    return null;
                                }
                                break;
                        }
                        parsed.add(new RegexIndex<>(i, new RegexSymbolRange<>(c1, c2), false));
                    }
                    case '*' -> {
                        //System.out.println(i + " : REPETITION");
                        boolean reference = false;
                        switch (phrase.charAt(i - 1)) {
                            case '}':
                                r1 = children.get(r - 1).analyse();
                                reference = true;
                                break;
                            default:
                                if (isSymbol(i - 1)) {
                                    Character k = phrase.charAt(i - 1);
                                    try {
                                        r1 = new RegexSymbol<>((U) k);
                                    }
                                    catch (ClassCastException e) {
                                        return null;
                                    }
                                } else {
                                    return null;
                                }
                                break;
                        }
                        parsed.add(new RegexIndex<>(i, new RegexRepetition<>(r1, 0, -1), reference));
                    }
                }
            }
        }
        Collections.sort(parsed);
        RegexIndex[] p1 = new RegexIndex[parsed.size()];
        parsed.toArray(p1);
        //System.out.println(methods.tuple(p1));
        for (int i = 0; i < p1.length; i += 1) {
            if (p1[i] != null) {
                r1 = p1[i].getChild();
                try {
                    if (r1 instanceof RegexAlternation) {
                        if (!p1[i].isReference()) {
                            //System.out.println("ALT_DEL" + methods.tuple(parsed.get(i - 1 - o).getChild(), parsed.get(i + 1 - o).getChild()));
                            if (i < p1.length - 1) {
                                p1[i + 1] = null;
                            }
                            p1[i - 1] = null;
                        }
                    } else if (r1 instanceof RegexSymbolRange) {
                        if (!p1[i].isReference()) {
                            //System.out.println("RNG_DEL" + methods.tuple(parsed.get(i - 1 - o).getChild(), parsed.get(i + 1 - o).getChild()));
                            if (i < p1.length - 1) {
                                p1[i + 1] = null;
                            }
                            p1[i - 1] = null;
                        }
                    } else if (r1 instanceof RegexRepetition) {
                        /*if (p1[i - 1] != null) {
                            r2 = p1[i - 1].getChild();
                        }
                        else {
                            r2 = p1[i - 2].getChild();
                        }
                        p1[i - 1] = null;
                        ((RegexRepetition) r1).setPhrase(r2);
                        System.out.println("REP_DEL : " + i + " : " + methods.tuple(r2, p1[i].getChild()));*/
                        if (p1[i - 1] != null) {
                            p1[i - 1] = null;
                        }
                        else {
                            if (p1[i - 2].getChild() instanceof RegexConcatenation || p1[i - 2].getChild() instanceof RegexAlternation){
                                if (p1[i - 2].getChild().getLastChild() == ((RegexRepetition) r1).getPhrase()) {
                                    p1[i - 2].getChild().setLastChild(((RegexRepetition) r1).getPhrase());
                                    p1[i] = null;
                                }
                            }
                        }
                        //System.out.println("REP_DEL : " + i);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    break;
                }
            }
        }
        //System.out.println(methods.tuple(p1));
        ArrayList<RegexIndex> concise = new ArrayList<>();
        for (RegexIndex ra : p1) {
            if (ra != null) {
                concise.add(ra);
            }
        }

        //System.out.println(methods.tuple(concise));
        //System.out.println(phrase + ": " + parsed.size());
        RegexChild[] rc = new RegexChild[concise.size()];
        for (int i = 0; i < rc.length; i += 1) {
            rc[i] = concise.get(i).getChild();
        }
        return rc;
    }

    public <U extends Character> RegexChild<U> analyse() {
        RegexChild<U>[] rl;
        switch (bracket) {
            case '[' -> {
                rl = parsePhrase('-', '*');
                if (rl.length == 1) {
                    return rl[0];
                }
                return new RegexAlternation<>(rl);
            }
            default -> {
                rl = parsePhrase('*', '|');
                if (rl.length == 1) {
                    return rl[0];
                }
                return new RegexConcatenation<>(rl);
            }
        }
    }

    public String toString() {
        return stringMethods.tuple(phrase, start, end, children);
    }
}
