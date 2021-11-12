package com.N1_Lexer.Regex;

import com.Methods.stringMethods;

public abstract class Regex<T extends Character> {
    protected RegexChild<T>[] children;
    protected char[] pattern;

    public RegexChild<T>[] getChildren() {
        return children;
    }
    public abstract boolean check(String input);
    public boolean check(String input, int i) {
        return check(input);
    }
    public char[] getPattern() {
        return pattern;
    }

    public String getName() {
        return getClass().getSimpleName().replace("Regex", "");
    }
    public String toString() {
        if (children != null) {
            return getName() + stringMethods.tuple(children);
        }
        return getName();
    }
}
