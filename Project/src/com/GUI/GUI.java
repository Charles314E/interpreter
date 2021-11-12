package com.GUI;

import com.GUI.TokenBlocks.Headers.IfHeaderBlock;
import com.GUI.TokenBlocks.Headers.WhileHeaderBlock;
import com.GUI.TokenBlocks.Operators.EqSigns.GreaterThanEqualsBlock;
import com.GUI.TokenBlocks.Operators.EqSigns.NotEqualsBlock;
import com.GUI.TokenBlocks.Operators.OpSigns.ExponentBlock;
import com.GUI.TokenBlocks.Operators.OpSigns.ModuloBlock;
import com.GUI.TokenBlocks.Values.IdentifierBlock;
import com.GUI.TokenBlocks.Values.MethodNameBlock;
import com.N1_Lexer.ArtLexer;
import com.N2_Parser.ArtParser;
import com.N3_SemanticActions.SyntaxTree;
import com.Classes.ObjectFactory;
import com.Classes.Pair;
import com.Methods.stringMethods;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUI extends JFrame {
    private final ArtLexer lexer = new ArtLexer();
    private final ArtParser parser = new ArtParser();
    private final int w, h;
    private double mx = 0, my = 0;
    private int lockCooldown = 10;
    private int lockTemperature = -1;
    private final Group slotGroup = new Group();
    private final Scene scene = new Scene(slotGroup, getWidth(), getHeight());
    private final JFXPanel jfx = new JFXPanel();
    private final ArrayList<DefinedBlock<?>> blocks = new ArrayList<>();
    private final SyntaxTree tree = new SyntaxTree(null);
    private final Tooltip tooltip = new Tooltip();
    private Pair<BlockSlot, BlockSlot> lockingSlots, finalLockingSlots;
    public final GrammarVisitor grammarVisitor = new GrammarVisitor(this);

    public final javafx.scene.paint.Color correctColour = javafx.scene.paint.Color.rgb(0, 255, 0);
    public final javafx.scene.paint.Color incorrectColour = javafx.scene.paint.Color.rgb(225, 0, 0);

    public GUI(int width, int height) {
        w = width;
        h = height;
        System.out.println(stringMethods.tuple(slotGroup.getBoundsInLocal().getWidth(), slotGroup.getBoundsInLocal().getHeight()));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        run();
    }

    public Group getSlotGroup() {
        return slotGroup;
    }

    public Scene getScene() {
        return scene;
    }

    public void run() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initAndShowGUI();
            }
        });
    }

    public void initAndShowGUI() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                jfx.setScene(scene);
            }
        });

        setSize(w, h);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        add(jfx);
        jfx.setBackground(Color.WHITE);
        jfx.setVisible(true);
        System.out.println(this);
        slotGroup.setVisible(true);
        Tooltip.install(slotGroup, tooltip);

        createBlock(new IdentifierBlock(), 32, 32);
        createBlock(new IdentifierBlock(), 96, 128);
        createBlock(new IfHeaderBlock(), 256, 64);
        createBlock(new WhileHeaderBlock(), 96, 256);
        createBlock(new MethodNameBlock(), 156, 96);
        createBlock(new ExponentBlock(), 128, 32);
        createBlock(new NotEqualsBlock(), 128, 64);
        createBlock(new GreaterThanEqualsBlock(), 204, 16);
        createBlock(new ModuloBlock(), 64, 64);
        System.out.println(blocks);
        refresh();

        for (int i = 0; i < slotGroup.getChildren().size(); i += 1) {
            slotGroup.getChildren().get(i).setVisible(true);
        }
        jfx.setSize(getSize());
        System.out.println(jfx);
        
        System.out.println(stringMethods.tuple(slotGroup.getBoundsInLocal().getWidth(), slotGroup.getBoundsInLocal().getHeight()));

        scene.setOnMouseClicked(t -> {
            System.out.println("[DBG]: Clicked the scene.");
            boolean analysing = true;
            for (TokenBlock block : blocks) {
                if (block.isBeingSelected()) {
                    analysing = false;
                    break;
                }
            }
            if (analysing) {
                TokenBlock block;
                EditableBlock<?> eBlock;
                for (TokenBlock tokenBlock : blocks) {
                    block = tokenBlock;
                    if (block instanceof EditableBlock) {
                        if (block.isSelected()) {
                            eBlock = (EditableBlock<?>) block;
                            if (eBlock.wasEdited()) {
                                eBlock.analyseText();
                            }
                        }
                    }
                }
            }
            selectBlock(null);
            for (TokenBlock block : blocks) {
                block.stopSelecting();
            }
        });
        //Taken from Stack Overflow - https://stackoverflow.com/questions/53740820/how-do-i-add-a-tooltip-to-a-rectangular-region-of-a-javafx-canvas
        scene.setOnMouseMoved(e -> {
            mx = e.getX();
            my = e.getY();
        });
        scene.setOnMouseExited(e -> {
            tooltip.hide();
        });
        scene.setOnMouseReleased(e -> {
            if (lockingSlots != null) {
                if (lockingSlots.getKey().concentrateGlow(lockingSlots.getValue(), lockingSlots.getKey().getX(), lockingSlots.getKey().getY()) < 0.65) {
                    lockingSlots = null;
                }
            }
        });
        scene.setOnKeyPressed(t -> {
            for (int i = 0, blocksSize = blocks.size(); i < blocksSize; i++) {
                TokenBlock block = blocks.get(i);
                if (block instanceof EditableBlock) {
                    EditableBlock<?> eBlock = (EditableBlock<?>) block;
                    if (eBlock.isSelected()) {
                        eBlock.addToInput(t);
                    }
                }
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (TokenBlock block : blocks) {
                    if (block instanceof EditableBlock) {
                        EditableBlock<?> eBlock = (EditableBlock<?>) block;
                        eBlock.fadeTextHighlight();
                    }
                    if (block.getArea().contains(mx, my)) {
                        tooltip.setText(block.getTooltip());
                        //System.out.println("[DBG]: " + block + " is being hovered over.");
                    }
                    for (BlockSlot g : block.getBorderGlows()) {
                        if (g.isFading()) {
                            g.fadeColour();
                        }
                    }
                    if (lockTemperature > -1) {
                        if (lockTemperature > 0) {
                            //System.out.println("[DBG]: All slots are " + lockTemperature + "°C.");
                        }
                        if (lockTemperature == 0) {
                            try {
                                BlockSlot thisSlot = finalLockingSlots.getKey();
                                BlockSlot otherSlot = finalLockingSlots.getValue();
                                if (otherSlot.lockSlot(thisSlot)) {
                                    thisSlot.pullBlockToSlot(otherSlot);
                                    resetFinalLockingSlots();
                                    resetLockingSlots();
                                    System.out.println("[DBG]: All slots have cooled down.");
                                    System.out.println("[DBG]: " + lockingSlots);
                                }
                                else {
                                    otherSlot.setFading(true);
                                }
                            } catch (NullPointerException e) {

                            }
                        }
                        lockTemperature -= 1;
                    }
                }
            }
        }.start();
    }

    public JFXPanel getJFX() {
        return jfx;
    }

    public void addBlock(DefinedBlock<?> block) {
        blocks.add(block);
    }
    public void removeBlock(DefinedBlock<?> block) {
        blocks.remove(block);
    }
    public ArrayList<DefinedBlock<?>> getBlocks() {
        return blocks;
    }

    public void selectBlock(TokenBlock block) {
        deselectAllBlocks();
        if (block != null) {
            block.setSelected(true);
        }
        for (TokenBlock b : blocks) {
            b.drawSelectedGlow();
        }
    }
    public void deselectAllBlocks() {
        for (TokenBlock block : blocks) {
            //System.out.println("[DBG]: Deselecting " + block + ".");
            if (!block.isBeingSelected()) {
                block.setSelected(false);
            }
        }
    }

    public void refresh() {
        revalidate();
        repaint();
    }

    public void drawBlockGlows(TokenBlock block) {
        for (DefinedBlock<?> b : getBlocks()) {
            if (b != block) {
                for (BlockSlot g : b.getBorderGlows()) {
                    g.getGlow().setRadius(16);
                }
                makeSlotsGlow(block, b);
            }
        }
    }
    public boolean makeSlotsGlow(TokenBlock b1, TokenBlock b2) {
        return makeSlotsGlow(b1, b2, -1, -1);
    }
    public boolean makeSlotsGlow(TokenBlock b1, TokenBlock b2, int x, int y) {
        BlockSlot maxGlow1 = null, maxGlow2 = null;
        double dist, maxDist = 0;
        for (BlockSlot g : b1.getBorderGlows()) {
            for (BlockSlot bg : b2.getBorderGlows()) {
                if (x < 0 || y < 0) {
                    dist = g.concentrateGlow(bg, g.getX(), g.getY());
                }
                else {
                    dist = g.concentrateGlow(bg, x, y);
                }
                if (dist != -1) {
                    if (dist > maxDist) {
                        maxDist = dist;
                        maxGlow1 = g;
                        maxGlow2 = bg;
                    }
                }
            }
        }
        if (maxDist > 0.5) {
            //System.out.println("[DBG]: " + maxGlow1 + " <--> " + maxGlow2 + " ~ " + maxDist);
            try {
                maxGlow2.setGlowSpread(maxDist * 0.9);
                if (maxDist > 0.65) {
                    lockingSlots = new Pair<>(maxGlow1, maxGlow2);
                    //System.out.println("[DBG]: Created locking slots.");
                    return true;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void execute(DefinedBlock<?> block) {
        tree.execute(block.getToken());
    }

    public DefinedBlock<?> createBlock(ObjectFactory<? extends DefinedBlock<?>> factory, int x, int y, boolean show, boolean refresh, boolean analyse) {
        DefinedBlock<?> block = createBlock(factory.makeObject(), x, y, show, analyse);
        /*DefinedBlock block = factory.makeObject();
        block.addToFrame(this);
        block.addArea();
        block.setLocation(x, y);*/
        if (refresh) {
            block.resetFrame();
        }
        return block;
    }

    public DefinedBlock<?> createBlock(DefinedBlock<?> block, int x, int y, boolean show, boolean analyse) {
        if (block instanceof EditableBlock) {
            ((EditableBlock<?>) block).reset(this, x, y, show, analyse);
        }
        else {
            block.reset(this, x, y, show);
        }
        System.out.println("[DBG]: Created " + block + ".");
        return block;
    }

    public DefinedBlock<?> createBlock(DefinedBlock<?> block, int x, int y) {
        return createBlock(block, x, y, true, true);
    }

    public Pair<BlockSlot, BlockSlot> getLockingSlots() {
        return lockingSlots;
    }
    public void resetLockingSlots() {
        lockingSlots = null;
    }
    public void resetLockTemperature() {
        lockTemperature = lockCooldown;
    }
    public int getSlotTemperature() {
        return lockTemperature;
    }
    public void setFinalLockingSlots(BlockSlot keySlot, BlockSlot lockSlot) {
        finalLockingSlots = new Pair<>(keySlot, lockSlot);
    }
    public void resetFinalLockingSlots() {
        finalLockingSlots = null;
    }

    public ArtLexer getLexer() {
        return lexer;
    }
    public ArtParser getParser() {
        return parser;
    }
}
