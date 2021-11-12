package com.GUI.TokenBlocks.Operators.OpSigns;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Operators.PlusOperator;

public class PlusBlock extends OpSignBlock<PlusOperator> {
    public PlusBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new PlusOperator());
        setText('+');
    }
    public PlusBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Operator", "PlusOperator");
        addRule("PlusOperator", "Expression", "+", "Expression");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
