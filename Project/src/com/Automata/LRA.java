package com.Automata;

import com.Exceptions.SyntaxError;
import com.N1_Lexer.ContextToken;
import com.N1_Lexer.Lexer;
import com.N2_Parser.Actions.*;
import com.N2_Parser.ErrorCorrector;
import com.N2_Parser.Grammar.*;
import com.N2_Parser.Parser;
import com.N3_SemanticActions.SyntaxTree;
import com.N3_SemanticActions.Tokens.ConcreteToken;
import com.N3_SemanticActions.Tokens.Token;
import com.Classes.Pair;
import com.Methods.stringMethods;

import java.util.*;

public abstract class LRA<T extends Item> extends Automaton<String> {
    private final Parser<T> parser;
    private final LRStateList<T> states = new LRStateList<>();
    private ArrayList<ContextToken> derivations;
    private final ErrorCorrector<T> corrector;
    private final Class<T> clazz;

    public LRA(Class<T> clazz, Parser<T> parser, LRState<T> start) {
        this(clazz, parser);
        this.start = start;
        addState(this.start);
    }
    public LRA(Class<T> clazz, Parser<T> parser, boolean silent) {
        this(clazz, parser);
        this.silent = silent;
    }
    
    public LRA(Class<T> clazz, Parser<T> parser) {
        this.clazz = clazz;
        this.parser = parser;
        corrector = new ErrorCorrector<>(parser);
    }

    public Parser<T> getParser() {
        return parser;
    }

    @Override
    public void reset() {
        super.reset();
        derivations = new ArrayList<>();
    }

    public ItemList<T> closure(ItemList<T> i) {
        stringMethods.silentPrintLine(stringMethods.tuple("CLOSURE", i), silent);
        ArrayList<T> b;
        for (int j = 0; j < i.size(); j += 1) {
            b = getInitialisedGrammars(parser.getRulesFromName(i.get(j).getNextToken()));
            for (T p : b) {
                //methods.silentPrintLine(p + " " + i.contains(p) + " " + i.indexOf(p), silent);
                if (!(i.contains(p))) {
                    i.add(p);
                }
            }
            //methods.silentPrintLine(j + " " + i + " " + b, silent);
        }
        stringMethods.silentPrintLine(stringMethods.tuple("CLOSURE_FINISHED", i), silent);
        return new ItemList<>(clazz, i);
    }
    public abstract ItemList<T> getInitialisedGrammars(ArrayList<Grammar<T>> i);
    public Pair<LRState<T>, HashSet<T>> move(ItemList<T> i, String x) {
        stringMethods.silentPrintLine(stringMethods.tuple("MOVE", x, i), silent);
        ItemList<T> j = new ItemList<>(clazz);
        HashSet<T> k = new HashSet<>();
        for (T item : i) {
            if (item.hasNextToken()) {
                if (item.getNextToken().equals(x)) {
                    j.add((T) item.shift());
                }
            }
            else {
                k.add(item);
            }
        }
        ItemList<T> c = closure(j);
        stringMethods.silentPrintLine(stringMethods.tuple("MOVE_COMPLETED", c), silent);
        return new Pair<>(new LRState<>(clazz,this, c), k);
    }

    public LRState<T> getState(int id) {
        for (LRState<T> state : states) {
            if (state.getID() == id) {
                return state;
            }
        }
        return null;
    }

    public LRState<T> getTransitionFromState(LRState<T> state, String input) {
        for (Edge<String> edge : state.getOutput()) {
            if (edge.getInput().equals(input)) {
                return (LRState<T>) edge.getDestination();
            }
        }
        return null;
    }
    public void transition(String input) {}
    public void transition(LRState<T> state) {
        current = state;
    }
    public boolean pushToStack(String input) {
        String i = parser.getNameForAlias(input);
        if (i == null) {
            i = input;
        }
        Lexer.type tokenID = Lexer.type.getIdentifier(i);
        if (parser.getInput().get(0).getType().equals(tokenID)) {
            //System.out.println("hello");
            parser.getStack().push(parser.getInput().get(0));
            parser.getInput().remove(0);
            return true;
        }
        try {
            //System.out.println("hello : " + input + " : " + tokenID + " : " + parser.getStack().peek().getType().equals(tokenID));
            return parser.getStack().peek().getType().equals(tokenID);
        }
        catch (EmptyStackException e) {
            return false;
        }
    }
    public boolean pushToInput(ContextToken token) {
        parser.getInput().add(0, token);
        if (token == parser.getStack().peek()) {
            try {
                parser.getStack().pop();
                return true;
            } catch (EmptyStackException e) {
                return false;
            }
        }
        return false;
    }
    public int traverse(String[] input) {
        return -1;
    }

    public void printParseStage(LRState<T> current, ParserAction<T> action, ArrayList<ContextToken> input, Stack<ContextToken> stack) {
        stringMethods.silentPrintLine(current.getSimpleString() + " : " + action + " : " + stringMethods.encapsulatedTuple("[", "]", " | ", stack) + " ~ " + stringMethods.encapsulatedTuple("[", "]", " | ", input), silent);
    }
    public ArrayList<ContextToken> getDerivations() {
        return derivations;
    }
    public void traverse() {
        corrector.clearStates();
        traverse(0, 0, parser.getInput(), parser.getStack());
    }
    public Stack<ContextToken> traverse(int level, int acted, ArrayList<ContextToken> input, Stack<ContextToken> stack) throws StackOverflowError {
        boolean traversing = true;
        boolean hasAction, corrected = false;
        ContextToken token = null;
        ArrayList<ParserAction<T>> actions = null;
        LRState<T> current = (LRState<T>) start, next;
        int checkpoint = 1, passed = 0;
        ParserAction<T> action;
        while (traversing) {
            hasAction = true;
            if (!corrected) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stringMethods.silentPrintLine(silent);
                hasAction = true;
                try {
                    if (!token.equals(stack.peek())) {
                        token = stack.peek();
                    } else {
                        token = null;
                    }
                } catch (NullPointerException | EmptyStackException e) {
                    token = null;
                }
                actions = null;
                try {
                    actions = parser.getActionGrid().getActions(current, token.getType().getClassName());
                } catch (IndexOutOfBoundsException | NullPointerException e) {
                    try {
                        token = input.get(0);
                        actions = parser.getActionGrid().getActions(current, token.getType().getClassName());
                    } catch (IndexOutOfBoundsException | NullPointerException e2) {
                        if (level < 10) {
                            try {
                                traverse(level + 1, acted, input, stack);
                            } catch (StackOverflowError e3) {
                                throw e3;
                            }
                        } else {
                            throw new StackOverflowError();
                        }
                        hasAction = false;
                    }
                }
            }
            stringMethods.silentPrint(level + " : " + acted + " : " + token + " : " + actions + " : ", silent);
            if (actions != null) {
                if (actions.size() == 0) {
                    hasAction = false;
                }
            }
            else {
                hasAction = false;
            }
            corrected = false;
            if (hasAction) {
                hasAction = false;
                try {
                    corrector.addState(actions);
                    for (int i = 0; i < actions.size(); i += 1) {
                        action = actions.get(i);
                        if (action != null) {
                            next = action.getState();
                            if (action.action()) {
                                stringMethods.silentPrint("SUCCESS", silent);
                                if (action instanceof ReduceAction) {
                                    stringMethods.silentPrint(stringMethods.tuple(((ReduceAction<T>) action).getRule(), stack.get(stack.size() - 1).getChildren()), silent);
                                }
                                if (action instanceof ShiftAction) {
                                    stringMethods.silentPrint(stringMethods.tuple(((ShiftAction<T>) action).getToken(), stack.get(stack.size() - 1)), silent);
                                }
                                stringMethods.silentPrintLine(silent);
                                acted = 0;
                                passed = 0;
                                hasAction = true;

                                printParseStage(current, action, input, stack);
                                if (next != null) {
                                    current = next;
                                }
                                else if (action instanceof ReduceAction) {
                                    derivations.add(stack.peek());
                                    if (action instanceof AcceptAction) {
                                        traversing = false;
                                    }
                                }
                                break;
                            }
                            else {
                                stringMethods.silentPrint("FAILED", silent);
                                if (action instanceof ReduceAction) {
                                    stringMethods.silentPrint(stringMethods.tuple(((ReduceAction<T>) action).getRule()), silent);
                                }
                                if (action instanceof ShiftAction) {
                                    stringMethods.silentPrint(stringMethods.tuple(((ShiftAction<T>) action).getToken()), silent);
                                }
                                stringMethods.silentPrint(" ", silent);
                            }
                        } else {
                            printParseStage(current, null, input, stack);
                            acted += 1;
                        }
                    }
                    if (!hasAction) {
                        stringMethods.silentPrintLine(" ", silent);
                        printParseStage(current, null, input, stack);
                        acted += 1;
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    stringMethods.silentPrintLine("ENDING...", silent);
                    traversing = false;
                }
            }
            else {
                stringMethods.silentPrintLine("NULL", silent);
                printParseStage(current, null, input, stack);
                passed += 1;
                if (passed == 2) {
                    checkpoint = current.getID();
                    current = (LRState<T>) start;
                }
                if (passed > 3) {
                    ContextToken correction = corrector.correctBrokenStack(stack);
                    if (correction != null) {
                        passed = 0;
                        System.out.println("CORRECTION: \n" + correction.toString() + " " + correction.getChildren());
                        current = getState(checkpoint);
                    }
                    else {
                        throw new SyntaxError(token);
                    }
                }
            }
            if (acted > 10) {
                ContextToken t = stack.get(stack.size() - 1);
                ContextToken correction = corrector.correctBrokenStack(stack);
                if (correction != null) {
                    ContextToken t1 = stack.get(stack.size() - 3);
                    ContextToken c = t1;
                    while (c.getChildren().size() > 0) {
                        c = c.getChildren().get(0);
                    }
                    System.out.println("CORRECTION: " + correction.toString() + " " + c + " " + stack);
                    corrected = true;
                    token = t;
                    actions = parser.getActionGrid().getActions(current, token.getType().getClassName());
                    try {
                        Thread.sleep(5000);
                    }
                    catch (InterruptedException e) {

                    }
                }
                else {
                    throw new SyntaxError(token);
                }
            }
        }
        return stack;
    }

    public boolean addTransition(State<String> state, Edge<String> edge) {
        return false;
    }
    public boolean addTransition(LRState<T> state, Edge<String> edge) {
        return addTransition(state, edge, null);
    }
    public boolean addTransition(LRState<T> state, Edge<String> edge, T reduceRule) {
        ParserActionGrid<T> grid = parser.getActionGrid();
        String token = edge.getInput();
        if (!grid.getNames().contains(token)) {
            grid.addName(token);
        }
        ParserAction<T> action;
        if (reduceRule == null) {
            try {
                if (parser.isTerminal(token)) {
                    action = new ShiftAction<>(grid, (LRState<T>) edge.getDestination(), token);
                } else {
                    action = new GotoAction<>(grid, (LRState<T>) edge.getDestination(), token);
                }
            } catch (Exception e) {
                stringMethods.silentPrintLine(e.getMessage(), silent);
                return false;
            }
        }
        else {
            if (parser.getRule(0) == reduceRule.getGrammar()) {
                action = new AcceptAction<>(grid, this);
            }
            else {
                action = createReduceAction(grid, reduceRule);
            }
        }
        if (!grid.getActions(state, edge.getInput()).contains(action)) {
            grid.addAction(state, edge.getInput(), action);
            return true;
        }
        return false;
    }

    protected abstract ParserAction<T> createReduceAction(ParserActionGrid<T> grid, T reduceRule);

    public int getNewID() {
        int id = 0;
        for (LRState<T> s : states) {
            if (s.getID() > id) {
                id = s.getID();
            }
        }
        //methods.silentPrintLine(methods.tuple("ID", id, states.size()), silent);
        return id + 1;
    }
    public boolean addState(LRState<T> s) {
        if (!parser.getActionGrid().hasState(s)) {
            states.add(s);
            s.setID(states.size());
            return true;
        }
        return false;
    }

    public void populate() {
        populate(false);
    }
    public void populate(boolean slow) {
        LRStateList<T> t = new LRStateList<>();
        t.add(new LRState<>(clazz, this, closure(getInitialisedGrammars(parser.getRulesFromDefinition("EndOfFile")))));
        setStart(t.get(0));
        Pair<LRState<T>, HashSet<T>> move;
        LRState<T> j;
        HashSet<T> completed;
        Edge<String> edge;
        ParserActionGrid<T> grid = parser.getActionGrid();
        boolean newState;
        for (int i = 0; i < t.size(); i += 1) {
            stringMethods.silentPrintLine(silent);
            stringMethods.silentPrintLine("#----------------#", silent);
            if (slow) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stringMethods.silentPrintLine(silent);
            stringMethods.silentPrintLine(t.get(i), silent);
            grid.addState(t.get(i));
            for (Object item : t.get(i).getItems()) {
                stringMethods.silentPrintLine(silent);
                move = move(t.get(i).getItems(), ((Item) item).getNextToken());
                j = move.getKey();
                completed = move.getValue();
                newState = true;
                if (j.getItems().size() > 0) {
                    for (LRState<T> i1 : t) {
                        if (i1.equals(j)) {
                            stringMethods.silentPrintLine("OLD STATE : " + j, silent);
                            newState = false;
                            j = i1;
                            break;
                        }
                    }
                    if (newState) {
                        stringMethods.silentPrintLine("NEW STATE : " + j, silent);
                    }
                    edge = new Edge<>(((Item) item).getNextToken(), j);
                    if (addState(j)) {
                        t.add(j);
                        addTransition(t.get(i), edge);
                    }
                }
                else {
                    String token;
                    for (T g : completed) {
                        token = g.getGrammar().getDefinition()[g.getGrammar().getDefinition().length - 1];
                        edge = new Edge<>(token, new LRState<>(clazz, this, new ItemList<>(clazz)));
                        addTransition(t.get(i), edge, g);
                    }
                }
                if (slow) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        printStates(false);
    }

    public Token createParseTree() {
        ArrayList<Token> tips = new ArrayList<>();
        Token base, stem = null;
        Lexer.type name;
        for (ContextToken g : derivations) {
            stringMethods.silentPrintLine(g, silent);
            stem = new ConcreteToken(g);
            stringMethods.silentPrintLine("  STEM: " + stem, silent);
            for (ContextToken s : g.getChildren()) {
                base = null;
                name = null;
                for (int i = 0; i < tips.size(); i += 1) {
                    if (tips.get(i).getToken() == s) {
                        base = tips.get(i);
                        name = g.getType();
                        stringMethods.silentPrintLine("  MATCHED AT " + i + " " + tips + ".", silent);
                        stringMethods.silentPrintLine("    BASE: " + base.getToken() + " " + base.getChildren(), silent);
                        stringMethods.silentPrintLine("    NAME: " + name, silent);
                    }
                }
                if (name != null) {
                    stringMethods.silentPrintLine("  BASE REMOVED.", silent);
                    tips.remove(base);
                }
                else {
                    base = new ConcreteToken(s);
                }
                stem.addChild(base);
                tips.add(stem);
                stringMethods.silentPrintLine("  BASE: " + base, silent);
            }
        }
        if (stem != null) {
            stringMethods.silentPrintLine(silent);
            stringMethods.silentPrintLine(stem.toString(0), silent);
            return stem;
        }
        return null;
    }
}
