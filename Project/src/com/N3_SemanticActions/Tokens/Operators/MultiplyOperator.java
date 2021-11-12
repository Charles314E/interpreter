package com.N3_SemanticActions.Tokens.Operators;

import com.N3_SemanticActions.Tokens.Expression;
import com.N3_SemanticActions.Tokens.Operator;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Values.BooleanLiteral;
import com.N3_SemanticActions.Tokens.Values.FloatLiteral;
import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public class MultiplyOperator extends Operator {
    public MultiplyOperator() {}
    public MultiplyOperator(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Expression getMultiplicand() {
        return e1;
    }
    public Expression getMultiplier() {
        return e2;
    }

    public FloatLiteral accept(Visitor v) {
        return v.visit(this);
    }

    public MultiplyOperator construct(ArrayList<Token> arguments) {
        return (assignParameters(arguments) ? this : null);
    }

    public String toString() {
        return "Multiply";
    }

    public String toString(int level) {
        StringBuilder sb = new StringBuilder().append("(").append(e1.toString(0)).append(" * ").append(e2.toString(0)).append(")");
        return sb.toString();
    }
}
