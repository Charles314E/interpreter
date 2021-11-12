package com.N4_SymbolTables;

import com.N3_SemanticActions.Tokens.Expression;
import com.N3_SemanticActions.Tokens.Values.Identifier;
import com.Classes.ObjectFactory;

public class VariableTable extends HashTable<VariableBinding> {
    public VariableTable() {
        this.factory = new ObjectFactory<>(VariableBinding.class);
    }

    public VariableBinding insert(Identifier s, Expression value) {
        return (VariableBinding) super.insert(s, value);
    }

    public VariableBinding lookup(Identifier s) {
        return super.lookup(s);
    }
}
