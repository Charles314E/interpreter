package com.N3_SemanticActions.Tokens;

import com.N1_Lexer.ContextToken;
import com.N3_SemanticActions.Visitor;
import com.Classes.ObjectFactory;
import com.Methods.stringMethods;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Token {
    private ContextToken name;
    private List<Token> children = new ArrayList<>();
    private Token parent;
    private int line = -1, column = -1;

    public Token() {

    }
    public Token(ContextToken name, Token ... children) {
        this.name = name;
        for (Token t : children) {
            this.children.add(t);
        }
    }

    public void setName(ContextToken name) {
        this.name = name;
    }

    public List<Token> getChildren() {
        return children;
    }

    public void addChild(Token token) {
        children.add(token);
        token.setParent(this);
    }

    private void setParent(Token token) {
        parent = token;
    }
    public Token getParent() {
        return parent;
    }

    public Token getChild(int n) {
        return children.get(n);
    }

    public ContextToken getToken() {
        return name;
    }

    public String toString() {
        try {
            return name.getType().getClassName() + "#" + hashCode() + "#"+ name.hashCode() + "#" + (name.getContext() == null ? "" : name.getContext());
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public String toString(int level) {
        StringBuilder sb = new StringBuilder(stringMethods.indent(level)).append(toString()).append("\n");
        for (Token child : children) {
            sb.append(child.toString(level + 1));
        }
        return sb.toString();
    }
    public String createCode(int level) {
        return null;
    }

    public String stringChildren(int level) {
        StringBuilder sb = new StringBuilder(stringMethods.indent(level)).append(toString()).append("\n");
        for (Token child : children) {
            sb.append(child.stringChildren(level + 1));
        }
        return sb.toString();
    }

    public Object accept(Visitor v) {
        return null;
    }

    public ArrayList<Token> buildChildren() {
        ArrayList<Token> tokens = new ArrayList<>();
        for (Token child : children) {
            tokens.addAll(child.buildFromToken());
        }
        return tokens;
    }

    public ArrayList<Token> buildFromToken() {
        ObjectFactory<Token> factory = name.getType().getTokenFactory();
        if (factory.getObjectClass() != null) {
            ArrayList<Token> result = new ArrayList<>();
            result.add(factory.makeObject().construct(this));
            Point p = name.getPosition();
            result.get(0).line = p.x;
            result.get(0).column = p.y;
            return result;
        }
        return buildChildren();
    }

    public abstract Token construct(ArrayList<Token> arguments);
    public Token construct(Token blueprint) {
        name = blueprint.name;
        return construct(blueprint.buildChildren());
    }

    public Token attemptToConstruct() {
        return buildChildren().get(0);
    }

    public int getLine() {
        return line;
    }
    public int getColumn() {
        return column;
    }
}
