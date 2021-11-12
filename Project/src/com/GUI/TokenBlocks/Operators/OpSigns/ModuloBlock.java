package com.GUI.TokenBlocks.Operators.OpSigns;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Operators.ModulusOperator;
import com.N3_SemanticActions.Tokens.Operators.PlusOperator;

public class ModuloBlock extends OpSignBlock<ModulusOperator> {
    public ModuloBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new ModulusOperator());
        setText('%');
    }
    public ModuloBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Operator", "ModulusOperator");
        addRule("ModulusOperator", "Expression", "%", "Expression");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
