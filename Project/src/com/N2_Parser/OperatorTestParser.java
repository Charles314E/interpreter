package com.N2_Parser;

import com.N2_Parser.Actions.LookaheadToReduceAction;
import com.N2_Parser.Actions.ReduceAction;

public class OperatorTestParser extends SimpleParser {
    public OperatorTestParser() {
        this(true, false);
    }
    public OperatorTestParser(boolean silent) {
        this(silent, false);
    }
    public OperatorTestParser(boolean silent, boolean slow) {
        addAliases(
                "EndOfFile", "$",
                "Whitespace", "''",
                "Equals", "="
        );

        addRule("Phrase", "Phrase", "$");
        addRule("Phrase", "Statement", ";");
        addRule("Statement", "Expression", "''", "=", "''", "Expression");
        addRule("Expression", "Integer");
        addRule("Expression", "Identifier");
        addRule("Expression", "Operator");
        addRule("Operator", "PlusOperator");
        addRule("PlusOperator", "Expression", "''", "Plus", "''", "Expression");

        createLRA(silent);
        //((LookaheadToReduceAction) getActionGrid().getAction(1, "Integer", "r3if;")).setLookahead(null);
        getActionGrid().printGrid();
    }
}
