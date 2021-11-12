package com.N2_Parser.Actions;

import com.N1_Lexer.ContextToken;
import com.N1_Lexer.Lexer;
import com.N2_Parser.Grammar.Grammar;
import com.N2_Parser.Grammar.Item;
import com.N3_SemanticActions.Tokens.ConcreteToken;
import com.N3_SemanticActions.Tokens.Token;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;

public class ReduceAction<T extends Item> extends ParserAction<T> {
    protected Grammar<T> rule;
    public ReduceAction() {

    }
    public ReduceAction(ParserActionGrid<T> grid, Grammar<T> rule) {
        this.grid = grid;
        this.rule = rule;
    }

    public Grammar<T> getRule() {
        return rule;
    }

    @Override
    public String toString() {
        return "r" + rule.getParser().getRules().indexOf(rule);
    }

    public boolean equals(ReduceAction<T> o) {
        return rule.equals(o.getRule());
    }

    public boolean action() {
        return action(null);
    }
    public boolean action(Token token) {
        return action(token, grid.getParser().getStack());
    }
    public boolean action(Token token, Stack<ContextToken> stack) {
        ContextToken lastToken;
        try {
            lastToken = stack.peek();
        }
        catch (EmptyStackException e) {
            return false;
        }
        if (grid != null) {
            grid.getParser().getLRA().pushToStack(rule.getDefinition()[rule.getDefinition().length - 1]);
        }
        String[] definition = rule.getDefinition();
        ContextToken peek, newContextToken = new ContextToken(Lexer.type.getIdentifier(rule.getName()));
        Stack<ContextToken> poppedTokens = new Stack<>();
        for (int i = definition.length; i > 0; i -= 1) {
            try {
                peek = poppedTokens.push(stack.peek());
                if (peek.getType() == Lexer.type.getIdentifier(definition[i - 1])) {
                    stack.pop();
                    if (token != null) {
                        token.addChild(new ConcreteToken(peek));
                    }
                    newContextToken.addChild(peek);
                }
                else {
                    poppedTokens.pop();
                    while (!poppedTokens.isEmpty()) {
                        stack.push(poppedTokens.pop());
                    }
                    if (stack.peek() != lastToken) {
                        grid.getParser().getLRA().pushToInput(stack.peek());
                    }
                    return false;
                }
            }
            catch (EmptyStackException e) {
                poppedTokens.pop();
                while (!poppedTokens.isEmpty()) {
                    stack.push(poppedTokens.pop());
                }
                try {
                    if (stack.peek() != lastToken) {
                        grid.getParser().getLRA().pushToInput(stack.peek());
                    }
                }
                catch (EmptyStackException e1) {
                    stack.push(lastToken);
                }
                System.out.println(e.getMessage());
                return false;
            }
        }
        Collections.reverse(newContextToken.getChildren());
        stack.push(newContextToken);
        if (token != null) {
            token.setName(stack.peek());
        }
        return true;
    }
}
