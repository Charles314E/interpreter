package com.Automata;

import com.N2_Parser.Grammar.Item;
import com.N2_Parser.Grammar.ItemList;
import com.Methods.stringMethods;

public class LRState<T extends Item> extends State<String> {
    private ItemList<T> items;
    public LRState(Class<T> clazz, LRA<T> automaton, ItemList<T> rules) {
        super(automaton);
        items = new ItemList<>(clazz);
        automaton.addState(this);
        addItems(rules);
    }
    public LRState(LRA<T> automaton) {
        super(automaton);
    }

    public void addItems(ItemList<T> rules) {
        this.items = rules;
    }
    public ItemList<T> getItems() {
        return items;
    }

    @Override
    public LRA<T> getAutomaton() {
        return (LRA<T>) super.getAutomaton();
    }

    public boolean equals(LRState<T> o) {
        if (items.equals(o.getItems())) {
            return true;
        }
        return false;
    }

    public String toString(boolean silent) {
        return super.toString(true) + stringMethods.tuple(items);
    }
    public String getSimpleString() {
        return super.toString(true);
    }
}
