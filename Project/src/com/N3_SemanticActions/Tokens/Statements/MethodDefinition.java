package com.N3_SemanticActions.Tokens.Statements;

import com.N3_SemanticActions.Tokens.Phrase;
import com.N3_SemanticActions.Tokens.Statement;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Values.Identifier;
import com.N3_SemanticActions.Visitor;
import com.Methods.stringMethods;

import java.util.ArrayList;

public class MethodDefinition extends Statement {
    private Identifier id;
    private ArgumentList arguments;
    private Phrase body;

    public MethodDefinition() {}

    public Identifier getIdentifier() {
        return id;
    }
    public ArgumentList getArguments() {
        return arguments;
    }
    public Phrase getBody() {
        return body;
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }

    public MethodDefinition construct(ArrayList<Token> arguments) {
        if (arguments.size() == 3) {
            id = (Identifier) arguments.get(0);
            this.arguments = (ArgumentList) arguments.get(1);
            this.body = (Phrase) arguments.get(2);
            return this;
        }
        return null;
    }

    public String toString() {
        return "Method Definition";
    }
    public String toString(int level) {
        StringBuilder sb = new StringBuilder(stringMethods.indent(level * 2)).append("def ").append(id.toString(0)).append(arguments.toString(0));
        sb.append(" {").append("\n").append(body.toString(level + 1)).append("\n").append(stringMethods.indent(level * 2)).append("}").append("\n");
        return sb.toString();
    }
}
