package com.GUI.TokenBlocks.Operators.OpSigns;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Operators.MinusOperator;
import com.N3_SemanticActions.Tokens.Operators.MultiplyOperator;

public class MinusBlock extends OpSignBlock<MinusOperator> {
    public MinusBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new MinusOperator());
        setText('-');
    }
    public MinusBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Operator", "MinusOperator");
        addRule("MinusOperator", "Expression", "-", "Expression");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
