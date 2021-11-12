package com.N2_Parser.Grammar;

public abstract class Item<T extends Item> {
    protected Grammar<T> rule;
    protected int pointer;

    public Grammar<T> getGrammar() {
        return rule;
    }
    public int getPointerPosition() {
        return pointer;
    }

    public boolean hasNextToken() {
        return (pointer < rule.getDefinition().length - 1);
    }
    public String getNextToken() {
        try {
            return rule.getDefinition()[pointer];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public abstract Item shift();
}
