package com.GUI.TokenBlocks.Operators.OpSigns;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N3_SemanticActions.Tokens.Values.Item;

public class IndexBlock extends OpSignBlock<Item> {
    public IndexBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new Item());
        setText('◌');
    }
    public IndexBlock() {
        this(null, null);
    }

    public void setGrammars() {
        super.setGrammars();
        addRule("Expression", "Variable");
        addRule("Variable", "Item");
        addRule("Item", "Name", "[", "Expression", "]");
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }
}
