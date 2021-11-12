package com.N1_Lexer.Regex;

public class RegexRepetition<T extends Character> extends RegexChild<T> {
    protected RegexChild<T> phrase;
    protected int min;
    protected int max;

    public RegexRepetition(RegexChild<T> phrase, int min, int max) {
        this.phrase = phrase;
        this.min = min;
        this.max = max;
    }
    public void setPhrase(RegexChild<T> phrase) {
        this.phrase = phrase;
    }
    public RegexChild<T> getPhrase() {
        return phrase;
    }
    public int getSmallestLoop() {
        return min;
    }
    public int getLargestLoop() {
        return max;
    }

    public String toString() {
        return "" + phrase + "*";
    }
}
