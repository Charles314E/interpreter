package com.GUI.TokenBlocks.Values;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Values.FloatLiteral;

public class FloatBlock extends ValueBlock<FloatLiteral> {
    public FloatBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new FloatLiteral());
    }
    public FloatBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Expression", "Real");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
