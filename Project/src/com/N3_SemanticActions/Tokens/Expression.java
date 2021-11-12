package com.N3_SemanticActions.Tokens;

import com.N3_SemanticActions.Visitor;

public abstract class Expression extends Statement {
    public abstract Object accept(Visitor v);

    public boolean equals(Expression o, Visitor.Interpreter v) {
        try {
            v.getValue((Expression) accept(v)).accept(v).equals(v.getValue((Expression) o.accept(v)).accept(v));
        }
        catch (ClassCastException e) {
            return accept(v).equals(o.accept(v));
        }
        return false;
    }
}
