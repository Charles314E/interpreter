package com.N1_Lexer.Regex;

import com.Methods.stringMethods;

public class RegexAlternation<T extends Character> extends RegexChild<T> {
    public RegexAlternation(RegexChild ... children) {
        try {
            this.children = children;
        }
        catch (ClassCastException e) {
            System.out.println("[ERR]: " + e.getMessage());
        }
    }

    @Override
    public boolean check(String input) {
        int i = 0;
        for (Regex<T> child : children) {
            if (child.check(input, i)) {
                return true;
            }
            i += 1;
        }
        return false;
    }

    public String toString() {
        return stringMethods.encapsulatedTuple("[", "]", "", children);
    }
}
