package com.N3_SemanticActions.Tokens.Expressions;

import com.N3_SemanticActions.Tokens.Equation;
import com.N3_SemanticActions.Tokens.Expression;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Values.BooleanLiteral;
import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public class Or extends Expression {
    private Equation e1;
    private Equation e2;
    public Or(Equation e1, Equation e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Equation getPredicate() {
        return e1;
    }
    public Equation getSubject() {
        return e2;
    }

    public Or construct(ArrayList<Token> arguments) {
        return this;
    }

    public BooleanLiteral accept(Visitor v) {
        return v.visit(this);
    }
}
