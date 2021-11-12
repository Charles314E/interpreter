package com.N1_Lexer;

public class ArtLexer extends Lexer<Character> {
    public ArtLexer() {
        addToken(type.WHITESPACE, "[\t ]*");
        addToken(type.IDENTIFIER, "[[a-z][A-Z]][[a-z][A-Z][_][0-9]]*");
        addToken(type.INTEGER, "[[\\-][ε]][0-9][0-9]*");
        addToken(type.REAL, "[[\\-][ε]][0-9][0-9]*[.][0-9][0-9]*");
        addToken(type.NULL, "null");
        addToken(type.BOOLEAN, "(true)|(false)");
        addToken(type.STRING, "['][[0-9][a-z][A-Z][ ][\\-][\\*][\\|][\\(][\\)][\\[][\\]][\\{][\\}][\\?][!£$%^&:;@~#.,+=_/<>]]*[']");
        addToken(type.NEW_LINE, "@n");
        addToken(type.IF, "(if)|(IF)");
        addToken(type.ELSE_IF, "(elif)|(ELIF)");
        addToken(type.ELSE, "(else)|(ELSE)");
        addToken(type.FOR, "(for)|(FOR)");
        addToken(type.WHILE, "(while)|(WHILE)");
        addToken(type.DEFINITION, "(def)|(DEF)");
        addToken(type.VAR, "(var)|(VAR)");
        addToken(type.RETURN, "(return)|(RETURN)");
        addToken(type.TO, "...");
        addToken(type.ASSIGN, "=");
        addToken(type.EQUALS, "==");
        addToken(type.NOT_EQUALS, "!=");
        addToken(type.LESS_THAN, "<");
        addToken(type.GREATER_THAN, ">");
        addToken(type.LESS_THAN_EQUALS, "<=");
        addToken(type.GREATER_THAN_EQUALS, ">=");
        addToken(type.AND, "(and)|(AND)");
        addToken(type.OR, "(or)|(OR)");
        addToken(type.NOT, "(not)|(NOT)");
        addToken(type.IN, "(in)|(IN)");
        addToken(type.PLUS, "+");
        addToken(type.MINUS, "\\-");
        addToken(type.MULTIPLY, "\\*");
        addToken(type.DIVIDE, "/");
        //addToken(type.INT_DIVIDE, "//");
        addToken(type.POWER, "power");
        addToken(type.MODULUS, "%");
        addToken(type.LEFT_BRACKET, "\\(");
        addToken(type.RIGHT_BRACKET, "\\)");
        addToken(type.LEFT_SQUARE_BRACKET, "\\[");
        addToken(type.RIGHT_SQUARE_BRACKET, "\\]");
        addToken(type.LEFT_CURLY_BRACKET, "\\{");
        addToken(type.RIGHT_CURLY_BRACKET, "\\}");
        addToken(type.COMMA, ",");
        addToken(type.DOT, ".");
        addToken(type.COLON, ":");
        addToken(type.SEMICOLON, ";");
    }
}
