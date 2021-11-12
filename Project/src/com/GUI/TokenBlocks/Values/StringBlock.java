package com.GUI.TokenBlocks.Values;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Values.StringLiteral;

public class StringBlock extends ValueBlock<StringLiteral> {
    public StringBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new StringLiteral());
    }
    public StringBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Expression", "String");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
