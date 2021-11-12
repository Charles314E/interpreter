package com.GUI.TokenBlocks.Operators;

import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.Methods.stringMethods;
import com.N3_SemanticActions.Tokens.Token;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public abstract class CircleBlock<T extends Token> extends DefinedBlock<T> {
    public CircleBlock(GUI gui, DefinedBlock<?> parent, T token) {
        super(gui, parent, token);
        setSize(32, 32);
        resetSlots();
        setButtonShape();
        resetFrame();
    }

    public void setGrammars() {
        addRule("Expression", "Expression", "$");
    }

    public void setText(char op) {
        text = createText(Character.toString(op), Color.BLACK);
        baseWidth = 16 - (int) text.getBoundsInLocal().getCenterX() - 1;
        baseHeight = 16 - (int) (text.getBoundsInLocal().getCenterY() - (text.getBoundsInLocal().getHeight() / 2));
        System.out.println("TEXT: " + stringMethods.tuple(baseWidth, baseHeight));
    }

    public void positionText(Text text) {
        try {
            text.setX(getX() + baseWidth);
            text.setY(getY() + baseHeight);
        }
        catch (NullPointerException e) {

        }
    }

    public void resetSlots() {
        undefineSlots();
        addSlot(slotShape.CIRCLE, side.LEFT, 2, 0);
        addSlot(slotShape.CIRCLE, side.RIGHT, 2, 2);
    }
}