package com.N2_Parser.Actions;

import com.N2_Parser.Grammar.Item;

import java.util.ArrayList;

public class ParserActionList<T extends Item> extends ArrayList<ParserAction<T>> {
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size(); i++) {
                if (get(i) == null) {
                    return i;
                }
            }
        } else {
            if (o instanceof GotoAction) {
                for (int i = 0; i < size(); i++) {
                    if (get(i) instanceof GotoAction) {
                        if (((GotoAction<T>) o).equals((GotoAction<T>) get(i))) {
                            return i;
                        }
                    }
                }
            }
            else if (o instanceof ShiftAction) {
                for (int i = 0; i < size(); i++) {
                    if (get(i) instanceof ShiftAction) {
                        if (((ShiftAction<T>) o).equals((ShiftAction<T>) get(i))) {
                            return i;
                        }
                    }
                }
            }
            else if (o instanceof ReduceAction) {
                for (int i = 0; i < size(); i++) {
                    if (get(i) instanceof ReduceAction) {
                        if (((ReduceAction) o).equals((ReduceAction) get(i))) {
                            return i;
                        }
                    }
                }
            }
            return -1;
        }
        return -1;
    }
}
