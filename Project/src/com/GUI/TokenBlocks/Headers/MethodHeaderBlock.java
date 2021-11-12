package com.GUI.TokenBlocks.Headers;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Statements.IfStatement;

public class MethodHeaderBlock extends HeaderBlock<IfStatement> {
    public MethodHeaderBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new IfStatement(), "DEF");
    }
    public MethodHeaderBlock() {
        this(null, null);
    }

    @Override
    public void setGrammars() {
        super.setGrammars();
        addRule("Statement", "MethodDefinition");
        addRule("MethodDefinition", "MethodHeader", "EnclosedPhrase");
        addRule("MethodHeader", "Def", "Identifier", "(", "Arguments", ")");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
