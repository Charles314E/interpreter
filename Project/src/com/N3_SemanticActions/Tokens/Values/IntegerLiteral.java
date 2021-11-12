package com.N3_SemanticActions.Tokens.Values;

import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Value;
import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public class IntegerLiteral extends Value {
    public IntegerLiteral() {
        type = IntegerLiteral.class;
    }
    public IntegerLiteral(int value) {
        this();
        this.value = Integer.toString(value);
    }

    public Double accept(Visitor v) {
        return v.visit(this);
    }

    public IntegerLiteral construct(ArrayList<Token> arguments) {
        return this;
    }
    public IntegerLiteral construct(Token blueprint) {
        value = blueprint.getToken().getContext();
        return this;
    }

    public String toString() {
        return "Integer";
    }
    public String toString(int level) {
        return value;
    }
}
