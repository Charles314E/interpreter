package com.GUI;

import com.Data.exporting;
import com.Data.importing;
import com.Exceptions.ArtException;
import com.GUI.TokenBlocks.Virtual.VirtualBlock;
import com.Methods.stringMethods;
import com.N1_Lexer.ContextToken;
import com.N1_Lexer.Lexer;
import com.N2_Parser.Grammar.Grammar;
import com.N2_Parser.Grammar.PointedGrammar;
import com.N2_Parser.SimpleParser;
import com.N3_SemanticActions.SyntaxTree;
import com.N3_SemanticActions.Tokens.Token;

import java.util.*;

public abstract class DefinedBlock<T extends Token> extends TokenBlock {
    protected T token;
    private Token concreteToken;
    private SyntaxTree tree;
    private final HashMap<BlockSlot, PointedGrammar> slotGrammars = new HashMap<>();
    private final SimpleParser parser;
    protected ArrayList<ContextToken> tokens;
    private final String tableName = "table" + getClass().getSimpleName() + ".txt";

    public DefinedBlock(GUI gui, DefinedBlock<?> parent, T token) {
        super(gui, parent);
        this.token = token;
        parser = new SimpleParser();
        parser.addAliases("EndOfFile", "$");
        setLocation(0, 0);
        setSize(48, 32);
        setActions();
        setVisible(true);
        addToFrame(gui);
        System.out.println("[DBG]: I am " + this);
    }

    public abstract void setGrammars();

    public void createParser() {
        createParser(frame);
    }
    public void createParser(GUI frame) {
        parser.createLRA(false);
        parser.getActionGrid().printGrid();
        if (importing.fileExists(tableName)) {
            parser.copyAliases(frame.getParser());
            importing.importTable(parser.getActionGrid(), tableName);
        }
        else {
            parser.populateLRA();
            parser.copyAliases(frame.getParser());
            exporting.exportTable(parser.getActionGrid(), tableName);
        }
        System.out.println("ALIASES: " + parser.getAliases());
        redefineRules();
    }

    private void redefineRules() {
        for (Grammar<PointedGrammar> g : parser.getRules()) {
            String[] definition = g.getDefinition();
            for (int i = 0, definitionLength = definition.length; i < definitionLength; i++) {
                String token = parser.getNameForAlias(definition[i]);
                if (token != null) {
                    definition[i] = token;
                }
            }
        }
    }

    public void printTree() {
        if (isSelected()) {
            System.out.println();
            System.out.println("SYNTAX TREE@" + this + ":");
            System.out.println(tree);
            System.out.println();
        }
    }

    public BlockSlot addSlot(slotShape shape, side side) {
        return addSlot(shape, side, -1, -1);
    }
    public BlockSlot addSlot(slotShape shape, side side, int grammar) {
        return addSlot(shape, side, grammar, 0);
    }
    public BlockSlot addSlot(slotShape shape, side side, int grammar, int index) {
        BlockSlot glow = super.addSlot(shape, side);
        setGlowToGrammarToken(glow, grammar, index);
        return glow;
    }

    public void setGlowToGrammarToken(BlockSlot glow, int grammar, int index) {
        try {
            if (!(grammar == -1 || index == -1)) {
                slotGrammars.put(glow, new PointedGrammar(grammar, index));
            }
            else {
                throw new IndexOutOfBoundsException();
            }
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("[ERR]: Was not able to assign " + glow.toString(true) + " to grammar rule " + grammar + ".");
            System.out.println(parser.getRules());
            //e.printStackTrace();
        }
    }

    public void addRule(String name, String ... tokens) {
        try {
            parser.addRule(name, tokens);
            Grammar<PointedGrammar> grammar = parser.getRule(name, tokens);
            System.out.println("[DBG]: " + this + " added rule '" + grammar + "'.");
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void bindSlotsToGrammars() {
        for (PointedGrammar grammar : slotGrammars.values()) {
            grammar.setGrammarRule(parser.getRule(grammar.getID()));
        }
    }

    public PointedGrammar getSlotGrammar(BlockSlot slot) {
        return slotGrammars.get(slot);
    }

    private SyntaxTree getSyntaxTree() {
        return tree;
    }

    public DefinedBlock<?> parse() {
        Stack<ContextToken> stack = new Stack<>();
        ArrayList<ContextToken> input = new ArrayList<>(tokens);
        stack.push(input.get(0));
        input.remove(0);
        System.out.println();
        Token t = parser.parse(input, stack);
        System.out.println("BLOCK CHILDREN: " + t.toString(0));
        tree = new SyntaxTree(t, t.getChild(0).getChild(0));
        try {
            DefinedBlock<?> block = Lexer.type.getBlockFactory(tree.getConcreteRoot().getToken().getType()).makeObject();
            block.setConcreteToken(tree.getConcreteRoot());
            return block;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addToFrame(GUI gui) {
        try {
            gui.getJFX().add(this);
            gui.addBlock(this);
            frame = gui;
            resetFrame();
            System.out.println("[DBG]: " + this + " was added to frame " + gui + ".");
        }
        catch (NullPointerException e) {
            System.out.println("[ERR]: " + this + " could not be added to a missing frame.");
        }
    }

    public void removeFromFrame() {
        try {
            frame.getJFX().remove(this);
            frame.removeBlock(this);
            removeArea(true);
            resetFrame();
        }
        catch (NullPointerException e) {
            //System.out.println("[ERR]: " + this + " could not be removed from a missing frame.");
        }
    }

    public T getToken() {
        return token;
    }
    public void setToken(T token) {
        this.token = token;
    }
    public void setConcreteToken(Token token) {
        this.concreteToken = token;
    }
    public Token getConcreteToken() {
        return concreteToken;
    }

    public void reset(GUI frame, int x, int y, boolean show) {
        if (show) {
            addToFrame(frame);
            addArea();
        }
        setLocation(x, y);
        setupParser(frame.grammarVisitor);
        bindSlotsToGrammars();
    }

    public abstract void accept(GrammarVisitor v);

    public void setupParser(GrammarVisitor v) {
        accept(v);
        createParser(v.frame);
    }

    public DefinedBlock<?> createBlockFromParse(boolean show) {
        return createBlockFromParse(show, createTokensFromSlots(), children.toArray(new DefinedBlock<?>[0]));
    }
    public DefinedBlock<?> createBlockFromParse() {
        return createBlockFromParse(createTokensFromSlots(), children.toArray(new DefinedBlock<?>[0]));
    }

    private ArrayList<ContextToken> createTokensFromSlots() {
        int max, number, highest = 0, i;
        boolean added, good = false, first;
        ArrayList<ContextToken> tokens = new ArrayList<>(), temp;
        ArrayList<BlockSlot> failedSlots = new ArrayList<>(), tempSlots;
        String[] definition;
        Token t;
        for (Grammar<PointedGrammar> grammar : parser.getRules()) {
            System.out.println();
            System.out.println("TESTING GRAMMAR '" + grammar + "'...");
            definition = grammar.getDefinition();
            System.out.println("DEFINITION: " + Arrays.asList(definition));
            temp = new ArrayList<>();
            tempSlots = new ArrayList<>();
            max = 1;
            number = 0;
            i = 0;
            first = true;
            if (definition.length == 1) {
                String name = Lexer.type.getName(this);
                try {
                    if (name.equals(definition[0])) {
                        good = true;
                        tokens = new ArrayList<>();
                        tokens.add(new ContextToken(Lexer.type.getIdentifier(name)));
                    }
                }
                catch (NullPointerException e) {
                    good = false;
                }
            }
            else {
                for (String token : definition) {//int i = 0, definitionLength = definition.length; i < definitionLength; i++) {
                    //token = definition[i];
                    added = false;
                    System.out.print("TOKEN: " + token + " (index " + i + ", length " + definition.length + ")");
                    for (Map.Entry<BlockSlot, PointedGrammar> binding : slotGrammars.entrySet()) {
                        if (binding.getValue().getGrammar() == grammar) {
                            if (binding.getValue().getPointerPosition() == i) {
                                System.out.print(" ... HAS SLOT BINDING");
                                if (!first) {
                                    max += 1;
                                }
                                first = false;
                                if (binding.getKey().getLockedSlot() != null) {
                                    System.out.println(" ... ADDED");
                                    number += 1;
                                    added = true;
                                    try {
                                        t = ((DefinedBlock<?>) binding.getKey().getLockedSlot().getBlock()).getParentBlock().getConcreteToken();
                                        temp.add(t.getToken());
                                        //System.out.println(t.getChildren());
                                    } catch (Exception e) {

                                    }
                                } else {
                                    tempSlots.add(binding.getKey());
                                }
                            }
                        }
                    }
                    if (!added) {
                        System.out.println();
                        String name = parser.getNameForAlias(token);
                        temp.add(new ContextToken(Lexer.type.getIdentifier(name != null ? name : token)));
                    }
                    i += 1;
                }
                if (number == max) {
                    if (max >= highest) {
                        good = true;
                        highest = number;
                        tokens = temp;
                    }
                }
                failedSlots = tempSlots;
            }
            System.out.println("PARSED_TOKENS: " + tokens);
        }
        if (good) {
            if (tokens.get(tokens.size() - 1).getType() != Lexer.type.END_OF_FILE) {
                tokens.add(new ContextToken(Lexer.type.END_OF_FILE));
            }
            System.out.println("FINALISED_TOKENS: " + tokens);
            return tokens;
        }
        System.out.println("PARSING_UNSUCCESSFUL");
        System.out.println();
        for (BlockSlot g : failedSlots) {
            g.setFading(true);
        }
        return null;
    }

    public void addPotentialParent() {
        DefinedBlock<?> parent = createBlockFromParse();
        if (parent != null) {
            potentialParents.add(parent);
            System.out.println("[DBG]: Adding potential parent " + parent + " to " + this + "...");
        }
        else {
            System.out.println("[DBG]: Could not add a potential parent to " + this + ".");
        }
        setParentBlock(null);
    }

    public DefinedBlock<?> createBlockFromParse(ArrayList<ContextToken> tokens, DefinedBlock<?> ... children) {
        return createBlockFromParse(true, tokens, children);
    }
    public DefinedBlock<?> createBlockFromParse(boolean show, ArrayList<ContextToken> tokens, DefinedBlock<?> ... children) {
        return createBlockFromParse(frame, show, tokens, children);
    }
    public DefinedBlock<?> createBlockFromParse(GUI frame, boolean show, ArrayList<ContextToken> tokens, DefinedBlock<?> ... children) {
        return createBlockFromParse(frame, show, this, tokens, children);
    }
    public DefinedBlock<?> createBlockFromParse(GUI frame, boolean show, DefinedBlock<?> block, ArrayList<ContextToken> tokens, DefinedBlock<?> ... children) {
        if (tokens != null) {
            System.out.println("ID: " + hashCode());
            if (block != this) {
                block.removeFromFrame();
            }
            block.tokens = tokens;
            try {
                block.setParentBlock(block.parse());
                System.out.println("PARENT_TREE:");
                System.out.println(block.getParentBlock().getSyntaxTree());
            } catch (ArtException e) {
                e.printStackTrace();
            }
            if (block.getParentBlock() != null) {
                block.getParentBlock().setupParser(frame.grammarVisitor);
                block.resetTooltip();
                ArrayList<Token> arguments = new ArrayList<>();
                System.out.println("CHILD: " + Arrays.toString(children));
                for (DefinedBlock<?> child : children) {
                    arguments.add(child.token);
                    try {
                        child.getParentBlock().setParentBlock(block);
                    } catch (NullPointerException e) {
                        child.setParentBlock(block);
                    }
                    child.resetTooltip();
                }
                Token t = block.getToken().construct(arguments);
                System.out.println("TOKEN: " + block.getToken());
                System.out.println("t = " + t.toString(0));
                System.out.println(stringMethods.tuple(block == this, block.frame == frame));
                if (show) {
                    if (!(block == this && block.frame == frame)) {
                        for (BlockSlot g : getBorderGlows()) {
                            if (glows.get(g) != null) {
                                for (BlockSlot g1 : block.getBorderGlows()) {
                                    if (g.equals(g1)) {
                                        g1.lockSlot(glows.get(g), true);
                                        g.lockSlot(null);
                                        break;
                                    }
                                }
                            }
                        }
                        if (block != this) {
                            removeFromFrame();
                        }
                        block.addToFrame(frame);
                        block.addArea();
                        frame.selectBlock(block);
                        block.resetFrame();
                    }
                    System.out.println("[DBG]: " + block + " has been successfully created with parent " + block.getParentBlock() + ".");
                }
            }
            DefinedBlock<?> highestParent = block.getHighestParent();
            if (highestParent instanceof VirtualBlock) {
                highestParent.addPotentialParent();
            }
            return block.getParentBlock();
        }
        return null;
    }

    public void removeChild(DefinedBlock<?> block) {
        System.out.println("[DBG]: " + this + " is removing child " + block);
        childMap.remove(block);
        children.remove(block);
    }
}
