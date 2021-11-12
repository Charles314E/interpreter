package com.N3_SemanticActions.Tokens.Statements;

import com.N3_SemanticActions.Tokens.*;
import com.N3_SemanticActions.Visitor;
import com.Methods.stringMethods;

import java.util.ArrayList;

public class WhileLoop extends LoopStatement {
    private Equation condition;
    public WhileLoop() {}

    public Expression getCondition() {
        return condition;
    }

    public Value accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public Value loopThroughBody(Visitor.Interpreter v) {
        if (condition.accept(v).accept(v)) {
            Value current = body.accept(v);
            return (current != null ? current : loopThroughBody(v));
        }
        return null;
    }

    public WhileLoop construct(ArrayList<Token> arguments) {
        System.out.println(stringMethods.tuple(toString(), arguments));
        if (arguments.size() == 2) {
            condition = (Equation) arguments.get(0);
            body = (Phrase) arguments.get(1);
            return this;
        }
        return null;
    }

    public String toString() {
        return "While Loop";
    }
    public String toString(int level) {
        StringBuilder sb = new StringBuilder(stringMethods.indent(level * 2)).append("while ").append(condition.toString(0));
        sb.append(" {").append("\n").append(body.toString(level + 1)).append(stringMethods.indent(level * 2)).append("}").append("\n");
        return sb.toString();
    }
}
