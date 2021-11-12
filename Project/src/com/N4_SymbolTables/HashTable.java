package com.N4_SymbolTables;

import com.N3_SemanticActions.Tokens.Values.Identifier;
import com.N3_SemanticActions.Visitor;
import com.Classes.ObjectFactory;

//Adapted from the symbol table implementation on page 106 of "Modern Compiler Implementation in Java", by Andrew W. Appel
public abstract class HashTable<B extends Binding> {
    final int SIZE = 256;
    protected Bucket<B>[] table = new Bucket[SIZE];
    protected Visitor.Interpreter interpreter;
    protected ObjectFactory<Binding> factory;

    public <T extends Object> Binding insert(Identifier s, T value) {
        int index = s.hashCode() % SIZE;
        table[index] = new Bucket<>(s, factory.makeObject().construct(new Object[] {interpreter, value}), table[index]);
        return table[index].getBinding();
    }

    public B lookup(Identifier s) {
        int index = s.hashCode() % SIZE;
        for (Bucket<B> b = table[index]; b != null; b = b.getNext()) {
            if (s.equals(b.getKey())) {
                return b.getBinding();
            }
        }
        return null;
    }

    public Binding pop(Identifier s) {
        int index = s.hashCode() % SIZE;
        try {
            table[index] = table[index].getNext();
            return table[index].getBinding();
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public void setInterpreter(Visitor.Interpreter interpreter) {
        this.interpreter = interpreter;
    }
    public Visitor.Interpreter getInterpreter() {
        return interpreter;
    }
}
