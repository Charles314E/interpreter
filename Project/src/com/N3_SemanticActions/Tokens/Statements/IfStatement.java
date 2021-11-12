package com.N3_SemanticActions.Tokens.Statements;

import com.N3_SemanticActions.Tokens.*;
import com.N3_SemanticActions.Tokens.Values.BooleanLiteral;
import com.N3_SemanticActions.Visitor;
import com.Methods.stringMethods;

import java.util.ArrayList;

public class IfStatement extends Conditional {
    private Equation condition;
    private Phrase result;
    public IfStatement() {}

    public Equation getCondition() {
        return condition;
    }
    public Phrase getResult() {
        return result;
    }

    public BooleanLiteral accept(Visitor v) {
        return v.visit(this);
    }

    public IfStatement construct(ArrayList<Token> arguments) {
        if (arguments.size() == 2) {
            condition = (Equation) arguments.get(0);
            result = (Phrase) arguments.get(1);
            return this;
        }
        return this;
    }

    public String toString() {
        return "If Statement";
    }
    public String toString(int level) {
        StringBuilder sb = new StringBuilder(stringMethods.indent(level * 2)).append("if ").append(condition.toString(0));
        sb.append(" {").append("\n").append(result.toString(level + 1)).append(stringMethods.indent(level * 2)).append("}").append("\n");
        return sb.toString();
    }
}
