package com.N2_Parser.Lookahead;

public class LookaheadTestParser extends LookaheadParser {
    public LookaheadTestParser() {
        this(true, false);
    }
    public LookaheadTestParser(boolean silent) {
        this(silent, false);
    }
    public LookaheadTestParser(boolean silent, boolean slow) {
        addAliases(
                "EndOfFile", "$",
                "Phrase", "S'",
                "Statement", "S",
                "Expression", "E",
                "Variable", "V",
                "Pointer", "*",
                "Identifier", "x",
                "Equals", "="
        );

        addRule("S'", "S", "$");
        addRule("S", "V", "=", "E");
        addRule("S", "E");
        addRule("E", "V");
        addRule("V", "x");
        addRule("V", "*", "E");

        createLRA(silent, slow);
        getActionGrid().printGrid();
    }
}
