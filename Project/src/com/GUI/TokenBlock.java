package com.GUI;

import com.Classes.ObjectFactory;
import com.Classes.Pair;
import com.GUI.ChildProperties.PairingVisitor;
import com.GUI.ChildProperties.SlotPairing;
import com.Methods.stringMethods;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.*;

public abstract class TokenBlock extends JComponent {
    private boolean selected = false;
    private boolean beingSelected = false;
    protected GUI frame;
    protected TokenBlock parent;
    protected ArrayList<DefinedBlock<?>> potentialParents = new ArrayList<>();
    protected ArrayList<DefinedBlock<?>> children = new ArrayList<>();
    protected HashMap<TokenBlock, SlotPairing> childMap = new HashMap<>();
    protected BufferedImage border;
    protected Group area;
    private final Shape[] layers = new Shape[4];
    private Shape surface;
    private boolean slotsUndefined = false;
    protected HashMap<BlockSlot, BlockSlot> glows = new HashMap<>();
    private final ArrayList<BlockSlot> myGlows = new ArrayList<>();
    private final Color deselectedColour = Color.rgb(125, 125, 125);
    private final Color selectedColour = Color.rgb(0, 155, 0);
    private final Color colour = Color.rgb(225, 225, 225);
    private final int[][] offset = new int[layers.length][2];
    protected String input = null;
    protected Font font;
    protected Text text;
    protected Text highlightText = null;
    protected double highlight = 0;
    protected Color highlightColour = null;
    public final PairingVisitor visitor = new PairingVisitor();

    public final String className = getClass().getSimpleName();
    public final String name = className.substring(0, className.length() - 5) + "@" + hashCode();
    protected String tooltip;
    protected int baseWidth, baseHeight;
    protected int rx, ry;

    public ArrayList<DefinedBlock<?>> getPotentialParents() {
        return potentialParents;
    }

    public enum side {
        TOP, LEFT, RIGHT, BOTTOM;
    }
    public enum slotShape {
        SQUARE(Rectangle.class, Rectangle.class), CIRCLE(Arc.class, Circle.class), CIRCLE_SLOT(Arc.class, Circle.class), ROUNDED_RECTANGLE(Rectangle.class, null), DOT(Circle.class, Circle.class), DOT_SLOT(Circle.class, Circle.class);
        private final ObjectFactory<Shape> shapeFactory, areaFactory;
        slotShape(Class<? extends Shape> classShape, Class<? extends Shape> classArea) {
            this.shapeFactory = new ObjectFactory<>(classShape);
            this.areaFactory = new ObjectFactory<>(classArea);
        }
        public ObjectFactory<Shape> getShapeFactory() {
            return shapeFactory;
        }
        public ObjectFactory<Shape> getAreaFactory() {
            return areaFactory;
        }
    }

    public TokenBlock() {
        text = new Text("");
        baseHeight = (int) text.getBoundsInLocal().getHeight();
    }
    public TokenBlock(GUI gui, TokenBlock parent) {
        this();
        this.frame = gui;
        this.parent = parent;
        font = new Font("Serif", 20);
        try {
            setButtonShape();
        }
        catch (NullPointerException e) {

        }
        resetTooltip();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        colourGlows(layers[0]);
    }
    public void drawSelectedGlow() {
        if (isSelected()) {
            for (BlockSlot g : getBorderGlows()) {
                g.getGlow().setRadius(16);
                g.setGlowSpread(0.75);
            }
            frame.drawBlockGlows(this);
            //System.out.println("[DBG]: Brightened " + this + "'s glow.");
        }
        else {
            for (BlockSlot g : getBorderGlows()) {
                g.setGlowRadius(0);
                g.setGlowSpread(0);
            }
            //System.out.println("[DBG]: Dimmed " + this + "'s glow.");
        }
    }

    protected void resetTooltip() {
        if (input == null) {
            tooltip = name;
        }
        else {
            String value = input;
            if (input.equals("")) {
                value = "<empty>";
            }
            tooltip = name + " : " + value;
        }
        if (parent != null) {
            parent.resetTooltip();
            tooltip = parent.getTooltip() + " → " + tooltip;
        }
        //tooltip += " ~ " + childMap.keySet();
    }

    public String getTooltip() {
        return tooltip;
    }

    public boolean isBeingSelected() {
        return beingSelected;
    }
    private void startSelecting() {
        beingSelected = true;
    }
    public void stopSelecting() {
        beingSelected = false;
    }

    public int getRelativeX() {
        return rx;
    }

    public int getRelativeY() {
        return ry;
    }

    public BlockSlot getSlotInGlow(BlockSlot g) {
        return glows.get(g);
    }

    protected void setParentBlock(TokenBlock parent) {
        this.parent = parent;
    }

    public abstract void printTree();

    protected DefinedBlock<?> getParentBlock() {
        return (DefinedBlock<?>) parent;
    }

    public DefinedBlock<?> getHighestParent() {
        TokenBlock p = this;
        for (TokenBlock b = this; b != null; b = b.getParentBlock()) {
            p = b;
            System.out.println("[DBG]: BLOCK_PARENT: " + p);
        }
        return (DefinedBlock<?>) p;
    }

    public void setActions() {
        area.setOnMouseClicked(t -> {
            startSelecting();
            System.out.println("[DBG]: Clicked the block " + TokenBlock.this.toString() + ".");
            frame.selectBlock(this);
            System.out.println("[DBG]: " + TokenBlock.this.toString() + " has parent " + getHighestParent() + ".");
            printTree();
        });
        area.setOnMouseDragged(t -> {
            frame.selectBlock(this);
            for (BlockSlot g : getBorderGlows()) {
                g.getGlow().setRadius(16);
                g.setGlowSpread(0.25);
            }
            setLocation((int) t.getSceneX(), (int) t.getSceneY());
            //pullChildren();
            frame.drawBlockGlows(this);
        });
        area.setOnMouseReleased(t -> {
            finaliseLocks();
        });
    }

    public boolean finaliseLocks() {
        var lockSlots = frame.getLockingSlots();
        boolean locking = false;
        if (lockSlots != null) {
            lockSlots = orderLockingSlots(lockSlots);
            if (lockSlots != null) {
                BlockSlot thisSlot = lockSlots.getKey();
                BlockSlot otherSlot = lockSlots.getValue();
                System.out.println("[DBG]: LOCKING SLOTS: " + thisSlot + " + " + otherSlot);
                if (!(glows.get(thisSlot) == otherSlot || glows.get(otherSlot) == thisSlot)) {
                    if (thisSlot != null) {
                        System.out.println("[DBG]: Slots locked.");
                        frame.setFinalLockingSlots(thisSlot, otherSlot);
                        frame.resetLockingSlots();
                        locking = true;

                    }
                }
                frame.resetLockTemperature();
                if (!locking && frame.getSlotTemperature() < 1) {
                    frame.resetFinalLockingSlots();
                }
                return true;
            }
            else {
                for (TokenBlock b : childMap.keySet()) {
                    if (b.finaliseLocks()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Pair<BlockSlot, BlockSlot> orderLockingSlots(Pair<BlockSlot, BlockSlot> lockSlots) {
        if (glows.containsKey(lockSlots.getKey())) {
            return new Pair<>(lockSlots.getKey(), lockSlots.getValue());
        }
        else if (glows.containsKey(lockSlots.getValue())) {
            return new Pair<>(lockSlots.getValue(), lockSlots.getKey());
        }
        return null;
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        positionButtonShape();
        positionSlots();
        if (slotsUndefined) {
            setButtonShape();
            adjustSlots();
            for (BlockSlot g : getBorderGlows()) {
                g.resetOffset();
            }
            slotsUndefined = false;
        }
        positionSlots();
        positionButtonShape();
        //System.out.println(stringMethods.tuple(getX(), getY()) + " " + stringMethods.tuple(layers[1].getLayoutX(), layers[1].getLayoutY()) + " " + getAreaTuple());
        resetFrame();
        positionText(text);
        pullChildren();
    }

    public void positionButtonShape() {
        for (int i = 0; i < layers.length; i += 1) {
            try {
                layers[i].setLayoutX(getX() - offset[i][0]);
                layers[i].setLayoutY(getY() - offset[i][1]);
            }
            catch (NullPointerException e) {

            }
        }
    }
    public void positionSlots() {
        try {
            for (BlockSlot g : getBorderGlows()) {
                g.setLocation(getX(), getY());
            }
        }
        catch (NullPointerException e) {

        }
    }
    public void adjustSlots() {
        for (int i = 0; i < layers.length; i += 1) {
            try {
                offset[i][0] = getX() - (int) layers[i].getLayoutX();
                offset[i][1] = getY() - (int) layers[i].getLayoutY();
            }
            catch (NullPointerException e) {

            }
        }
    }
    public void pullChildren() {
        try {
            //Set<BlockGlow> notChildren = new HashSet<>(myGlows);
            for (Map.Entry<TokenBlock, SlotPairing> lock : childMap.entrySet()) {
                lock.getValue().accept(visitor);
            }
            /*if (lock instanceof ConcreteSlotPairing) {
                    notChildren.remove(((ConcreteSlotPairing) lock).self);
                }*/
            /*for (BlockGlow lock : notChildren) {
                lock.lockSlot(null);
            }*/
        }
        catch (NullPointerException e) {
            System.out.println("[ERR]: " + e.getMessage());
        }
    }

    public String getAreaTuple() {
        try {
            return stringMethods.tuple(area.getLayoutX(), area.getLayoutY(), area.getBoundsInLocal().getWidth(), area.getBoundsInLocal().getHeight());
        }
        catch (Exception e) {
            return null;
        }
    }

    public Group getArea() {
        return area;
    }

    public void setBorderIcon(BufferedImage border) {
        this.border = border;
    }
    public BlockSlot addSlot(slotShape shape, side side) {
        BlockSlot glow = new BlockSlot(this, shape, side);
        glows.put(glow, null);
        myGlows.add(glow);
        return glow;
    }

    public HashMap<BlockSlot, BlockSlot> getBorderLocks() {
        return glows;
    }
    public ArrayList<BlockSlot> getBorderGlows() {
        return myGlows;
    }

    public GUI getFrame() {
        return frame;
    }

    public void setButtonShape() throws NullPointerException {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(getX());
        rectangle.setY(getY());
        rectangle.setWidth(getWidth());
        rectangle.setHeight(getHeight());
        Shape shape = rectangle, shape1 = null;
        HashSet<Node> slots = new HashSet<>();
        for (BlockSlot slot : getBorderGlows()) {
            switch (slot.getShape()) {
                case CIRCLE -> shape = Shape.subtract(shape, Shape.subtract(slot.getBoundingBox(), slot.getArea()));
                case CIRCLE_SLOT, DOT_SLOT -> shape = Shape.subtract(shape, slot.getArea());
                case SQUARE -> {
                    try {
                        shape1 = Shape.union(shape1, slot.getArea());
                    } catch (NullPointerException e) {
                        shape1 = slot.getArea();
                    }
                }
                case DOT -> layers[3] = slot.getLine();
            }
            slots.add(slot.getSlot());
        }
        layers[1] = shape;
        if (shape1 != null) {
            layers[2] = Shape.union(shape, shape1);
            layers[0] = shape1;
        }
        else {
            layers[2] = Shape.union(shape, shape);
        }
        surface = Shape.union(layers[2], layers[2]);
        layers[2].setStroke(Color.BLACK);
        layers[2].setFill(Color.TRANSPARENT);
        if (area == null) {
            area = new Group();
        }
        else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    area.getChildren().clear();
                    try {
                        area.getChildren().add(layers[3]);
                    }
                    catch (NullPointerException e) {

                    }
                    area.getChildren().addAll(slots);
                }
            });
        }
        try {
            shape1.setVisible(true);
            Shape finalShape1 = shape1;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    area.getChildren().add(finalShape1);
                }
            });
        }
        catch (NullPointerException e) {

        }
        shape.setVisible(true);
        Shape finalShape = shape;
        Shape finalOutline = layers[2];
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                area.getChildren().add(finalShape);
                area.getChildren().add(finalOutline);
                addArea();
            }
        });
        shape.setFill(colour);
        colourGlows(shape1);
        if (isSelected()) {
            for (BlockSlot slot : getBorderGlows()) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!area.getChildren().contains(slot.getSlot())) {
                            area.getChildren().add(slot.getSlot());
                        }
                    }
                });
            }
        }
        resetFrame();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    area.getChildren().add(text);
                }
                catch (NullPointerException e) {

                }
            }
        });
    }

    public Text createText(Paint colour) {
        return createText(input, colour);
    }
    public Text createText(String str, Paint colour) {
        Text text = new Text(str);
        positionText(text);
        text.setFont(font);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setStroke(colour);
        return text;
    }
    public void positionText(Text text) {
        try {
            double tx = getX() + (baseWidth / 2.0), ty = getY() + (getHeight() / 2.0) + (baseHeight / 2.0);
            text.setX(tx);
            text.setY(ty);
            highlightText.setX(tx);
            highlightText.setY(ty);
        }
        catch (NullPointerException e) {

        }
    }

    public void highlightText(Color colour) {
        highlightColour = colour;
        highlightText = createText(colour);
        showHighlight();
    }

    protected void showHighlight() {
        highlight = 1;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    area.getChildren().add(highlightText);
                }
                catch (Exception e) {

                }
            }
        });
    }

    public void fadeTextHighlight() {
        if (highlight > 0) {
            highlight -= 0.01;
            try {
                highlightColour = new Color(highlightColour.getRed(), highlightColour.getGreen(), highlightColour.getBlue(), highlight);
                highlightText.setStroke(highlightColour);
            }
            catch (IllegalArgumentException e) {
                highlight = 0;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            area.getChildren().remove(highlightText);
                        } catch (NullPointerException e) {

                        }
                    }
                });
            }
            resetFrame();
        }
    }


    private void colourGlows(Shape shape) {
        try {
            Paint fill = shape.getFill();
            if (fill != Color.TRANSPARENT) {
                if (fill != colour) {
                    if (isSelected()) {
                        shape.setFill(selectedColour);
                    } else {
                        shape.setFill(deselectedColour);
                    }
                }
            }
        }
        catch (NullPointerException e) {

        }
    }

    public String toString() {
        return getClass().getSimpleName() + stringMethods.encapsulatedTuple("[", "]", ", ", hashCode(), getX(), getY(), selected, beingSelected);
    }

    public Shape getBottomLayer() {
        return layers[0];
    }
    public Shape getTopLayer() {
        return layers[1];
    }
    public Shape getOutline() {
        return layers[2];
    }
    public Shape getSurface() {
        return surface;
    }

    public int[] getOffset() {
        return offset[0];
    }

    public void addArea() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!frame.getSlotGroup().getChildren().contains(area)) {
                        frame.getSlotGroup().getChildren().add(area);
                    }
                }
                catch (NullPointerException e) {
                    //System.out.println("[ERR]: " + this + " could not add its shape area to a missing frame.");
                }
            }
        });
    }
    public void removeArea(boolean deleted) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (frame.getSlotGroup().getChildren().contains(area)) {
                        frame.getSlotGroup().getChildren().remove(area);
                        System.out.println("[DBG]: " + TokenBlock.this.toString() + " has successfully been deleted.");
                    }
                    else {
                        System.out.println("[DBG]: " + TokenBlock.this.toString() + " was already missing.");
                    }
                    resetFrame();
                    if (deleted) {
                        frame = null;
                    }
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                    System.out.println("[ERR]: " + TokenBlock.this.toString() + " could not remove its shape area from a missing frame.");
                }
            }
        });
    }

    public void resetFrame() {
        try {
            frame.refresh();
        }
        catch (NullPointerException e) {
            System.out.println("[ERR]: " + this + " could not refresh a missing frame.");
        }
    }

    public abstract void resetSlots();

    public void undefineSlots() {
        slotsUndefined = true;
    }
}
