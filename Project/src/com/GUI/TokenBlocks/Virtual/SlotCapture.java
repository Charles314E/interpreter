package com.GUI.TokenBlocks.Virtual;

import com.GUI.TokenBlock;

public class SlotCapture {
    private final int index;
    private final TokenBlock.slotShape shape;
    private final TokenBlock.side side;
    public SlotCapture(int index, TokenBlock.slotShape shape, TokenBlock.side side) {
        this.index = index;
        this.shape = shape;
        this.side = side;
    }

    public int getIndex() {
        return index;
    }
    public TokenBlock.slotShape getShape() {
        return shape;
    }
    public TokenBlock.side getSide() {
        return side;
    }
}
