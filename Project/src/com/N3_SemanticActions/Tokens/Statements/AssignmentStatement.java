package com.N3_SemanticActions.Tokens.Statements;

import com.N3_SemanticActions.Tokens.Expression;
import com.N3_SemanticActions.Tokens.Statement;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Values.Identifier;
import com.N3_SemanticActions.Visitor;
import com.Methods.stringMethods;

import java.util.ArrayList;

public class AssignmentStatement extends Statement {
    private Identifier id;
    private Expression value;

    public Identifier getIdentifier() {
        return id;
    }
    public Expression getValue() {
        return value;
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }

    public AssignmentStatement construct(ArrayList<Token> arguments) {
        if (arguments.size() == 2) {
            id = (Identifier) arguments.get(0);
            value = (Expression) arguments.get(1);
            return this;
        }
        return null;
    }

    public String toString() {
        return "Assign";
    }
    public String toString(int level) {
        StringBuilder sb = new StringBuilder(stringMethods.indent(level * 2)).append(id.toString(0)).append(" = ").append(value.toString(0)).append("\n");
        return sb.toString();
    }
}
