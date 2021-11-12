package com.GUI.TokenBlocks.Values;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Values.IntegerLiteral;

public class IntegerBlock extends ValueBlock<IntegerLiteral> {
    public IntegerBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new IntegerLiteral());
    }
    public IntegerBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Expression", "Integer");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
