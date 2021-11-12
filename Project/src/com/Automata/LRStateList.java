package com.Automata;

import com.N2_Parser.Grammar.Item;

import java.util.ArrayList;

public class LRStateList<T extends Item> extends ArrayList<LRState<T>> {
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size(); i++) {
                if (get(i) == null) {
                    return i;
                }
            }
        } else {
            if (o instanceof LRState) {
                for (int i = 0; i < size(); i++) {
                    try {
                        if (((LRState<T>) o).equals(get(i))) {
                            return i;
                        }
                    }
                    catch (ClassCastException e) {

                    }
                }
            }
            return -1;
        }
        return -1;
    }
}
