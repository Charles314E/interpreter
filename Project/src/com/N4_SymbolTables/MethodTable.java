package com.N4_SymbolTables;

import com.N3_SemanticActions.Tokens.Statements.MethodDefinition;
import com.N3_SemanticActions.Tokens.Values.Identifier;
import com.Classes.ObjectFactory;

public class MethodTable extends HashTable<MethodBinding> {
    public MethodTable() {
        this.factory = new ObjectFactory<>(MethodBinding.class);
    }

    public MethodBinding insert(Identifier s, MethodDefinition def) {
        MethodBinding binding = (MethodBinding) super.insert(s, def.getBody());
        binding.setArguments(def.getArguments());
        return binding;
    }

    public MethodBinding lookup(Identifier s) {
        return super.lookup(s);
    }
}
