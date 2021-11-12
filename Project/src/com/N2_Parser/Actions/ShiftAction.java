package com.N2_Parser.Actions;

import com.Automata.LRA;
import com.Automata.LRState;
import com.N2_Parser.Grammar.Item;
import com.N2_Parser.Parser;

public class ShiftAction<T extends Item> extends ParserAction<T> {
    protected String token;
    public ShiftAction() {

    }
    public ShiftAction(ParserActionGrid<T> grid, LRState<T> state, String token) {
        this.grid = grid;
        this.state = state;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "s" + state.getID();
    }

    public boolean equals(ShiftAction<T> o) {
        return state.equals(o.getState());
    }

    public boolean action() {
        Parser<T> parser = grid.getParser();
        LRA<T> automaton = parser.getLRA();
        if (automaton.pushToStack(token)) {
            automaton.transition(state);
            return true;
        }
        return false;
    }
}
