package com.N1_Lexer;

public class TestLexer extends Lexer<Character> {
    public TestLexer() {
        addToken(type.PLUS, "+");
        addToken(type.LEFT_BRACKET, "\\(");
        addToken(type.RIGHT_BRACKET, "\\)");
        addToken(type.IDENTIFIER, "[[a-z][A-Z]][[a-z][A-Z]_[0-9]]*");
        addToken(type.COMMA, ",");
    }
}
