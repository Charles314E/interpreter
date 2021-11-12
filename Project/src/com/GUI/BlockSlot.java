package com.GUI;

import com.GUI.ChildProperties.ConcreteSlotPairing;
import com.Methods.mathMethods;
import com.Methods.stringMethods;
import com.N1_Lexer.Lexer;
import com.N2_Parser.Grammar.PointedGrammar;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class BlockSlot {
    private final TokenBlock block;
    private BlockSlot lockedSlot;
    private DropShadow glow;
    private final TokenBlock.slotShape shape;
    private TokenBlock.slotShape partner;
    private final TokenBlock.side side;
    private final TokenBlock.side flip;
    private final Shape slot;
    private final Shape area;
    private Polyline line = null;
    private Rectangle box;
    private int x, y, xx, yy, cx, cy, ox, oy, dw, dh;
    private int bx = -1, by = -1;
    private final Color glowColour = new Color(Color.LIME.getRed(), Color.LIME.getGreen(), Color.LIME.getBlue(), 0.5);
    private final Color failedColour = new Color(Color.FIREBRICK.getRed(), Color.FIREBRICK.getGreen(), Color.FIREBRICK.getBlue(), 0.5);
    private double fading = 0;
    private double spread;

    public BlockSlot(TokenBlock block, TokenBlock.slotShape shape, TokenBlock.side side) {
        this.block = block;
        this.shape = shape;
        this.side = side;
        slot = shape.getShapeFactory().makeObject();
        area = shape.getAreaFactory().makeObject();
        createGlow();
        switch (shape) {
            case SQUARE -> partner = TokenBlock.slotShape.SQUARE;
            case CIRCLE -> partner = TokenBlock.slotShape.CIRCLE_SLOT;
            case CIRCLE_SLOT -> partner = TokenBlock.slotShape.CIRCLE;
            case DOT -> partner = TokenBlock.slotShape.DOT_SLOT;
        }
        if (shape == TokenBlock.slotShape.DOT) {
            flip = TokenBlock.side.LEFT;
            dw = 32;
            dh = (int) (block.getHeight() * 1.5) + 1;
            System.out.println("DOT : " + stringMethods.tuple(dw, dh));
        }
        else {
            switch (side) {
                case BOTTOM -> {
                    flip = TokenBlock.side.TOP;
                    dw = 0;
                    switch (shape) {
                        case SQUARE -> dh = 0;
                        case CIRCLE -> dh = -1;
                        case CIRCLE_SLOT -> dh = 1;
                    }
                }
                case TOP -> {
                    flip = TokenBlock.side.BOTTOM;
                    dw = 0;
                    switch (shape) {
                        case SQUARE -> dh = 0;
                        case CIRCLE -> dh = 1;
                        case CIRCLE_SLOT -> dh = -1;
                    }
                }
                case LEFT -> {
                    flip = TokenBlock.side.RIGHT;
                    dh = 0;
                    switch (shape) {
                        case SQUARE -> dw = 0;
                        case CIRCLE -> dw = -1;
                        case CIRCLE_SLOT -> dw = 1;
                    }
                }
                case RIGHT -> {
                    flip = TokenBlock.side.LEFT;
                    dh = 0;
                    switch (shape) {
                        case SQUARE -> dw = 0;
                        case CIRCLE -> dw = 1;
                        case CIRCLE_SLOT -> dw = -1;
                    }
                }
                default -> flip = null;
            }
        }
        positionGlow();
    }

    public TokenBlock getBlock() {
        return block;
    }
    public DropShadow getGlow() {
        return glow;
    }
    public TokenBlock.slotShape getShape() {
        return shape;
    }
    public TokenBlock.slotShape getPartnerSlot() {
        return partner;
    }
    public TokenBlock.side getSide() {
        return side;
    }
    public TokenBlock.side getFlip() {
        return flip;
    }
    public Shape getSlot() {
        return slot;
    }
    public Shape getArea() {
        return area;
    }
    public Shape getBoundingBox() {
        return box;
    }
    public Polyline getLine() {
        return line;
    }

    public void createGlow() {
        createGlow(16);
    }
    public void createGlow(int r) {
        glow = new DropShadow();
        glow.setBlurType(BlurType.GAUSSIAN);
        glow.setColor(glowColour);
        glow.setSpread(0.5);
        glow.setRadius(r);
        slot.setEffect(glow);
        slot.setVisible(true);
    }

    public double concentrateGlow(BlockSlot other, int x, int y) {
        if (shape == other.getPartnerSlot()) {
            if (side == other.getFlip()) {
                double dist = 1 - mathMethods.distance(x, y, other.getX(), other.getY()) / 32;
                if (dist == Math.max(0, Math.min(dist, 1))) {
                    return dist;
                }
            }
        }
        return -1;
    }

    public boolean lockSlot(BlockSlot partner) {
        return lockSlot(partner, false);
    }
    public boolean lockSlot(BlockSlot partner, boolean transfer) {
        boolean good = true, child = false;
        if (!transfer) {
            BlockSlot g = block.getBorderLocks().get(this);
            System.out.println("SLOTTING... " + g);
            if (g == null) {
                System.out.println("GRAMMATICAL CHILD: " + stringMethods.tuple(isGrammaticalChild(partner), partner.isGrammaticalChild(this)) + " " + stringMethods.tuple(block.className, partner.block.className));
                if (isGrammaticalChild(partner)) {
                    child = true;
                    DefinedBlock<?> b;
                    addChild(partner);
                    setLockedSlot(partner);
                    if ((b = ((DefinedBlock<?>) block).createBlockFromParse()) != null) {
                        System.out.println("PARSED_BLOCK: " + b);
                        partner.pullBlockToSlot(this);
                        layerBlocks(partner);
                    }
                    else {
                        System.out.println("PARSED_BLOCK: " + b);
                        //removeChild(partner);
                        good = false;
                    }
                    setLockedSlot(null);
                }
                else if (partner.isGrammaticalChild(this)) {
                    child = true;
                    DefinedBlock<?> b;
                    partner.addChild(this);
                    partner.setLockedSlot(this);
                    if ((b = ((DefinedBlock<?>) partner.block).createBlockFromParse()) != null) {
                        System.out.println("PARSED_BLOCK: " + b);
                        pullBlockToSlot(partner);
                        partner.layerBlocks(this);
                    }
                    else {
                        System.out.println("PARSED_BLOCK: " + b);
                        //partner.removeChild(this);
                        good = false;
                    }
                    partner.setLockedSlot(null);
                }
                else {
                    good = false;
                }
            }
        }
        if (good || child) {
            System.out.println("GOOD SLOT: " + this + " ~ " + partner);
            setLockedSlot(partner);
            try {
                partner.setLockedSlot(this);
            } catch (NullPointerException e) {

            }
            updatePartnerSlot(partner);
        }
        else {
            System.out.println("BAD SLOT: " + this + " ~ " + partner);
        }
        return (good || child);
    }
    public BlockSlot getLockedSlot() {
        return lockedSlot;
    }
    public void setLockedSlot(BlockSlot partner) {
        lockedSlot = partner;
        block.glows.put(this, partner);
    }

    public void setFading(boolean f) {
        fading = f ? 1 : 0;
        if (f) {
            spread = glow.getSpread();
            glow.setRadius(16);
        }
    }
    public boolean isFading() {
        return fading > 0;
    }
    public void fadeColour() {
        if (fading > 0) {
            fading -= 0.01;
            try {
                double red = Math.min(mathMethods.mean(glowColour.getRed(), 1 - fading, failedColour.getRed(), fading) * 2, 255);
                double green = Math.min(mathMethods.mean(glowColour.getGreen(), 1 - fading, failedColour.getGreen(), fading) * 2, 255);
                double blue = Math.min(mathMethods.mean(glowColour.getBlue(), 1 - fading, failedColour.getBlue(), fading) * 2, 255);
                //System.out.println("FADING" + stringMethods.tuple(red, green, blue));
                Color fadeColour = new Color(
                        red,
                        green,
                        blue,
                        glowColour.getOpacity());
                glow.setColor(fadeColour);
                glow.setSpread(fading * 0.65);
            }
            catch (IllegalArgumentException e) {
                fading = 0;
                glow.setColor(glowColour);
                glow.setSpread(spread);
            }
        }
        else {
            glow.setColor(glowColour);
            glow.setSpread(spread);
        }
    }

    public void setGlowRadius(double radius) {
        if (fading > 0) {
            glow.setRadius(16);
        }
        else {
            glow.setRadius(radius);
        }
    }
    public void setGlowSpread(double spread) {
        if (!(fading > 0)) {
            getGlow().setSpread(spread);
        }
    }

    public void updatePartnerSlot(BlockSlot partner) {
        int n1 = block.getBorderGlows().size(), n2 = -1;
        try {
            n2 = partner.getBlock().getBorderGlows().size();
        }
        catch (NullPointerException e) {

        }
        if (partner == null) {
            BlockSlot g = null;
            for (DefinedBlock<?> block : block.getFrame().getBlocks()) {
                if (block.glows.containsValue(this)) {
                    for (BlockSlot glow : block.getBorderGlows()) {
                        if (block.glows.get(glow) == this) {
                            block.glows.put(glow, null);
                            glow.removeChild(this);
                            g = glow;
                        }
                    }
                }
            }
            if (g != null) {
                removeChild(g);
            }
        }
        else {
            partner.getBlock().glows.put(partner, this);
            block.resetFrame();
        }
        /*System.out.println("SLOT UPDATE FOR THIS: " + n1 + " -> " + block.getBorderGlows().size());
        try {
            System.out.println("SLOT UPDATE FOR PARTNER: " + n2 + " -> " + partner.getBlock().getBorderGlows().size());
        }
        catch (NullPointerException e) {
            System.out.println("NO PARTNER");
        }*/
    }

    public void addChild(BlockSlot slot) {
        DefinedBlock<?> self = (DefinedBlock<?>) getBlock();
        DefinedBlock<?> other = (DefinedBlock<?>) slot.getBlock();
        self.childMap.put(other, new ConcreteSlotPairing(self, this, slot));
        self.children.add(other);
    }
    public void removeChild(BlockSlot slot) {
        DefinedBlock<?> self = (DefinedBlock<?>) getBlock();
        DefinedBlock<?> other = (DefinedBlock<?>) slot.getBlock();
        lockSlot(null);
        self.removeChild(other);
    }

    private boolean isGrammaticalChild(BlockSlot partner) {
        DefinedBlock<?> self = ((DefinedBlock<?>) block);
        DefinedBlock<?> other = ((DefinedBlock<?>) partner.block);
        PointedGrammar grammar = self.getSlotGrammar(this);
        System.out.println(grammar);
        try {
            DefinedBlock<?> parent = other.getHighestParent();
            boolean good = Lexer.type.getName(parent).equals(grammar.getNextToken());
            System.out.println("IS_CHILD: " + stringMethods.tuple(Lexer.type.getName(parent), grammar.getNextToken(), good));
            if (!good) {
                for (DefinedBlock<?> p : parent.getPotentialParents()) {
                    good = Lexer.type.getName(p).equals(grammar.getNextToken());
                    System.out.println("PARENT_IS_CHILD: " + stringMethods.tuple(Lexer.type.getName(p), grammar.getNextToken(), good));
                    if (good) {
                        break;
                    }
                }
            }
            return good;
        }
        catch (NullPointerException e) {
            return false;
        }
    }

    public void layerBlocks(BlockSlot partner) {
        if (shape == TokenBlock.slotShape.CIRCLE) {
            block.getArea().toFront();
            partner.getBlock().getArea().toBack();
        }
        else {
            block.getArea().toBack();
            partner.getBlock().getArea().toFront();
        }
        try {
            line.toBack();
            if (shape == TokenBlock.slotShape.DOT) {
                slot.toBack();
            }
        }
        catch (NullPointerException e) {

        }
    }

    public void pullBlockToSlot(BlockSlot partner) {
        layerBlocks(partner);
        TokenBlock other = partner.getBlock();
        if (bx < 0) {
            bx = partner.getCollisionX() - cx;
            by = partner.getCollisionY() - cy;
        }
        block.setLocation(other.getX() + bx, other.getY() + by);
    }

    public void setLocation(int x, int y) {
        if (shape == TokenBlock.slotShape.DOT) {
            this.ox = 0;
            this.oy = 0;
        }
        else {
            this.ox = (int) box.getWidth() * dw;
            this.oy = (int) box.getHeight() * dh;
        }
        this.cx = this.x + ox;
        this.cy = this.y + oy;
        this.xx = x + this.cx;
        this.yy = y + this.cy;
        setLayoutLocation(x, y);
    }
    public void setLayoutLocation(int x, int y) {
        setLayoutLocation(box, x, y);
        setLayoutLocation(slot, x, y);
        setLayoutLocation(area, x, y);
    }
    private void setLayoutLocation(Node node, int x, int y) {
        node.setLayoutX(x);
        node.setLayoutY(y);
    }
    private void setRectangleLocation(Rectangle rect, int x, int y) {
        rect.setX(x);
        rect.setY(y);
    }
    private void positionRectangle(Rectangle rect, int x, int y, int w, int h) {
        setLocation(x, y);
        setRectangleLocation(rect, x, y);
        rect.setWidth(w);
        rect.setHeight(h);
    }
    private void setArcLocation(Arc arc, int x, int y) {
        arc.setCenterX(x);
        arc.setCenterY(y);
    }
    private void positionArc(Arc arc, int x, int y, int r, int a) {
        setLocation(x, y);
        setArcLocation(arc, x, y);
        arc.setRadiusX(r);
        arc.setRadiusY(r);
        arc.setStartAngle(a - 90);
    }
    private void setCircleLocation(Circle circle, int x, int y) {
        circle.setCenterX(x);
        circle.setCenterY(y);
    }
    private void positionCircle(Circle circle, int x, int y, int r) {
        setLocation(x, y);
        setCircleLocation(circle, x, y);
        circle.setRadius(r);
    }
    private void positionBoundingBox(double x, double y, double w, double h) {
        box.setX(x);
        box.setY(y);
        box.setWidth(w);
        box.setHeight(h);
    }

    public void positionGlow() {
        int x, y, x1, y1;
        box = new Rectangle();
        switch (shape) {
            case SQUARE -> {
                Rectangle square = (Rectangle) area;
                Rectangle line = (Rectangle) slot;
                int w, h, w1 = 0, h1 = 0;
                x1 = 0;
                y1 = 0;
                switch (side) {
                    case TOP -> {
                        x = 0;
                        y = 0;
                        w = block.getWidth();
                        w1 = w;
                        h = 1;
                        h1 = 16;
                    }
                    case BOTTOM -> {
                        x = 0;
                        y = block.getHeight();
                        w = block.getWidth();
                        w1 = w;
                        h = 1;
                        h1 = 16;
                        y1 = y - h1;
                        y = y - h;
                    }
                    case LEFT -> {
                        x = 0;
                        y = 0;
                        w = 1;
                        w1 = 16;
                        x -= 1;
                        h = block.getHeight();
                        h1 = h;
                    }
                    case RIGHT -> {
                        x = block.getWidth();
                        y = 0;
                        w = 1;
                        w1 = 16;
                        x1 = x - w1;
                        x += 1;
                        h = block.getHeight();
                        h1 = h;
                        x = x - w;
                    }
                    default -> {
                        x = 0;
                        y = 0;
                        w = 0;
                        h = 0;
                    }
                }
                positionRectangle(line, x, y, w, h);
                positionRectangle(square, x1, y1, w1, h1);
                positionBoundingBox(square.getX(), square.getY(), square.getWidth(), square.getHeight());
            }
            case DOT, DOT_SLOT -> {
                Circle shape = (Circle) area;
                Circle glow = (Circle) slot;
                int r = 2;
                if (this.shape == TokenBlock.slotShape.DOT) {
                    x = dw;
                    y = dh;
                    System.out.println("DOT2 : " + stringMethods.tuple(x, y));
                    r = 2;
                    line = new Polyline(12, block.getHeight(), 12, y, dw - (r + 2), y,
                            dw - (r + 2), y - r, dw - (r + 2), y + r);
                    line.setStrokeWidth(0.25);
                    glow.setFill(glowColour);
                }
                else {
                    x = 8;
                    y = block.getHeight() / 2;
                    positionBoundingBox(bx - r, by - r, r * 2, r * 2);
                }
                positionCircle(shape, x, y, r);
                positionCircle(glow, x, y, r);
            }
            default -> {
                Circle circle = (Circle) area;
                Arc arc = (Arc) slot;
                arc.setType(ArcType.OPEN);
                arc.setLength(180);
                int r = 16, a = 0;
                x1 = 0;
                y1 = 0;
                int bx, by, bw, bh;
                switch (side) {
                    case LEFT -> {
                        x = 0;
                        x1 = x;
                        y = block.getHeight() / 2;
                        y1 = y;
                        bx = x;
                        by = 0;
                        bw = r;
                        bh = r * 2;
                        switch (shape) {
                            case CIRCLE -> {
                                x1 += block.getWidth() / 2;
                                x = x1;
                                a = 180;
                            }
                            //case CIRCLE_SLOT -> arc.setFill(Color.TRANSPARENT);
                        }
                    }
                    case RIGHT -> {
                        x = block.getWidth();
                        x1 = x;
                        y = block.getHeight() / 2;
                        y1 = y;
                        bx = x - r;
                        by = 0;
                        bw = r;
                        bh = r * 2;
                        switch (shape) {
                            case CIRCLE -> {
                                x1 -= r;
                                x = x1;
                                a = 0;
                            }
                            case CIRCLE_SLOT -> {
                                a = 180;
                                //arc.setFill(Color.TRANSPARENT);
                            }
                        }
                    }
                    default -> {
                        x = 0;
                        y = 0;
                        r = 0;
                        bx = 0;
                        by = 0;
                        bw = 0;
                        bh = 0;
                    }
                }
                positionArc(arc, x, y, r, a);
                positionCircle(circle, x1, y1, r);
                positionBoundingBox(bx, by, bw, bh);
                System.out.println("CIRCLE@" + block + " : " + stringMethods.tuple(x, y, r, a, bx, by, bw, bh));
            }
        }
        this.x = x;
        this.y = y;
        block.resetFrame();
    }

    public int getRelativeX() {
        return x;
    }
    public int getCollisionX() {
        return cx;
    }
    public int getOffsetX() {
        return ox;
    }
    public int getX() {
        return xx;
    }
    public int getRelativeY() {
        return y;
    }
    public int getCollisionY() {
        return cy;
    }
    public int getOffsetY() {
        return oy;
    }
    public int getY() {
        return yy;
    }

    public void resetOffset() {
        bx = -1;
        by = -1;
    }

    public boolean equals(Object o) {
        if (o instanceof BlockSlot) {
            return shape == ((BlockSlot) o).shape && side == ((BlockSlot) o).side;
        }
        return false;
    }

    public String toString() {
        return toString(false);
    }
    public String toString(boolean simple) {
        if (simple) {
            return "Slot" + stringMethods.encapsulatedTuple("[", "]", ", ", block, side, shape, x, y, glow.getSpread());
        }
        else {
            return "Slot" + stringMethods.encapsulatedTuple("[", "]", ", ", block, side, shape, "relative", x, y, "collision", cx, cy, "offset", ox, oy, "absolute", xx, yy, glow.getSpread());
        }
    }
}
