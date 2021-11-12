package com.N2_Parser.Grammar;

import java.util.ArrayList;
import java.util.Collection;

public class ItemList<T extends Item> extends ArrayList<T> {
    Class<T> clazz;
    public ItemList(Class<T> clazz) {
        this.clazz = clazz;
    }
    public ItemList(Class<T> clazz, Collection<T> items) {
        this(clazz);
        addAll(items);
    }
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size(); i++) {
                if (get(i) == null) {
                    return i;
                }
            }
        } else {
            try {
                for (int i = 0; i < size(); i++) {
                    if (clazz.cast(o).equals(get(i))) {
                        return i;
                    }
                }
            }
            catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
    public boolean equals(ItemList<T> o) {
        if (o != null) {
            try {
                boolean c = true;
                for (int i = 0; i < size(); i += 1) {
                    if (!o.get(i).equals(get(i))) {
                        c = false;
                    }
                }
                return c;
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
        return false;
    }
}
