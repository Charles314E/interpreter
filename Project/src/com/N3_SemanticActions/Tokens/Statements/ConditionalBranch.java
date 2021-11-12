package com.N3_SemanticActions.Tokens.Statements;

import com.N3_SemanticActions.Tokens.Phrase;
import com.N3_SemanticActions.Tokens.Statement;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public class ConditionalBranch extends Statement {
    protected ArrayList<Conditional> branches;
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    public ArrayList<Conditional> getBranches() {
        return branches;
    }

    public ConditionalBranch construct(ArrayList<Token> arguments) {
        branches = new ArrayList<>();
        for (Token branch : arguments) {
            if (branch instanceof Conditional) {
                branches.add((Conditional) branch);
            }
            else if (branch instanceof ConditionalBranch) {
                branches.addAll(((ConditionalBranch) branch).branches);
            }
        }
        return this;
    }

    public String toString() {
        return "Conditional Branch";
    }
    public String toString(int level) {
        StringBuilder sb = new StringBuilder();
        for (Conditional branch : branches) {
            sb.append(branch.toString(level));
        }
        return sb.toString();
    }
}
