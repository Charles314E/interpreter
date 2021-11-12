package com.N1_Lexer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContextToken {
    private final Lexer.type type;
    private String context = null;
    private int line = -1;
    private int column = -1;
    private List<ContextToken> children = new ArrayList<>();
    public ContextToken(Lexer.type type) {
        this.type = type;
    }
    public ContextToken(Lexer.type type, int line, int column, String context) {
        this.type = type;
        this.context = context;
        this.line = line;
        this.column = column;
    }
    public ContextToken(Lexer.type type, ContextToken ... children) {
        this.type = type;
        this.children = Arrays.asList(children);
    }

    public Lexer.type getType() {
        return type;
    }
    public String getContext() {
        return context;
    }
    public Point getPosition() {
        return new Point(line, column);
    }
    public List<ContextToken> getChildren() {
        return children;
    }
    public void addChild(ContextToken token) {
        children.add(token);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder().append(type);
        if (context != null) {
            sb.append("(").append("'").append(context).append("'");
            if (!(column == -1 || line == -1)) {
                sb.append(":").append(line).append(":").append(column);
            }
            sb.append(")");
        }
        sb.append("#").append(hashCode());
        return sb.toString();
    }
}
