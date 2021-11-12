package com.N2_Parser.Actions;

import com.Automata.LRState;
import com.N2_Parser.Grammar.Item;

public class GotoAction<T extends Item> extends ShiftAction<T> {
    public GotoAction(ParserActionGrid<T> grid, LRState<T> state, String token) {
        super(grid, state, token);
    }
    @Override
    public String toString() {
        return "g" + state.getID();
    }

    public boolean equals(GotoAction<T> o) {
        return state.equals(o.getState());
    }
}
