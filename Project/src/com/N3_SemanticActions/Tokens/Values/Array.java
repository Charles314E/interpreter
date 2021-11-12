package com.N3_SemanticActions.Tokens.Values;

import com.N3_SemanticActions.Tokens.Expression;
import com.N3_SemanticActions.Tokens.Statements.VirtualList;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Value;
import com.N3_SemanticActions.Visitor;
import com.Methods.stringMethods;

import java.util.ArrayList;

public class Array extends Value {
    private ArrayList<Expression> array;
    public Array() {}
    public Array(ArrayList<Expression> array) {
        this.array = array;
    }

    public ArrayList<Expression> getArray() {
        return array;
    }

    public Array construct(ArrayList<Token> arguments) {
        array = new ArrayList<>();
        for (Token expression : arguments) {
            if (expression instanceof Expression) {
                array.add((Expression) expression);
            }
            else if (expression instanceof VirtualList) {
                array.addAll(((VirtualList) expression).getArray());
            }
        }
        return this;
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }

    public String toString() {
        return "Array";
    }
    public String toString(int level) {
        return stringMethods.encapsulatedTuple("[", "]", ", ", array.toArray());
    }

    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < array.size(); i += 1) {
            hash = hash * 65599 + array.get(i).hashCode();
        }
        return hash;
    }
}
