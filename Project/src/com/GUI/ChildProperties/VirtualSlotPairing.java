package com.GUI.ChildProperties;

import com.GUI.TokenBlock;

public class VirtualSlotPairing extends SlotPairing {
    public final TokenBlock child;
    public VirtualSlotPairing(TokenBlock block, TokenBlock child) {
        super(block);
        this.child = child;
    }

    @Override
    public void accept(PairingVisitor v) {
        v.visit(this);
    }
}
