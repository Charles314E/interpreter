package com.GUI.ChildProperties;

public class PairingVisitor {
    public void visit(ConcreteSlotPairing lock) {
        lock.other.pullBlockToSlot(lock.self);
    }
    public void visit(VirtualSlotPairing lock) {
        lock.block.setLocation(
                lock.child.getX() - lock.child.getRelativeX(),
                lock.child.getY() - lock.child.getRelativeY()
        );
    }
}
