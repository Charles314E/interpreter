package com.N3_SemanticActions.Tokens.Values;

import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Value;
import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public class FloatLiteral extends Value {
    public FloatLiteral() {
        type = FloatLiteral.class;
    }
    public FloatLiteral(Double value) {
        this();
        this.value = Double.toString(value);
    }

    public Double accept(Visitor v) {
        return v.visit(this);
    }

    public FloatLiteral construct(ArrayList<Token> arguments) {
        return this;
    }
    public FloatLiteral construct(Token blueprint) {
        value = blueprint.getToken().getContext();
        return this;
    }

    public String toString() {
        return "Float";
    }
    public String toString(int level) {
        return value;
    }
}
