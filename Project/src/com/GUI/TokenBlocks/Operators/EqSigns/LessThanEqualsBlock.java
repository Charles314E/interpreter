package com.GUI.TokenBlocks.Operators.EqSigns;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Equations.LessThanEquals;

public class LessThanEqualsBlock extends EqSignBlock<LessThanEquals> {
    public LessThanEqualsBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new LessThanEquals());
        setText('≤');
    }
    public LessThanEqualsBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Equation", "LessThanOrEqualsStatement");
        addRule("LessThanOrEqualsStatement", "Expression", "<=", "Expression");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
