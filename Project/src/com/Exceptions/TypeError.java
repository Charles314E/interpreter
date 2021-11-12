package com.Exceptions;

import com.N3_SemanticActions.Tokens.Token;

public class TypeError extends ArtException {
    public final Token token;
    public TypeError(Token token) {
        this.token = token;
    }

    public String getMessage() {
        if (token.getLine() == -1) {
            return " with token '" + token.toString(0) + "'.";
        }
        return " with token '" + token.toString(0) + "', at line " + token.getLine() + ", column " + token.getColumn() + ".";
    }
}