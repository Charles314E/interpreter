package com.Exceptions;

import com.N3_SemanticActions.Tokens.Values.Identifier;

public class UndefinedError extends ArtException {
    private final Identifier identifier;
    public UndefinedError(Identifier identifier) {
        this.identifier = identifier;
    }

    public String getMessage() {
        return "The variable '" + identifier.getName() + "' is undefined.";
    }
}
