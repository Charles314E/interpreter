package com.N3_SemanticActions.Tokens.Statements;

import com.N3_SemanticActions.Tokens.Phrase;
import com.N3_SemanticActions.Tokens.Statement;
import com.N3_SemanticActions.Tokens.Value;
import com.N3_SemanticActions.Visitor;

public abstract class LoopStatement extends Statement {
    protected Phrase body;

    public Phrase getBody() {
        return body;
    }

    public abstract Value loopThroughBody(Visitor.Interpreter v);
}
