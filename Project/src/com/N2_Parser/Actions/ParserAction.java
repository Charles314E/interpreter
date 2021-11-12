package com.N2_Parser.Actions;

import com.Automata.LRState;
import com.N2_Parser.Grammar.Item;

public abstract class ParserAction<T extends Item> {
    protected ParserActionGrid<T> grid;
    protected LRState<T> state;
    public LRState<T> getState() {
        return state;
    }
    public boolean equals() {
        return false;
    }
    public abstract boolean action();
}
