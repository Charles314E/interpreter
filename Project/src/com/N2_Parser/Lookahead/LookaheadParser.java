package com.N2_Parser.Lookahead;

import com.Automata.LRA;
import com.N2_Parser.Actions.LookaheadToReduceAction;
import com.N2_Parser.Actions.ParserActionGrid;
import com.N2_Parser.Actions.ReduceAction;
import com.N2_Parser.Grammar.Grammar;
import com.N2_Parser.Grammar.ItemList;
import com.Methods.stringMethods;
import com.N2_Parser.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class LookaheadParser extends Parser<LookaheadGrammar> {
    public void createLRA(boolean silent) {
        createLRA(silent, false);
    }
    public void createLRA(boolean silent, boolean slow) {
        grid = new ParserActionGrid<>(this, new HashSet<>());
        automaton = new LRA<>(LookaheadGrammar.class, this, silent) {
            @Override
            public ItemList<LookaheadGrammar> getInitialisedGrammars(ArrayList<Grammar<LookaheadGrammar>> i) {
                ItemList<LookaheadGrammar> j = new ItemList<>(LookaheadGrammar.class);
                stringMethods.silentPrintLine("CREATING LOOKAHEADS...", silent);
                HashSet<String> l;
                for (Grammar<LookaheadGrammar> g : i) {
                    stringMethods.silentPrintLine(" " + g, silent);
                    l = ((LookaheadParser) getParser()).getLookaheadForGrammar(g);
                    if (l.size() > 0) {
                        for (String token : l) {
                            stringMethods.silentPrintLine("  " + token, silent);
                            j.add(new LookaheadGrammar(g, 0, token));
                        }
                    }
                    else {
                        j.add(new LookaheadGrammar(g, 0, null));
                    }
                }
                stringMethods.silentPrintLine("FINISHED.", silent);
                for (LookaheadGrammar g : j) {
                    stringMethods.silentPrintLine(g, silent);
                }
                return j;
            }

            public ReduceAction<LookaheadGrammar> createReduceAction(ParserActionGrid<LookaheadGrammar> grid, LookaheadGrammar reduceRule) {
                return new LookaheadToReduceAction<>(grid, reduceRule.getGrammar(), new String[] { reduceRule.getLookahead() });
            }
        };
        automaton.populate(slow);
    }

    public HashSet<String> getLookaheadForGrammar(Grammar<LookaheadGrammar> g) {
        return getLookaheadForGrammar(true, g);
    }
    public HashSet<String> getLookaheadForGrammar(boolean silent, Grammar<LookaheadGrammar> g) {
        return getLookaheadForGrammar(silent, g, g.getName(), new HashSet<>());
    }
    public HashSet<String> getLookaheadForGrammar(boolean silent, Grammar<LookaheadGrammar> g, String name, HashSet<Grammar<LookaheadGrammar>> done) {
        HashSet<String> lookahead = new HashSet<>();
        if (!done.contains(g)) {
            stringMethods.silentPrintLine(silent);
            stringMethods.silentPrintLine(stringMethods.tuple(g, name), silent);
            List<String> gl = Arrays.asList(g.getDefinition());
            try {
                if (gl.contains(name)) {
                    lookahead.add(gl.get(gl.indexOf(name) + 1));
                }
            } catch (IndexOutOfBoundsException e) {

            }
            stringMethods.silentPrintLine(lookahead, silent);
            name = g.getName();
            done.add(g);
            for (Grammar<LookaheadGrammar> h : getRulesFromDefinition(name)) {
                lookahead.addAll(getLookaheadForGrammar(silent, h, name, done));
            }
        }
        return lookahead;
    }
}
