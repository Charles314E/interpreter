package com.GUI.TokenBlocks.Virtual;

import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Token;

public class ExpressionBlock extends VirtualBlock<Token> {
    public ExpressionBlock() {
        super(null, null, null);
    }

    @Override
    public void setGrammars() {
        addRule("Expression", "Expression", "$");
        addRule("Expression", "(", "Expression", ")");
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
