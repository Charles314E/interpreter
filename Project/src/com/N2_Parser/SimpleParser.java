package com.N2_Parser;

import com.Automata.LRA;
import com.N2_Parser.Actions.ParserActionGrid;
import com.N2_Parser.Actions.ReduceAction;
import com.N2_Parser.Grammar.Grammar;
import com.N2_Parser.Grammar.ItemList;
import com.N2_Parser.Grammar.PointedGrammar;

import java.util.ArrayList;
import java.util.HashSet;

public class SimpleParser extends Parser<PointedGrammar> {
    public void createLRA(boolean silent) {
        grid = new ParserActionGrid<>(this, new HashSet<>());
        automaton = new LRA<>(PointedGrammar.class, this, silent) {
            @Override
            public ItemList<PointedGrammar> getInitialisedGrammars(ArrayList<Grammar<PointedGrammar>> i) {
                ItemList<PointedGrammar> j = new ItemList<>(PointedGrammar.class);
                for (Grammar<PointedGrammar> g : i) {
                    j.add(new PointedGrammar(g, 0));
                }
                return j;
            }
            public ReduceAction<PointedGrammar> createReduceAction(ParserActionGrid<PointedGrammar> grid, PointedGrammar reduceRule) {
                return new ReduceAction<PointedGrammar>(grid, reduceRule.getGrammar());
            }
        };
    }

    public void populateLRA() {
        automaton.populate(false);
    }
}
