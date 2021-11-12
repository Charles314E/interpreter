package com.N3_SemanticActions.Tokens.Statements;

import com.N3_SemanticActions.Tokens.Phrase;
import com.N3_SemanticActions.Tokens.Statement;
import com.N3_SemanticActions.Tokens.Values.BooleanLiteral;
import com.N3_SemanticActions.Visitor;

public abstract class Conditional extends Statement {
    public abstract Phrase getResult();
    public abstract BooleanLiteral accept(Visitor v);
}
