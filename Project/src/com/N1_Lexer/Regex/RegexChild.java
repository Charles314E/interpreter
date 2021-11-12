package com.N1_Lexer.Regex;

import java.util.ArrayList;

public class RegexChild<T extends Character> extends Regex<T> {
    protected RegexBase<T> top;
    protected int start;
    protected int end = -1;

    public RegexChild() {}
    public RegexChild(ArrayList<RegexChild<T>> children) {
        this.children = (RegexChild<T>[]) children.toArray();
    }
    public Regex<T> getTop() {
        return top;
    }
    public boolean check(String input) {
        for (Regex<T> child : children) {
            if (!child.check(input)) {
                return false;
            }
        }
        return true;
    }
    public int getStart() {
        return start;
    }
    public void setEnd(int end) {
        this.end = end;
    }
    public int getEnd() {
        return end;
    }
    public RegexChild<T> getFirstChild() {
        return children[0];
    }
    public RegexChild<T> getLastChild() {
        return children[children.length - 1];
    }
    public void setLastChild(RegexChild<T> child) {
        children[children.length - 1] = child;
    }
}
