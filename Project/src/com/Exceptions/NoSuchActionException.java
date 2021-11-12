package com.Exceptions;

import com.Automata.LRState;
import com.N1_Lexer.Lexer;
import com.N2_Parser.Grammar.Item;

public class NoSuchActionException extends RuntimeException {
    public final LRState<? extends Item> state;
    public final Lexer.type input;
    public NoSuchActionException(LRState<? extends Item> state, Lexer.type input) {
        this.state = state;
        this.input = input;
    }

    public String getMessage() {
        return "At state " + state.getID() + " for input " + input;
    }
}
