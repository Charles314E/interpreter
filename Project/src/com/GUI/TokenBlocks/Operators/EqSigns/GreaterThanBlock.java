package com.GUI.TokenBlocks.Operators.EqSigns;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Equations.MoreThan;

public class GreaterThanBlock extends EqSignBlock<MoreThan> {
    public GreaterThanBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new MoreThan());
        setText('>');
    }
    public GreaterThanBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Equation", "GreaterThanStatement");
        addRule("GreaterThanStatement", "Expression", ">", "Expression");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
