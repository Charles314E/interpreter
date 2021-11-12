package com.GUI.TokenBlocks.Operators.OpSigns;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Operators.DivisionOperator;

public class DivideBlock extends OpSignBlock<DivisionOperator> {
    public DivideBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new DivisionOperator());
        setText('÷');
    }
    public DivideBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Operator", "DivideOperator");
        addRule("DivideOperator", "Expression", "/", "Expression");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
