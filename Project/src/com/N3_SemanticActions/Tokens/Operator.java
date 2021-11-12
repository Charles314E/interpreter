package com.N3_SemanticActions.Tokens;

import java.util.ArrayList;

public abstract class Operator extends Expression {
    protected Expression e1;
    protected Expression e2;
    protected String operator;

    public boolean assignParameters(ArrayList<Token> arguments) {
        if (arguments.size() == 2) {
            e1 = (Expression) arguments.get(0);
            e2 = (Expression) arguments.get(1);
            return true;
        }
        return false;
    }
}