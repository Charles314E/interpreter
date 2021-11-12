package com.N3_SemanticActions.Tokens.Values;

import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Value;
import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public class Identifier extends Value {
    private String name;
    public Identifier() {
        type = Identifier.class;
    }
    public Identifier(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Value accept(Visitor v) {
        return v.visit(this);
    }

    public Identifier construct(ArrayList<Token> arguments) {
        return this;
    }
    public Identifier construct(Token blueprint) {
        name = blueprint.getToken().getContext();
        return this;
    }

    public String toString() {
        return "Identifier";
    }
    public String toString(int level) {
        return name;
    }

    public boolean equals(Object o) {
        if (o instanceof Identifier) {
            return hashCode() == o.hashCode();
        }
        return false;
    }

    //Taken from "Modern Compiler Implementation in Java", by Andrew W. Appel
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < name.length(); i += 1) {
            hash = hash * 65599 + name.charAt(i);
        }
        return hash;
    }
}