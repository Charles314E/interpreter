package com.GUI.TokenBlocks.Operators.OpSigns;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Operators.ExponentOperator;
import com.N3_SemanticActions.Tokens.Operators.PlusOperator;

public class ExponentBlock extends OpSignBlock<ExponentOperator> {
    public ExponentBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new ExponentOperator());
        setText('^');
    }
    public ExponentBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Operator", "ExponentOperator");
        addRule("ExponentOperator", "Power", "(", "Expression", ",", "Expression", ")");
    }

    public void resetSlots() {
        undefineSlots();
        addSlot(slotShape.CIRCLE, side.LEFT, 2, 2);
        addSlot(slotShape.CIRCLE, side.RIGHT, 2, 4);
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
