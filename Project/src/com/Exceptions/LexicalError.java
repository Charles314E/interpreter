package com.Exceptions;

public class LexicalError extends ArtException {
    public final int line;
    public final int column;
    public final char token;
    public LexicalError(int line, int column, char token) {
        this.line = line;
        this.column = column;
        this.token = token;
    }

    public String getMessage() {
        try {
            return "At line " + line + ", column " + column + ", at string '" + token + "'.";
        }
        catch (NullPointerException e) {
            return "";
        }
    }
}