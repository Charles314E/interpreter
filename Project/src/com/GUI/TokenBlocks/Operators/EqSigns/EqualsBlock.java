package com.GUI.TokenBlocks.Operators.EqSigns;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.GUI.TokenBlocks.Operators.OpSigns.OpSignBlock;
import com.N3_SemanticActions.Tokens.Equations.Equals;

public class EqualsBlock extends EqSignBlock<Equals> {
    public EqualsBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new Equals());
        setText('=');
    }
    public EqualsBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Equation", "EqualsStatement");
        addRule("EqualsStatement", "Expression", "==", "Expression");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
