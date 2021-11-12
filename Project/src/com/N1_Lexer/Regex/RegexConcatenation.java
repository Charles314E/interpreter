package com.N1_Lexer.Regex;

import com.Methods.stringMethods;

public class RegexConcatenation<T extends Character> extends RegexChild<T> {
    public RegexConcatenation(RegexChild<T> ... children) {
        this.children = children;
    }

    @Override
    public boolean check(String input) {
        for (Regex child : children) {
            if (!child.check(input)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return stringMethods.encapsulatedTuple("(", ")", "", children);
    }
}
