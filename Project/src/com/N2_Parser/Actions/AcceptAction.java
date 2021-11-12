package com.N2_Parser.Actions;

import com.Automata.LRA;
import com.N2_Parser.Grammar.Item;

public class AcceptAction<T extends Item> extends ReduceAction<T> {
    private LRA<T> automaton;
    public AcceptAction(ParserActionGrid<T> grid, LRA<T> automaton) {
        this.grid = grid;
        this.automaton = automaton;
        rule = automaton.getParser().getRule(0);
    }
    @Override
    public String toString() {
        return "a";
    }

    public LRA<T> getAutomaton() {
        return automaton;
    }

    public boolean equals(AcceptAction<T> o) {
        return automaton == o.getAutomaton();
    }

    public boolean action() {
        return super.action();
    }
}
