package com.GUI.TokenBlocks.Values;

import com.Classes.ObjectFactory;
import com.Classes.Pair;
import com.Exceptions.ArtException;
import com.Exceptions.TypeError;
import com.GUI.DefinedBlock;
import com.GUI.EditableBlock;
import com.GUI.GUI;
import com.GUI.GrammarVisitor;
import com.N1_Lexer.ContextToken;
import com.N1_Lexer.Lexer;
import com.N3_SemanticActions.Tokens.Values.StringLiteral;

import java.util.ArrayList;

public class MethodNameBlock extends EditableBlock<StringLiteral> {
    public MethodNameBlock(GUI gui, DefinedBlock<?> parent) {
        super(gui, parent, new StringLiteral());
    }
    public MethodNameBlock() {
        this(null, null);
    }

    public void setGrammars() {
        addRule("MethodCall", "Name", "(", "List", ")");
        addRule("Name", "Identifier");
    }

    public void resetSlots() {
        undefineSlots();
        addSlot(slotShape.SQUARE, side.LEFT);
        addSlot(slotShape.CIRCLE_SLOT, side.LEFT);
        addSlot(slotShape.DOT_SLOT, side.RIGHT);
        addSlot(slotShape.CIRCLE, side.RIGHT, 1, 0);
    }

    @Override
    public void accept(GrammarVisitor v) {
        v.visit(this);
    }

    @Override
    protected Pair<ArrayList<ContextToken>, ObjectFactory<?>> analyse(GUI frame) {
        try {
            ArrayList<ContextToken> tokens = new ArrayList<>();
            try {
                if (input.equals("")) {
                    tokens.add(new ContextToken(Lexer.type.METHOD));
                    tokens.add(new ContextToken(Lexer.type.END_OF_FILE));
                    highlightText(frame.correctColour);
                    return new Pair<>(tokens, Lexer.type.getBlockFactory(Lexer.type.METHOD));
                }
                else {
                    tokens = frame.getLexer().analyse(input, true);
                }
            }
            catch (NullPointerException e) {
                return null;
            }
            if (tokens != null) {
                if (tokens.size() == 2) {
                    if (tokens.get(0).getType() == Lexer.type.IDENTIFIER) {
                        tokens.set(0, new ContextToken(Lexer.type.METHOD));
                        ObjectFactory<DefinedBlock<?>> factory = Lexer.type.getBlockFactory(Lexer.type.METHOD);
                        //parent.addChild(this);
                        if (factory != null) {
                            highlightText(frame.correctColour);
                            return new Pair<>(tokens, factory);
                        }
                    }
                }
            }
            throw new TypeError(null);
        }
        catch (ArtException e) {
            return null;
        }
    }

    @Override
    public void analyseText(GUI frame) {
        super.analyseText(frame);
        addToFrame(frame);
        addArea();
        frame.selectBlock(this);
        resetFrame();
    }
}
