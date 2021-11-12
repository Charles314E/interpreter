package com.N4_SymbolTables;

import com.N3_SemanticActions.Visitor;

public abstract class Binding {
    protected Visitor.Interpreter interpreter;
    protected boolean constructed = false;

    public abstract <B extends Binding> B construct(Object ... obj);
}
