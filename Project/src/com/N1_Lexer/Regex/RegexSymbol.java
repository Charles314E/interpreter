package com.N1_Lexer.Regex;

public class RegexSymbol<T extends Character> extends RegexChild<T> {
    protected T symbol;

    public RegexSymbol(T symbol) {
        if ((Character) symbol == 'ε') {
            this.symbol = null;
        }
        else {
            this.symbol = symbol;
        }
    }

    public T getSymbol() {
        return symbol;
    }
    public boolean check(String input) {
        return check(input, 0);
    }

    public String toString() {
        return "[" + symbol + "]";
    }
}
