package com.N3_SemanticActions.Tokens.Operators;

import com.N3_SemanticActions.Tokens.Expression;
import com.N3_SemanticActions.Tokens.Operator;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Values.BooleanLiteral;
import com.N3_SemanticActions.Tokens.Values.FloatLiteral;
import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public class MinusOperator extends Operator {
    public MinusOperator() {}
    public MinusOperator(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Expression getSubtrahend() {
        return e1;
    }
    public Expression getMinuend() {
        return e2;
    }

    public FloatLiteral accept(Visitor v) {
        return v.visit(this);
    }

    public MinusOperator construct(ArrayList<Token> arguments) {
        return (assignParameters(arguments) ? this : null);
    }

    public String toString() {
        return "Minus";
    }

    public String toString(int level) {
        StringBuilder sb = new StringBuilder().append("(").append(e1.toString(0)).append(" - ").append(e2.toString(0)).append(")");
        return sb.toString();
    }
}
