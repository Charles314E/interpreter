package com.N3_SemanticActions.Tokens.Values;

import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Value;
import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public class BooleanLiteral extends Value {
    public BooleanLiteral() {
        type = BooleanLiteral.class;
    }
    public BooleanLiteral(boolean value) {
        this();
        this.value = Boolean.toString(value);
    }

    public BooleanLiteral construct(ArrayList<Token> arguments) {
        return this;
    }

    public Boolean accept(Visitor v) {
        return v.visit(this);
    }
}