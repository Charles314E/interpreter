package com.N3_SemanticActions.Tokens.Statements;

import com.N3_SemanticActions.Tokens.Expression;
import com.N3_SemanticActions.Tokens.Statement;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Visitor;
import com.Methods.stringMethods;

import java.util.ArrayList;

public class VirtualList extends Statement {
    private ArrayList<Expression> array;

    public VirtualList() {}
    public VirtualList(Expression id) {
        array = new ArrayList<>();
        array.add(id);
    }

    public ArrayList<Expression> getArray() {
        return array;
    }

    public VirtualList construct(ArrayList<Token> arguments) {
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
}
