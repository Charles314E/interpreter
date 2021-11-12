package com.N4_SymbolTables;

import com.N3_SemanticActions.Tokens.Phrase;
import com.N3_SemanticActions.Tokens.Statements.ArgumentList;
import com.N3_SemanticActions.Tokens.Values.Identifier;
import com.N3_SemanticActions.Visitor;

public class MethodBinding extends Binding {
    private Phrase body;
    private ArgumentList arguments;
    public Phrase getBody() {
        return body;
    }

    public MethodBinding construct(Object ... obj) {
        if (!constructed) {
            try {
                interpreter = (Visitor.Interpreter) obj[0];
                body = (Phrase) obj[1];
                constructed = true;
            }
            catch (ClassCastException e) {
                return this;
            }
        }
        return this;
    }

    public void setArguments(ArgumentList args) {
        this.arguments = args;
    }

    public int getArgumentLength() {
        return arguments.getArray().size();
    }
    public Identifier getArgument(int i) {
        return arguments.getArray().get(i);
    }
}
