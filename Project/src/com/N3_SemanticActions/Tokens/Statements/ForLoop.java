package com.N3_SemanticActions.Tokens.Statements;

import com.N3_SemanticActions.Tokens.Phrase;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Value;
import com.N3_SemanticActions.Tokens.Values.Identifier;
import com.N3_SemanticActions.Visitor;
import com.Methods.stringMethods;

import java.util.ArrayList;

public class ForLoop extends LoopStatement {
    private Identifier id;
    private Iterator iterator;

    public ForLoop() {}

    public Identifier getIdentifier() {
        return id;
    }

    public Iterator getIterator() {
        return iterator;
    }

    public Value loopThroughBody(Visitor.Interpreter v) {
        return loopThroughBody(v, 0);
    }

    public Value loopThroughBody(Visitor.Interpreter v, int i) {
        try {
            iterator.assignments[i].accept(v);
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
        Value current = body.accept(v);
        return (current != null ? current : loopThroughBody(v, i + 1));
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }

    public ForLoop construct(ArrayList<Token> arguments) {
        if (arguments.size() == 3) {
            id = (Identifier) arguments.get(0);
            iterator = (Iterator) arguments.get(1);
            iterator.loop = this;
            body = (Phrase) arguments.get(2);
            return this;
        }
        return null;
    }

    public String toString() {
        return "For Loop";
    }
    public String toString(int level) {
        StringBuilder sb = new StringBuilder(stringMethods.indent(level * 2)).append("loop ").append(id.toString(0)).append(" through ").append(iterator.toString(0));
        sb.append(" {").append("\n").append(body.toString(level + 1)).append(stringMethods.indent(level * 2)).append("}").append("\n");
        return sb.toString();
    }
}
