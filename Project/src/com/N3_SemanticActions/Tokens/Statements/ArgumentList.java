package com.N3_SemanticActions.Tokens.Statements;

import com.N3_SemanticActions.Tokens.Statement;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Values.Identifier;
import com.N3_SemanticActions.Visitor;
import com.Methods.stringMethods;

import java.util.ArrayList;

public class ArgumentList extends Statement {
    private ArrayList<Identifier> array;
    public ArgumentList() {}

    public ArrayList<Identifier> getArray() {
        return array;
    }

    public ArgumentList construct(ArrayList<Token> arguments) {
        array = new ArrayList<>();
        for (Token expression : arguments) {
            if (expression instanceof Identifier) {
                array.add((Identifier) expression);
            }
            else if (expression instanceof ArgumentList) {
                array.addAll(((ArgumentList) expression).array);
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
        return stringMethods.tuple(array.toArray());
    }
}
