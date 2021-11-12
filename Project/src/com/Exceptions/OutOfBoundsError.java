package com.Exceptions;

import com.N3_SemanticActions.Tokens.Token;

public class OutOfBoundsError extends ArtException {
    public final Token token;
    public OutOfBoundsError(Token token) {
        this.token = token;
    }
}