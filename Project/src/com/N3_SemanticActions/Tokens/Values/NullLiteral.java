package com.N3_SemanticActions.Tokens.Values;

import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Value;
import com.N3_SemanticActions.Visitor;

import java.lang.constant.Constable;
import java.util.ArrayList;

public class NullLiteral extends Value {
    public String getValue() {
        return null;
    }

    public Constable accept(Visitor v) {
        return v.visit(this);
    }

    public NullLiteral construct(ArrayList<Token> arguments) {
        return this;
    }
    public NullLiteral construct(Token blueprint) {
        return this;
    }

    public String toString() {
        return "Null";
    }
    public String toString(int level) {
        return null;
    }
}
