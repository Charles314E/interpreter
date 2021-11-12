package com.N3_SemanticActions.Tokens.Values;

import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Value;
import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public class StringLiteral extends Value {
    public StringLiteral() {}

    public String accept(Visitor v) {
        return v.visit(this);
    }

    public StringLiteral construct(ArrayList<Token> arguments) {
        return this;
    }
    public StringLiteral construct(Token blueprint) {
        value = blueprint.getToken().getContext();
        value = value.substring(1, value.length() - 1);
        return this;
    }

    public String toString() {
        return "String";
    }
    public String toString(int level) {
        return "'" + value + "'";
    }
}
