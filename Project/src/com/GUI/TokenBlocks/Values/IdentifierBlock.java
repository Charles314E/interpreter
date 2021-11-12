package com.GUI.TokenBlocks.Values;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Values.Identifier;

public class IdentifierBlock extends ValueBlock<Identifier> {
    public IdentifierBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new Identifier());
    }
    public IdentifierBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Expression", "Variable");
        addRule("Variable", "Name");
        addRule("Name", "Identifier");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
