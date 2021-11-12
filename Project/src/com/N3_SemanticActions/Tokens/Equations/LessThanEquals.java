package com.N3_SemanticActions.Tokens.Equations;

import com.N3_SemanticActions.Tokens.Equation;
import com.N3_SemanticActions.Tokens.Expression;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Values.BooleanLiteral;
import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public class LessThanEquals extends Equation {
    public LessThanEquals() {}
    public LessThanEquals(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Expression getPredicate() {
        return e1;
    }
    public Expression getSubject() {
        return e2;
    }

    public BooleanLiteral accept(Visitor v) {
        return v.visit(this);
    }

    public LessThanEquals construct(ArrayList<Token> arguments) {
        return (assignParameters(arguments) ? this : null);
    }

    public String toString() {
        return "Less Than Equals";
    }
    public String toString(int level) {
        StringBuilder sb = new StringBuilder().append("(").append(e1.toString(0)).append(" <= ").append(e2.toString(0)).append(")");
        return sb.toString();
    }
}
