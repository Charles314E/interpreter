package com.GUI.TokenBlocks.Operators.EqSigns;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Equations.MoreThanEquals;

public class GreaterThanEqualsBlock extends EqSignBlock<MoreThanEquals> {
    public GreaterThanEqualsBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new MoreThanEquals());
        setText('≥');
    }
    public GreaterThanEqualsBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Equation", "GreaterThanOrEqualsStatement");
        addRule("GreaterThanOrEqualsStatement", "Expression", ">=", "Expression");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
