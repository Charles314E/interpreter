package com.GUI.TokenBlocks.Virtual;

import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Token;

public class EquationBlock extends VirtualBlock<Token> {
    public EquationBlock() {
        super(null, null, null);
    }

    @Override
    public void setGrammars() {
        addRule("Expression", "Expression", "$");
        addRule("Expression", "Equation");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }

    @Override
    public final void setCaptureSlots() {
        createSlotCapture(0, slotShape.SQUARE, side.LEFT);
        createSlotCapture(0, slotShape.CIRCLE_SLOT, side.LEFT);
        createSlotCapture(0, slotShape.SQUARE, side.RIGHT);
        createSlotCapture(0, slotShape.CIRCLE_SLOT, side.RIGHT);
    }
}
