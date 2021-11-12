package com.N3_SemanticActions.Tokens.Expressions;

import com.N3_SemanticActions.Tokens.Equation;
import com.N3_SemanticActions.Tokens.Phrase;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Values.BooleanLiteral;
import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public class In extends Equation {
    private Phrase list;
    public In(Phrase l) {
        this.list = l;
    }

    public Phrase getList() {
        return list;
    }

    public In construct(ArrayList<Token> arguments) {
        return this;
    }

    public BooleanLiteral accept(Visitor v) {
        return v.visit(this);
    }
}
