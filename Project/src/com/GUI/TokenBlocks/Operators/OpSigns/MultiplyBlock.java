package com.GUI.TokenBlocks.Operators.OpSigns;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Operators.MultiplyOperator;

public class MultiplyBlock extends OpSignBlock<MultiplyOperator> {
    public MultiplyBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new MultiplyOperator());
        setText('×');
    }
    public MultiplyBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Operator", "MultiplyOperator");
        addRule("MultiplyOperator", "Expression", "*", "Expression");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
