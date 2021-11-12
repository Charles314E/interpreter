package com.GUI.TokenBlocks.Values;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Values.NullLiteral;

public class NullBlock extends ValueBlock<NullLiteral> {
    public NullBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new NullLiteral());
        input = null;
    }
    public NullBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Expression", "Null");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
