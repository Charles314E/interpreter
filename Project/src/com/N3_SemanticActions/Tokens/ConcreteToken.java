package com.N3_SemanticActions.Tokens;

import com.N1_Lexer.ContextToken;

import java.util.ArrayList;

public class ConcreteToken extends Token {
    public ConcreteToken() {

    }
    public ConcreteToken(ContextToken name, Token ... children) {
        super(name, children);
    }

    @Override
    public Token construct(ArrayList<Token> arguments) {
        System.out.println(arguments);
        return null;
    }
}
