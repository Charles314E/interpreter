package com.GUI.TokenBlocks.Operators.EqSigns;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Equations.LessThan;

public class LessThanBlock extends EqSignBlock<LessThan> {
    public LessThanBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new LessThan());
        setText('<');
    }
    public LessThanBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Equation", "LessThanStatement");
        addRule("LessThanStatement", "Expression", "<", "Expression");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
