package com.N3_SemanticActions.Tokens;

import com.N3_SemanticActions.Tokens.Values.BooleanLiteral;
import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public abstract class Equation extends Expression {
    protected Expression e1;
    protected Expression e2;
    protected String operator;
    
    public abstract BooleanLiteral accept(Visitor v);

    public boolean assignParameters(Token blueprint) {
        return assignParameters(blueprint.buildChildren());
    }
    public boolean assignParameters(ArrayList<Token> arguments) {
        if (arguments.size() == 2) {
            e1 = (Expression) arguments.get(0);
            e2 = (Expression) arguments.get(1);
            return true;
        }
        return false;
    }

    public String toString(int level) {
        StringBuilder sb = new StringBuilder().append("(").append(e1.toString(0)).append(" ").append(operator).append(" ").append(e2.toString(0)).append(")");
        return sb.toString();
    }
    public String createCode(int level) {
        StringBuilder sb = new StringBuilder().append(e1.createCode(0)).append(" ").append(operator).append(" ").append(e2.createCode(0));
        return sb.toString();
    }
}
