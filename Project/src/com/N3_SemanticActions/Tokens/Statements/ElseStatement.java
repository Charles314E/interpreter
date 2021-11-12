package com.N3_SemanticActions.Tokens.Statements;

import com.N3_SemanticActions.Tokens.Phrase;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Values.BooleanLiteral;
import com.N3_SemanticActions.Visitor;
import com.Methods.stringMethods;

import java.util.ArrayList;

public class ElseStatement extends Conditional {
    private Phrase result;
    public ElseStatement() {}

    public Phrase getResult() {
        return result;
    }

    public BooleanLiteral accept(Visitor v) {
        return v.visit(this);
    }

    public ElseStatement construct(ArrayList<Token> arguments) {
        if (arguments.size() == 1) {
            result = (Phrase) arguments.get(0);
            return this;
        }
        return this;
    }

    public String toString() {
        return "Else Statement";
    }
    public String toString(int level) {
        StringBuilder sb = new StringBuilder(stringMethods.indent(level * 2)).append("else ");
        sb.append(" {").append("\n").append(result.toString(level + 1)).append(stringMethods.indent(level * 2)).append("}").append("\n");
        return sb.toString();
    }
}
