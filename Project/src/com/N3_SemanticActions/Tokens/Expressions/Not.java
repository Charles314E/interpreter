package com.N3_SemanticActions.Tokens.Expressions;

import com.N3_SemanticActions.Tokens.Equation;
import com.N3_SemanticActions.Tokens.Expression;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Values.BooleanLiteral;
import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public class Not extends Expression {
    private Equation ex;
    public Not(Equation ex) {
        this.ex = ex;
    }

    public Equation getSubject() {
        return ex;
    }

    public Not construct(ArrayList<Token> arguments) {
        return this;
    }

    public BooleanLiteral accept(Visitor v) {
        return v.visit(this);
    }
}
