package com.N2_Parser.Actions;

import com.N1_Lexer.ContextToken;
import com.N2_Parser.Grammar.Grammar;
import com.N2_Parser.Grammar.Item;
import com.N3_SemanticActions.Tokens.Token;
import com.Methods.stringMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LookaheadToReduceAction<T extends Item> extends ReduceAction<T> {
    List<String> lookahead;
    public LookaheadToReduceAction(ParserActionGrid<T> grid, Grammar rule, String[] lookahead) {
        super(grid, rule);
        this.lookahead = new ArrayList<>();
        for (int i = 0; i < lookahead.length; i += 1) {
            this.lookahead.add(grid.getParser().getNameForAlias(lookahead[i]));
        }
    }

    public Grammar getRule() {
        return rule;
    }

    @Override
    public String toString() {
        String[] lk = new String[lookahead.size()];
        for (int i = 0; i < lookahead.size(); i += 1) {
            lk[i] = grid.getParser().getAliasForName(lookahead.get(i));
        }
        return "r" + rule.getParser().getRules().indexOf(rule) + (lookahead != null ? "if" + stringMethods.encapsulatedTuple("", "", "or", lk) : "");
    }

    public boolean equals(LookaheadToReduceAction<T> o) {
        return rule.equals(o.getRule());
    }

    public boolean action() {
        return action(null);
    }
    public boolean action(Token token) {
        return action(token, grid.getParser().getInput(), grid.getParser().getStack());
    }
    public boolean action(Token token, ArrayList<ContextToken> input, Stack<ContextToken> stack) {
        try {
            if (lookahead.contains(input.get(0).getType().getClassName())) {
                return super.action(token, stack);
            }
            return false;
        }
        catch (NullPointerException e) {
            return super.action(token, stack);
        }
    }
}
