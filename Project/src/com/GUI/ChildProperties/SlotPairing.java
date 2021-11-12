package com.GUI.ChildProperties;

import com.GUI.TokenBlock;

public abstract class SlotPairing {
    public final TokenBlock block;

    protected SlotPairing(TokenBlock block) {
        this.block = block;
    }

    public abstract void accept(PairingVisitor v);
}
