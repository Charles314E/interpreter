package com.GUI.TokenBlocks.Headers;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Statements.IfStatement;

public class WhileHeaderBlock extends HeaderBlock<IfStatement> {
    public WhileHeaderBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new IfStatement(), "WHILE");
    }
    public WhileHeaderBlock() {
        this(null, null);
    }

    @Override
    public void setGrammars() {
        super.setGrammars();
        addRule("Statement", "LoopStatement");
        addRule("WhileLoop", "WhileHeader", "EnclosedPhrase");
        addRule("WhileHeader", "While", "(", "Equation", ")");
        addRule("LoopStatement", "WhileLoop");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
