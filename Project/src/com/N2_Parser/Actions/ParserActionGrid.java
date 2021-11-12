package com.N2_Parser.Actions;

import com.Automata.LRState;
import com.N2_Parser.Grammar.Item;
import com.N2_Parser.Parser;
import com.Methods.stringMethods;

import java.util.*;

public class ParserActionGrid<T extends Item> {
    private HashMap<LRState<T>, HashMap<String, ParserActionList<T>>> actionGrid = new HashMap<>();
    private final HashSet<String> names;
    private final Parser<T> parser;

    public ParserActionGrid(Parser<T> parser, HashSet<String> names) {
        this.parser = parser;
        this.names = names;
    }

    public void setActionGrid(HashMap<LRState<T>, HashMap<String, ParserActionList<T>>> grid) {
        actionGrid = grid;
    }
    public HashMap<LRState<T>, HashMap<String, ParserActionList<T>>> getActionGrid() {
        return actionGrid;
    }

    public void addState(LRState<T> state) {
        if (actionGrid.get(state) == null) {
            HashMap<String, ParserActionList<T>> input = new HashMap<>();
            for (String name : names) {
                input.put(name, new ParserActionList<T>());
            }
            actionGrid.put(state, input);
        }
    }

    public HashSet<String> getNames() {
        return names;
    }
    public void addName(String name) {
        names.add(name);
        for (Map.Entry<LRState<T>, HashMap<String, ParserActionList<T>>> state : actionGrid.entrySet()) {
            state.getValue().putIfAbsent(name, new ParserActionList<T>());
        }
    }

    public Set<LRState<T>> getStates() {
        return actionGrid.keySet();
    }
    public boolean hasState(LRState<T> state) {
        return (actionGrid.get(state) != null);
    }

    public Parser<T> getParser() {
        return parser;
    }

    public void addAction(int state, String input, String action) {
        addAction(state, input, action, -1);
    }
    public void addAction(int state, String input, String action, int i) {
        addAction(parser.getLRA().getState(state), input, constructAction(input, action), i);
    }
    public void addAction(LRState<T> state, String input, ParserAction<T> action) {
        addAction(state, input, action, -1);
    }
    public void addAction(LRState<T> state, String input, ParserAction<T> action, int i) {
        if (actionGrid.get(state) == null) {
            addState(state);
        }
        ParserActionList<T> actions = actionGrid.get(state).get((parser.getNameForAlias(input) == null ? input : parser.getNameForAlias(input)));
        if (actions == null) {
            actions = new ParserActionList<T>();
            actionGrid.get(state).put(input, actions);
        }
        if (i == -1) {
            actions.add(action);
        }
        else {
            actions.add(i, action);
        }
    }
    public void replaceAction(int state, String input, String oldAction, String newAction) {
        removeAction(state, input, oldAction);
        addAction(state, input, newAction);
    }
    public int removeAction(int state, String input, String action) {
        ArrayList<ParserAction<T>> actions = getActions(parser.getLRA().getState(state), input);
        try {
            int n = -1;
            for (int i = 0; i < actions.size(); i += 1) {
                System.out.println(actions.get(i));
                if (actions.get(i).toString().equals(action)) {
                    n = i;
                }
            }
            actions.remove(n);
            return n;
        }
        catch (IndexOutOfBoundsException | NullPointerException e) {
            return -1;
        }
    }
    public ParserAction<T> removeAction(int state, String input, int index) {
        ArrayList<ParserAction<T>> actions = getActions(parser.getLRA().getState(state), input);
        try {
            return actions.remove(index);
        }
        catch (IndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }
    public void clearAction(ParserAction<T> action) {
        for (Map.Entry<LRState<T>, HashMap<String, ParserActionList<T>>> state : actionGrid.entrySet()) {
            for (Map.Entry<String, ParserActionList<T>> cell : state.getValue().entrySet()) {
                for (int i = 0; i < cell.getValue().size(); i += 1) {
                    if (cell.getValue().get(i).equals(action)) {
                        cell.getValue().remove(i);
                    }
                }
            }
        }
    }
    public void clearAction(String action) {
        for (Map.Entry<LRState<T>, HashMap<String, ParserActionList<T>>> state : actionGrid.entrySet()) {
            for (Map.Entry<String, ParserActionList<T>> cell : state.getValue().entrySet()) {
                for (int i = 0; i < cell.getValue().size(); i += 1) {
                    if (cell.getValue().get(i).toString().equals(action)) {
                        cell.getValue().remove(i);
                    }
                }
            }
        }
    }
    public ParserAction<T> getAction(int state, String input, String action) {
        ArrayList<ParserAction<T>> actions = getActions(parser.getLRA().getState(state), input);
        try {
            for (ParserAction<T> a : actions) {
                if (a.toString().equals(action)) {
                    return a;
                }
            }
        }
        catch (IndexOutOfBoundsException | NullPointerException e) {

        }
        return null;
    }
    public ArrayList<ParserAction<T>> getActions(int state, String input) {
        return getActions(parser.getLRA().getState(state), input);
    }
    public ArrayList<ParserAction<T>> getActions(LRState<T> state, String input) {
        try {
            String name = parser.getNameForAlias(input);
            return actionGrid.get(state).get(name != null ? name : input);
        }
        catch (NullPointerException e) {
            System.out.println(stringMethods.tuple("getActions()", e.getMessage()));
            return null;
        }
    }

    public void printGrid() {
        StringBuilder fs = new StringBuilder();
        HashMap<String, String> aliases = parser.getAliases();
        boolean shortened;
        System.out.print("               ");
        for (String name : names) {
            fs.append("%16s");
            shortened = false;
            for (Map.Entry<String, String> alias : aliases.entrySet()) {
                if (alias.getValue().equals(name)) {
                    System.out.printf("%14s", alias.getKey());
                    shortened = true;
                    break;
                }
            }
            if (!shortened) {
                System.out.printf("%14s", name.substring(0, Math.min(name.length(), 2)));
            }
            System.out.print("  ");
        }
        System.out.println();
        for (int i = 0; i < names.size() + 1; i += 1) {
            System.out.print("---------------#");
        }
        System.out.println();
        ArrayList<ParserAction<T>> action;
        for (Map.Entry<LRState<T>, HashMap<String, ParserActionList<T>>> state : actionGrid.entrySet()) {
            try {
                System.out.printf("%-15s", state.getKey().getSimpleString());
                System.out.print("|");
                for (String name : names) {
                    action = state.getValue().get(name);
                    if (action != null) {
                        System.out.printf("%12s", stringMethods.encapsulatedTuple("", "", ",", action));
                        System.out.print("    ");
                    } else {
                        System.out.printf("%16s", "");
                    }
                }
                System.out.println();
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<LRState<T>> appendNewAction(String[] actions, String ... newActions) {
        ArrayList<LRState<T>> states = new ArrayList<>();
        for (String action : actions) {
            states.addAll(appendNewAction(action, newActions));
        }
        return states;
    }
    public ArrayList<LRState<T>> appendNewAction(String action, String ... newActions) {
        ArrayList<LRState<T>> states = new ArrayList<>();
        for (String newAction : newActions) {
            states.addAll(insertAction(action, newAction, false));
        }
        return states;
    }
    public void replaceAction(String oldAction, String newAction) {
        insertAction(oldAction, newAction, true);
    }
    public ArrayList<LRState<T>> insertAction(String oldAction, String newAction, boolean replace) {
        ParserActionList<T> actions;
        ArrayList<LRState<T>> states = new ArrayList<>();
        for (Map.Entry<LRState<T>, HashMap<String, ParserActionList<T>>> row : actionGrid.entrySet()) {
            for (Map.Entry<String, ParserActionList<T>> cell : row.getValue().entrySet()) {
                actions = cell.getValue();
                for (int i = 0; i < actions.size(); i += 1) {
                    try {
                        if (actions.get(i).toString().equals(oldAction)) {
                            if (replace) {
                                actions.remove(i);
                                actions.add(i, constructAction(cell.getKey(), newAction));
                            } else {
                                actions.add(constructAction(cell.getKey(), newAction));
                            }
                            states.add(row.getKey());
                        }
                    }
                    catch (NullPointerException e) {
                        e.printStackTrace();
                        System.out.println(stringMethods.tuple(row.getKey().toString(true), cell.getKey(), actions, i));
                    }
                }
            }
        }
        return states;
    }

    public ParserAction<T> constructAction(String input, String newAction) {
        ParserAction<T> action = null;
        StringBuilder number = new StringBuilder();
        int end = -1;
        for (int i = 1; i < newAction.length(); i += 1) {
            try {
                Integer.parseInt(String.valueOf(newAction.charAt(i)));
                number.append(newAction.charAt(i));
            }
            catch (NumberFormatException e) {
                end = i;
                break;
            }
        }
        switch (newAction.charAt(0)) {
            case 's': action = new ShiftAction<>(this, parser.getLRA().getState(Integer.parseInt(number.toString())), input); break;
            case 'g': action = new GotoAction<>(this, parser.getLRA().getState(Integer.parseInt(number.toString())), input); break;
            case 'r':
                if (end > -1) {
                    int offset = -1;
                    try {
                        if (newAction.substring(end, end + 5).equals("ifnot")) {
                            offset = 5;
                        }
                        else if (newAction.substring(end, end + 2).equals("if")) {
                            offset = 2;
                        }
                    }
                    catch (StringIndexOutOfBoundsException e) {
                        if (newAction.substring(end, end + 2).equals("if")) {
                            offset = 2;
                        }
                    }
                    if (offset > 0) {
                        StringBuilder lookahead = new StringBuilder();
                        for (int i = end + offset; i < newAction.length(); i += 1) {
                            lookahead.append(newAction.charAt(i));
                        }
                        switch (offset) {
                            case 2: action = new LookaheadToReduceAction(this, parser.getRule(Integer.parseInt(number.toString())), lookahead.toString().split("or")); break;
                            case 5: action = new NoLookaheadToReduceAction(this, parser.getRule(Integer.parseInt(number.toString())), lookahead.toString().split("or")); break;
                        }
                    }
                }
                else {
                    action = new ReduceAction(this, parser.getRule(Integer.parseInt(number.toString())));
                }
                break;
            case 'a': action = new AcceptAction(this, parser.getLRA()); break;
        }
        return action;
    }

    public LRState<T> getStateWithAction(String input, String action, int index) {
        return getStateWithAction(input, action, index, 1);
    }
    public LRState<T> getStateWithAction(String input, String action, int index, int size) {
        ParserAction<T> cellAction;
        ArrayList<ParserAction<T>> cellActions;
        for (Map.Entry<LRState<T>, HashMap<String, ParserActionList<T>>> row : actionGrid.entrySet()) {
            try {
                cellActions = row.getValue().get(input);
                if (cellActions.size() >= size) {
                    cellAction = cellActions.get(index);
                    if (cellAction.toString().equals(action)) {
                        return row.getKey();
                    }
                }
            }
            catch (IndexOutOfBoundsException e) {

            }
        }
        return null;
    }
    public HashSet<ParserAction<T>> getActionsForInput(String input) {
        HashSet<ParserAction<T>> actions = new HashSet<>();
        for (Map.Entry<LRState<T>, HashMap<String, ParserActionList<T>>> row : actionGrid.entrySet()) {
            actions.addAll(row.getValue().get(input));
        }
        return actions;
    }

    public void copyAction(int sourceState, String sourceInput, int i, int destState, String destInput) {
        moveAction(sourceState, sourceInput, i, destState, destInput, false);
    }
    public void moveAction(int sourceState, String sourceInput, int i, int destState, String destInput) {
        moveAction(sourceState, sourceInput, i, destState, destInput, true);
    }
    public void moveAction(int sourceState, String sourceInput, int i, int destState, String destInput, boolean remove) {
        System.out.println(getActions(sourceState, sourceInput));
        ParserAction<T> action = getActions(sourceState, sourceInput).get(i);
        getActions(destState, destInput).add(constructAction(destInput, action.toString()));
        if (remove) {
            getActions(sourceState, sourceInput).remove(i);
        }
    }
}
