package com.GUI.TokenBlocks.Headers;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Statements.IfStatement;

public class IfHeaderBlock extends HeaderBlock<IfStatement> {
    public IfHeaderBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new IfStatement(), "IF");
    }
    public IfHeaderBlock() {
        this(null, null);
    }

    @Override
    public void setGrammars() {
        super.setGrammars();
        addRule("Statement", "IfStatement");
        addRule("IfStatement", "IfHeader", "EnclosedPhrase");
        addRule("IfHeader", "If", "(", "Equation", ")");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
