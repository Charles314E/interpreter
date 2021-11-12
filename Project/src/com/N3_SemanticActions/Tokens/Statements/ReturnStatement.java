package com.N3_SemanticActions.Tokens.Statements;

import com.N3_SemanticActions.Tokens.Expression;
import com.N3_SemanticActions.Tokens.Statement;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Value;
import com.N3_SemanticActions.Visitor;
import com.Methods.stringMethods;

import java.util.ArrayList;

public class ReturnStatement extends Statement {
    private Expression output;
    public ReturnStatement() {}

    public Expression getExpression() {
        return output;
    }

    public Value accept(Visitor v) {
        return v.visit(this);
    }

    public ReturnStatement construct(ArrayList<Token> arguments) {
        if (arguments.size() == 1) {
            output = (Expression) arguments.get(0);
            return this;
        }
        return null;
    }

    public String toString() {
        return "Return";
    }
    public String toString(int level) {
        StringBuilder sb = new StringBuilder(stringMethods.indent(level * 2)).append("return ").append(output.toString(0));
        return sb.toString();
    }
}
