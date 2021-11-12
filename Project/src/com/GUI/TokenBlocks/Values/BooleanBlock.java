package com.GUI.TokenBlocks.Values;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Values.BooleanLiteral;

public class BooleanBlock extends ValueBlock<BooleanLiteral> {
    public BooleanBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new BooleanLiteral());
    }
    public BooleanBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Expression", "Boolean");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
