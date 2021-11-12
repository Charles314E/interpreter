package com.N3_SemanticActions.Tokens;

import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public class Phrase extends Token {
    protected ArrayList<Statement> statements;
    public Phrase() {}
    public Phrase(ArrayList<Statement> statements) {
        this.statements = statements;
    }

    public Value accept(Visitor v) {
        return v.visit(this);
    }

    public ArrayList<Statement> getStatements() {
        return statements;
    }

    public Phrase construct(ArrayList<Token> arguments) {
        statements = new ArrayList<>();
        for (Token statement : arguments) {
            if (statement instanceof Statement) {
                statements.add((Statement) statement);
            }
            else if (statement instanceof Phrase) {
                statements.addAll(((Phrase) statement).statements);
            }
        }
        System.out.println(statements);
        return this;
    }

    public String toString() {
        return "Phrase";
    }
    public String toString(int level) {
        StringBuilder sb = new StringBuilder();
        for (Statement statement : statements) {
            sb.append(statement.toString(level));
        }
        return sb.toString();
    }
}
