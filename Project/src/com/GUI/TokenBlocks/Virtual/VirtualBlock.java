package com.GUI.TokenBlocks.Virtual;

import com.GUI.BlockSlot;
import com.GUI.ChildProperties.VirtualSlotPairing;
import com.GUI.DefinedBlock;
import com.GUI.GUI;
import com.GUI.TokenBlock;
import com.Methods.stringMethods;
import com.N3_SemanticActions.Tokens.Token;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class VirtualBlock<T extends Token> extends DefinedBlock<T> {
    protected ArrayList<SlotCapture> captures = new ArrayList<>();
    public VirtualBlock(GUI gui, DefinedBlock<?> parent, T token) {
        super(gui, parent, token);
    }

    public void setSelected(boolean selected) {}

    public void setButtonShape() {
        area = new Group();
    }

    public abstract void setCaptureSlots();

    protected void createSlotCapture(int index, slotShape shape, side side) {
        captures.add(new SlotCapture(index, shape, side));
    }

    @Override
    public void resetSlots() {
        TokenBlock block;
        HashMap<BlockSlot, BlockSlot> glows = new HashMap<>();
        for (SlotCapture slot : captures) {
            block = children.get(slot.getIndex());
            for (BlockSlot g : block.getBorderGlows()) {
                if (g.getShape() == slot.getShape() && g.getSide() == slot.getSide()) {
                    glows.put(g, block.getSlotInGlow(g));
                }
            }
        }
        this.glows.clear();
        this.glows = glows;
    }

    public void addChild(DefinedBlock<?> block) {
        childMap.put(block, new VirtualSlotPairing(this, block));
        children.add(block);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + stringMethods.encapsulatedTuple("[", "]", ", ", hashCode(), getX1(), getY1(), getX2(), getY2());
    }

    public Object getX1() {
        return getX();
    }
    public Object getY1() {
        return getY();
    }
    public Object getX2() {
        return getX() + getWidth();
    }
    public Object getY2() {
        return getY() + getHeight();
    }
}
