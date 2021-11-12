package com.N2_Parser;

import com.Automata.LRA;
import com.N1_Lexer.ContextToken;
import com.N1_Lexer.Lexer;
import com.N2_Parser.Actions.ParserActionGrid;
import com.N2_Parser.Grammar.Grammar;
import com.N2_Parser.Grammar.Item;
import com.N3_SemanticActions.SyntaxTree;
import com.Classes.Pair;
import com.Methods.stringMethods;
import com.N3_SemanticActions.Tokens.Token;

import java.util.*;

public abstract class Parser<T extends Item> {
    protected ParserActionGrid<T> grid;
    protected ArrayList<Grammar<T>> rules = new ArrayList<>();
    protected LRA<T> automaton;
    private HashMap<String, String> aliases = new HashMap<>();
    private ArrayList<ContextToken> input;
    private Stack<ContextToken> stack;
    public boolean parse(String input, boolean silent) {
        return false;
    }

    public LRA<T> getLRA() {
        return automaton;
    }

    public ArrayList<ContextToken> getInput() {
        return input;
    }
    public Stack<ContextToken> getStack() {
        return stack;
    }

    public Pair<String, String[]> removeAliases(boolean silent, String name, String ... definition) {
        stringMethods.silentPrint("(" + name, silent);
        if (aliases.get(name) != null) {
            name = aliases.get(name);
            stringMethods.silentPrint(" / " + name, silent);
        }
        stringMethods.silentPrint(", " + Arrays.asList(definition), silent);
        boolean altered = false;
        for (int i = 0; i < definition.length; i += 1) {
            if (aliases.get(definition[i]) != null) {
                definition[i] = aliases.get(definition[i]);
                altered = true;
            }
        }
        if (altered) {
            stringMethods.silentPrint(" / " + Arrays.asList(definition), silent);
        }
        stringMethods.silentPrintLine(")", silent);
        return new Pair<>(name, definition);
    }
    public void addRule(String name, String ... definition) {
        addRule(true, name, definition);
    }
    public void addRule(boolean silent, String name, String ... definition) {
        Pair<String, String[]> trueRule = removeAliases(silent, name, definition);
        rules.add(new Grammar<>(this, trueRule.getKey(), trueRule.getValue()));
    }
    public ArrayList<Grammar<T>> getRulesFromName(String token) {
        ArrayList<Grammar<T>> r = new ArrayList<>();
        for (Grammar<T> g : rules) {
            if (g.getName().equals(token)) {
                r.add(g);
            }
        }
        return r;
    }
    public ArrayList<Grammar<T>> getRulesFromDefinition(String token) {
        ArrayList<Grammar<T>> r = new ArrayList<>();
        boolean c = false;
        for (Grammar<T> g : rules) {
            for (String t : g.getDefinition()) {
                if (t.equals(token)) {
                    c = true;
                    break;
                }
            }
            if (c) {
                r.add(g);
                c = false;
            }
        }
        return r;
    }
    public Grammar<T> getRule(int id) throws IndexOutOfBoundsException {
        return rules.get(id);
    }
    public Grammar<T> getRule(String name, String ... tokens) {
        return getRule(true, name, tokens);
    }
    public Grammar<T> getRule(boolean silent, String name, String ... tokens) {
        Pair<String, String[]> trueRule = removeAliases(silent, name, tokens);
        name = trueRule.getKey();
        tokens = trueRule.getValue();
        boolean c = true;
        String[] def;
        for (Grammar<T> g : rules) {
            stringMethods.silentPrint(g + "...", silent);
            if (name.equals(g.getName())) {
                stringMethods.silentPrint(" FOUND.", silent);
                try {
                    def = g.getDefinition();
                    if (def.length == tokens.length) {
                        for (int i = 0; i < tokens.length; i += 1) {
                            if (!tokens[i].equals(def[i])) {
                                c = false;
                                stringMethods.silentPrint(" " + i, silent);
                            }
                        }
                        stringMethods.silentPrint(" " + c, silent);
                        if (c) {
                            stringMethods.silentPrintLine(silent);
                            return g;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    stringMethods.silentPrint(" [ERR]: " + e.getMessage(), silent);
                }
                c = true;
            }
            stringMethods.silentPrintLine(silent);
        }
        return null;
    }

    public boolean isTerminal(String token) {
        return (getRulesFromName(token).size() == 0);
    }

    public String printCheck(String type) {
        return stringMethods.tuple(type, stringMethods.tuple(getRulesFromName(type)));
    }

    public void createLRA() {
        createLRA(true);
    }
    public abstract void createLRA(boolean silent);

    public ArrayList<Grammar<T>> getRules() {
        return rules;
    }

    public HashSet<String> getNames() {
        HashSet<String> names = new HashSet<>();
        for (Grammar<T> g : rules) {
            names.add(g.getName());
        }
        return names;
    }

    public void addAliases(String ... aliases) {
        for (int i = 0; i < aliases.length / 2; i += 1) {
            this.aliases.put(aliases[i * 2 + 1], aliases[i * 2]);
        }
    }
    public HashMap<String, String> getAliases() {
        return aliases;
    }

    public String getAliasForName(String name) {
        for (Map.Entry<String, String> alias : aliases.entrySet()) {
            if (alias.getValue().equals(name)) {
                return alias.getKey();
            }
        }
        return name;
    }

    public String getNameForAlias(String lookahead) {
        return aliases.get(lookahead);
    }

    public ParserActionGrid<T> getActionGrid() {
        return grid;
    }

    public Token parse(Lexer.type ... input) {
        ArrayList<ContextToken> list = new ArrayList<>();
        for (Lexer.type token : input) {
            list.add(new ContextToken(token));
        }
        return parse(list);
    }
    public Token parse(ArrayList<ContextToken> input) {
        for (int i = 0; i < input.size(); i += 1) {
            if (input.get(i).getType() == Lexer.type.WHITESPACE) {
                input.remove(i);
            }
        }
        return parse(input, new Stack<>());
    }

    public Token parse(ArrayList<ContextToken> input, Stack<ContextToken> stack) {
        System.out.println(input);
        this.input = input;
        this.stack = stack;
        automaton.reset();
        automaton.traverse();
        System.out.println();
        return automaton.createParseTree();
    }

    public void copyAliases(Parser<?> parser) {
        aliases = parser.getAliases();
    }
}
