package com.GUI.TokenBlocks.Values;

import com.Exceptions.ArtException;
import com.Exceptions.TypeError;
import com.GUI.DefinedBlock;
import com.GUI.EditableBlock;
import com.GUI.GUI;
import com.N1_Lexer.ContextToken;
import com.N1_Lexer.Lexer;
import com.N3_SemanticActions.Tokens.Token;
import com.Classes.ObjectFactory;
import com.Classes.Pair;

import java.lang.constant.Constable;
import java.util.ArrayList;

public abstract class ValueBlock<T extends Token> extends EditableBlock<T> {
    public ValueBlock(GUI gui, DefinedBlock<?> parent, T token) {
        super(gui, parent, token);
        baseWidth = 40;
        setSize(baseWidth, 32);
        resetSlots();
        setButtonShape();
        resetFrame();
    }

    public void resetSlots() {
        undefineSlots();
        addSlot(slotShape.SQUARE, side.LEFT);
        addSlot(slotShape.CIRCLE_SLOT, side.LEFT);
        addSlot(slotShape.SQUARE, side.RIGHT);
        addSlot(slotShape.CIRCLE_SLOT, side.RIGHT);
    }

    public ArrayList<ContextToken> setNullTokens() {
        ArrayList<ContextToken> tokens = new ArrayList<>();
        tokens.add(new ContextToken(Lexer.type.NULL));
        tokens.add(new ContextToken(Lexer.type.END_OF_FILE));
        return tokens;
    }
    protected Pair<ArrayList<ContextToken>, ObjectFactory<?>> analyse(GUI frame) {
        try {
            ArrayList<ContextToken> tokens;
            try {
                if (input.equals("")) {
                    tokens = setNullTokens();
                } else {
                    tokens = frame.getLexer().analyse(input, true);
                }
            }
            catch (NullPointerException e) {
                tokens = setNullTokens();
            }
            if (tokens != null) {
                if (tokens.size() == 2) {
                    ObjectFactory<DefinedBlock<?>> factory = Lexer.type.getBlockFactory(tokens.get(0));
                    //parent.addChild(this);
                    if (factory != null) {
                        highlightText(frame.correctColour);
                        return new Pair<>(tokens, factory);
                    }
                }
            }
            throw new TypeError(null);
        }
        catch (ArtException e) {
            return null;
        }
    }
    public Pair<ArrayList<ContextToken>, ObjectFactory<?>> analyse() {
        return analyse(frame);
    }

    public void setGrammars() {
        addRule("Expression", "Expression", "$");
        addRule("Expression", "List");
        addRule("List", "List", ",", "Expression");
    }

    public Constable getValue() {
        return null;
    }
}