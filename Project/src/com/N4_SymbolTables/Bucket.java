package com.N4_SymbolTables;

import com.N3_SemanticActions.Tokens.Values.Identifier;

//Adapted from the bucket implementation on page 106 of "Modern Compiler Implementation in Java", by Andrew W. Appel
public class Bucket<B extends Binding> {
    private final Identifier key;
    private final B binding;
    private final Bucket<B> next;
    public Bucket(Identifier key, B binding, Bucket<B> next) {
        this.key = key;
        this.binding = binding;
        this.next = next;
    }

    public Identifier getKey() {
        return key;
    }
    public B getBinding() {
        return binding;
    }
    public Bucket<B> getNext() {
        return next;
    }
}
