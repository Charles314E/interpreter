package com.N3_SemanticActions.Tokens;

import com.N3_SemanticActions.Visitor;

public abstract class Value extends Expression {
    protected Class<? extends Value> type;
    protected String value;

    public Class<? extends Value> getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public abstract Object accept(Visitor v);
    public String toString(int level) {
        return value;
    }
}