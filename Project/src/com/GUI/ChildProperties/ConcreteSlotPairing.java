package com.GUI.ChildProperties;

import com.GUI.BlockSlot;
import com.GUI.TokenBlock;
import com.Methods.stringMethods;

public class ConcreteSlotPairing extends SlotPairing {
    public final BlockSlot self, other;
    public ConcreteSlotPairing(TokenBlock block, BlockSlot self, BlockSlot other) {
        super(block);
        this.self = self;
        this.other = other;
    }

    @Override
    public void accept(PairingVisitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "PAIRING" + stringMethods.encapsulatedTuple("[", "]", ", ", block, self.toString(true), other.toString(true));
    }
}
