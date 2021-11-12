package com.N3_SemanticActions.Tokens.Values;

import com.N3_SemanticActions.Tokens.*;
import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public class Item extends Value {
    private Identifier id;
    private Expression index;
    public Item() {
        type = Item.class;
    }

    public Identifier getIdentifier() {
        return id;
    }
    public Expression getIndex() {
        return index;
    }

    public Value accept(Visitor v) {
        return v.visit(this);
    }

    public Item construct(ArrayList<Token> arguments) {
        if (arguments.size() == 2) {
            id = (Identifier) arguments.get(0);
            index = (Expression) arguments.get(1);
            return this;
        }
        return null;
    }

    public String toString() {
        return "Identifier";
    }
    public String toString(int level) {
        return id.toString(0) + "[" + index.toString(0) + "]";
    }

    public boolean equals(Object o) {
        if (o instanceof Item) {
            return hashCode() == o.hashCode();
        }
        return false;
    }

    public int hashCode() {
        return id.hashCode() + index.hashCode();
    }
}