package com.N2_Parser;

import com.Classes.Pair;
import com.Exceptions.BrokenParse;
import com.Methods.stringMethods;
import com.N1_Lexer.ContextToken;
import com.N2_Parser.Actions.LookaheadToReduceAction;
import com.N2_Parser.Actions.NoLookaheadToReduceAction;
import com.N2_Parser.Actions.ParserAction;
import com.N2_Parser.Actions.ReduceAction;
import com.N2_Parser.Grammar.Grammar;
import com.N2_Parser.Grammar.GrammarSimilarity;
import com.N2_Parser.Grammar.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class ErrorCorrector<T extends Item> {
    private final Parser<T> parser;
    private Stack<ArrayList<ParserAction<T>>> errorStates;

    public ErrorCorrector(Parser<T> parser) {
        this.parser = parser;
    }

    public ContextToken correctBrokenStack(Stack<ContextToken> stack) {
        int i = 0;
        Stack<ContextToken> errorStack = new Stack<>();
        for (ContextToken t : stack) {
            if (i >= (stack.size() - 3)) {
                errorStack.push(t);
            }
            i += 1;
        }
        ArrayList<GrammarSimilarity<T>> rules = findClosestRules(errorStack, 6);
        //System.out.println(rules);
        try {
            throw new BrokenParse();
        }
        catch (BrokenParse e) {
            e.printStackTrace();
        }
        //System.out.println("[DBG]: Managed to get past the stack trace.");
        ArrayList<ParserAction<T>> eState;
        String stackToken, definitionToken;
        ReduceAction<T> ra;
        ContextToken childContextToken, stackContextToken, parentContextToken, nextContextToken;
        for (GrammarSimilarity<T> rule : rules) {
            for (int j = 0; j < errorStack.size(); j += 1) {
                stackContextToken = errorStack.get(j);
                parentContextToken = new ContextToken(stackContextToken.getType(), stackContextToken);
                try {
                    nextContextToken = errorStack.get(j + 1);
                }
                catch (IndexOutOfBoundsException e) {
                    nextContextToken = null;
                }
                stackToken = stackContextToken.getType().getClassName();
                definitionToken = rule.getRule().getDefinition()[j];
                //System.out.println(stringMethods.tuple(stackContextToken, parentContextToken, stackToken, definitionToken));
                if (!stackToken.equals(definitionToken)) {
                    //System.out.println("[DBG]: " + stackToken + " =/= " + definitionToken);
                    while (!errorStates.empty()) {
                        eState = errorStates.pop();
                        //System.out.println("[DBG]: " + eState);
                        for (ParserAction<T> a : eState) {
                            if (a instanceof ReduceAction) {
                                ra = (ReduceAction<T>) a;
                                //System.out.println("[DBG]: " + ra.getRule());
                                if (ra.getRule().getName().equals(definitionToken)) {
                                    //System.out.println("[DBG]: " + ra.getRule().getName() + " == " + definitionToken + " (" + ra.getRule() + ")");
                                    childContextToken = findCorrectChild(parentContextToken, nextContextToken, ra);
                                    if (childContextToken != null) {
                                        parser.getLRA().getDerivations().add(childContextToken);
                                        //System.out.println("[DBG]: Found " + childContextToken);
                                        stack.add(stack.indexOf(stackContextToken), childContextToken);
                                        stack.remove(stackContextToken);
                                        return childContextToken;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    protected ContextToken findCorrectChild(ContextToken token, ContextToken nextToken, ReduceAction<T> action) {
        boolean bad = false;
        ContextToken[] children = token.getChildren().toArray(new ContextToken[0]);
        Grammar<T> g1 = action.getRule(),
                g2 = new Grammar<>(action.getRule().getParser(), action.getRule().getName(), children);
        System.out.println("[DBG]: (" + g1 + ") & (" + g2 + ")");
        if (g1.equals(g2)) {
            Stack<ContextToken> stack = new Stack<>();
            stack.addAll(token.getChildren());
            ArrayList<ContextToken> input = new ArrayList<>();
            input.add(nextToken);
            if (action instanceof LookaheadToReduceAction) {
                if (((LookaheadToReduceAction<T>) action).action(null, input, stack)) {
                    System.out.println("1. " + action);
                    return stack.pop();
                }
            }
            else if (action instanceof NoLookaheadToReduceAction) {
                if (((NoLookaheadToReduceAction<T>) action).action(null, input, stack)) {
                    System.out.println("2. " + action);
                    return stack.pop();
                }
            }
            else if (action.action(null, stack)) {
                System.out.println("3. " + action);
                return stack.pop();
            }
            bad = true;
            System.out.println("BAD.");
        }
        if (bad) {
            for (ContextToken child : token.getChildren()) {
                return findCorrectChild(child, null, action);
            }
        }
        return null;
    }

    private ArrayList<GrammarSimilarity<T>> findClosestRules(Stack<ContextToken> tokens, int n) {
        Grammar<T> g = new Grammar<>(parser, tokens.toArray(new ContextToken[0]));
        ArrayList<GrammarSimilarity<T>> chosen = new ArrayList<>();
        ArrayList<Grammar<T>> rules = new ArrayList<>(parser.getRules());
        Collections.sort(rules);
        GrammarSimilarity<T> similarity;
        for (Grammar<T> rule : rules) {
            similarity = new GrammarSimilarity<>(rule, g.getSimilarity(rule));
            if (chosen.size() > 0) {
                if (chosen.get(0).compareTo(similarity) < 0) {
                    chosen.add(similarity);
                    if (chosen.size() == n) {
                        chosen.remove(0);
                    }
                }
            }
            else {
                chosen.add(similarity);
            }
            Collections.sort(chosen);
        }
        Collections.reverse(chosen);
        return chosen;
    }

    public void addState(ArrayList<ParserAction<T>> actions) {
        errorStates.push(actions);
    }

    public void clearStates() {
        errorStates = new Stack<>();
    }
}
