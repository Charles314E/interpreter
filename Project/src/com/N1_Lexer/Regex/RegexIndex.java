package com.N1_Lexer.Regex;

public class RegexIndex<T extends Character> implements Comparable<RegexIndex<T>> {
    private final int i;
    private final RegexChild<T> child;
    private final boolean reference;
    public RegexIndex(int i, RegexChild<T> child, boolean reference) {
        this.i = i;
        this.child = child;
        this.reference = reference;
    }

    public RegexChild<T> getChild() {
        return child;
    }
    public int compareTo(RegexIndex other) {
        return (this.i - other.i);
    }
    public boolean isReference() {
        return reference;
    }

    @Override
    public String toString() {
        return child.getName();
    }
}
