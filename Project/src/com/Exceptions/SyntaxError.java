package com.Exceptions;

import com.N1_Lexer.ContextToken;

import java.awt.*;

public class SyntaxError extends ArtException {
    public final ContextToken token;
    public SyntaxError(ContextToken token) {
        this.token = token;
    }

    public String getMessage() {
        try {
            Point pos = token.getPosition();
            return "At line " + pos.x + ", column " + pos.y + ", at token '" + token.getContext() + "'.";
        }
        catch (NullPointerException e) {
            return "";
        }
    }
}
