package com.GUI;

import com.Classes.ObjectFactory;
import com.Classes.Pair;
import com.GUI.TokenBlocks.Values.ValueBlock;
import com.Methods.stringMethods;
import com.N1_Lexer.ContextToken;
import com.N3_SemanticActions.Tokens.Token;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;

public abstract class EditableBlock<T extends Token> extends DefinedBlock<T> {
    protected boolean intelligible = true;
    private boolean edited = false;

    public EditableBlock(GUI gui, DefinedBlock<?> parent, T token) {
        super(gui, parent, token);
        resizeForInput();
        input = "";
    }

    public void addToInput(KeyEvent e) {
        if (intelligible) {
            edited = true;
            boolean analysing = false;
            KeyCode code = e.getCode();
            switch (code) {
                case ENTER -> analysing = true;
                case BACK_SPACE -> input = input.substring(0, Math.max(0, input.length() - 1));
                default -> {
                    String c = e.getText();
                    input += c;
                }
            }
            resetTooltip();
            resetText();
            if (analysing) {
                analyseText();
            }
        }
    }

    protected abstract Pair<ArrayList<ContextToken>, ObjectFactory<?>> analyse(GUI frame);
    protected Pair<ArrayList<ContextToken>, ObjectFactory<?>> analyse() {
        return analyse(frame);
    }
    public void analyseText(GUI frame) {
        Pair<ArrayList<ContextToken>, ObjectFactory<?>> result = analyse(frame);
        System.out.println("ANALYSIS: " + result);
        boolean failed = true;
        EditableBlock<?> newBlock = null;
        if (result != null) {
            newBlock = (EditableBlock<?>) frame.createBlock((ObjectFactory<? extends ValueBlock<?>>) result.getValue(), getX(), getY(), false, false, false);
            newBlock.intelligible = false;
            newBlock.setInputText(input);
            failed = createBlockFromParse(frame, true, newBlock, result.getKey()) == null;
            System.out.println("ANALYSIS_RESULT" + stringMethods.tuple(newBlock, failed));
            System.out.println();
        }
        if (failed) {
            try {
                highlightText(frame.incorrectColour);
            }
            catch (NullPointerException e) {

            }
        }
        else {
            try {
                newBlock.intelligible = true;
                newBlock.highlightText(frame.correctColour);
                System.out.println("[DBG]: Made " + newBlock + " intelligible.");
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
    public void analyseText() {
        analyseText(frame);
    }

    public void reset(GUI frame, int x, int y, boolean show, boolean analyse) {
        super.reset(frame, x, y, show);
        if (analyse) {
            analyseText(frame);
        }
    }

    private void setInputText(String text) {
        input = text;
        resetTooltip();
        resetText();
        resetFrame();
    }

    public void resetText() {
        text = createText(Color.BLACK);
        resizeForInput();
    }
    public void resizeForInput() {
        if (input == null) {
            input = "";
        }
        setSize(new Dimension((text != null ? (int) text.getBoundsInLocal().getWidth() : 0) + baseWidth, getHeight()));
        resetFrame();
    }

    public void setButtonShape() throws NullPointerException {
        super.setButtonShape();
    }

    public void setSize(Dimension d) {
        super.setSize(d);
        for (BlockSlot g : getBorderGlows()) {
            g.positionGlow();
            g.setLocation(getX(), getY());
        }
        setButtonShape();
        adjustSlots();
        for (BlockSlot g : getBorderGlows()) {
            try {
                g.pullBlockToSlot(glows.get(g));
            }
            catch (NullPointerException e) {

            }
        }
    }

    public boolean wasEdited() {
        return edited;
    }
}
