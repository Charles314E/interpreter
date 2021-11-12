package com.GUI.TokenBlocks.Operators.OpSigns;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.TokenBlocks.Operators.CircleBlock;
import com.N3_SemanticActions.Tokens.Token;

public abstract class OpSignBlock<T extends Token> extends CircleBlock<T> {
    public OpSignBlock(GUI gui, DefinedBlock<?> parent, T token) {
        super(gui, parent, token);
    }

    public void setGrammars() {
        addRule("Operator", "Operator", "$");
    }
}