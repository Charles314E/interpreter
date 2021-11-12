package com.GUI.TokenBlocks.Operators.EqSigns;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Equations.NotEquals;

public class NotEqualsBlock extends EqSignBlock<NotEquals> {
    public NotEqualsBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new NotEquals());
        setText('≠');
    }
    public NotEqualsBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Equation", "NotEqualsStatement");
        addRule("NotEqualsStatement", "Expression", "!=", "Expression");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
