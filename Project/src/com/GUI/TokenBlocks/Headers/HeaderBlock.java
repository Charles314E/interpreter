package com.GUI.TokenBlocks.Headers;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.Methods.stringMethods;
import com.N3_SemanticActions.Tokens.Token;
import javafx.scene.paint.Color;

public abstract class HeaderBlock<T extends Token> extends DefinedBlock<T> {
    public HeaderBlock(GUI gui, DefinedBlock<?> parent, T token, String text) {
        super(gui, parent, token);
        setText(text);
        setSize((int) this.text.getBoundsInLocal().getWidth() + baseWidth, 32);
        resetSlots();
        setButtonShape();
        resetFrame();
    }
    public HeaderBlock() {
        this(null, null, null, "");
    }

    public void setText(String word) {
        text = createText(word, Color.BLACK);
        baseWidth = 32;
        baseHeight = 16 - (int) (text.getBoundsInLocal().getCenterY() - (text.getBoundsInLocal().getHeight() / 2));
        System.out.println("TEXT: " + stringMethods.tuple(baseWidth, baseHeight));
    }

    public void setGrammars() {
        addRule("Statement", "Statement", "$");
        addRule("EnclosedPhrase", "{", "Phrase", "}");
    }

    public void resetSlots() {
        undefineSlots();
        addSlot(slotShape.CIRCLE, side.RIGHT, 4, 2);
        addSlot(slotShape.DOT, side.LEFT, 1, 1);
        addSlot(slotShape.DOT_SLOT, side.LEFT);
    }
}
