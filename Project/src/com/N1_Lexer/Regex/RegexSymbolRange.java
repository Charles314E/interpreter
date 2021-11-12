package com.N1_Lexer.Regex;

public class RegexSymbolRange<T extends Character> extends RegexAlternation<T> {
    public RegexSymbolRange(char first, char last) {
        if (last > first) {
            children = new RegexSymbol[last - first + 1];
            Character k;
            for (int i = 0; i < children.length; i += 1) {
                k = (char) ((int) first + i);
                try {
                    children[i] = new RegexSymbol<>((T) k);
                }
                catch (ClassCastException e) {
                    System.out.println("[ERR]: " + e.getMessage());
                }
            }
        }
    }

    public String toString() {
        try {
            return "[" + ((RegexSymbol<T>) getFirstChild()).getSymbol() + "-" + ((RegexSymbol<T>) getLastChild()).getSymbol() + "]";
        }
        catch (ClassCastException e) {
            return null;
        }
    }
}
