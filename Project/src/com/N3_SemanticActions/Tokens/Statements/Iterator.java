package com.N3_SemanticActions.Tokens.Statements;

import com.N3_SemanticActions.Tokens.Expression;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Values.Array;
import com.N3_SemanticActions.Visitor;

import java.util.ArrayList;

public class Iterator extends Token {
    public Array array;
    public AssignmentStatement[] assignments;
    public Expression first;
    public Expression last;
    public ForLoop loop;

    public Iterator() {}
    public Iterator(Array array) {
        this.array = array;
    }

    public Iterator construct(ArrayList<Token> arguments) {
        if (arguments.size() == 1) {
            array = (Array) arguments.get(0);
        }
        else if (arguments.size() == 2) {
            first = (Expression) arguments.get(0);
            last = (Expression) arguments.get(1);
        }
        return this;
    }

    public void constructArray(ArrayList<Expression> elements) {
        System.out.println(elements.size());
        assignments = new AssignmentStatement[elements.size()];
        AssignmentStatement assignment;
        ArrayList<Token> binding = new ArrayList<>();
        binding.add(loop.getIdentifier());
        binding.add(null);
        for (int i = 0; i < elements.size(); i += 1) {
            assignment = new AssignmentStatement();
            binding.remove(1);
            binding.add(elements.get(i));
            assignment.construct(binding);
            assignments[i] = assignment;
        }
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }

    public String toString() {
        return "Iterator";
    }
    public String toString(int level) {
        if (array != null) {
            return array.toString(level);
        }
        else {
            return first.toString(0) + " to " + last.toString(0);
        }
    }
}
