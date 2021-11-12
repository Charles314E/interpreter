package com.N3_SemanticActions.Tokens.Statements;

import com.N3_SemanticActions.Tokens.Expression;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Value;
import com.N3_SemanticActions.Tokens.Values.Identifier;
import com.N3_SemanticActions.Visitor;
import com.Methods.stringMethods;

import java.util.ArrayList;

public class MethodCall extends Expression {
    private Identifier id;
    private VirtualList arguments;

    public MethodCall() {}

    public Identifier getIdentifier() {
        return id;
    }

    public Value accept(Visitor v) {
        return v.visit(this);
    }

    public int getArgumentLength() {
        return arguments.getArray().size();
    }
    public Expression getArgument(int i) {
        return arguments.getArray().get(i);
    }

    public MethodCall construct(ArrayList<Token> arguments) {
        if (arguments.size() == 2) {
            id = (Identifier) arguments.get(0);
            try {
                this.arguments = (VirtualList) arguments.get(1);
            }
            catch (ClassCastException e) {
                this.arguments = new VirtualList((Expression) arguments.get(1));
            }
            return this;
        }
        return null;
    }

    public String toString() {
        return "Method Call";
    }
    public String toString(int level) {
        StringBuilder sb = new StringBuilder(stringMethods.indent(level * 2)).append(id.toString(0)).append(stringMethods.tuple(arguments.getArray().toArray()));
        return sb.toString();
    }
}
