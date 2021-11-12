package com.N2_Parser.Grammar;

public class GrammarSimilarity<T extends Item> implements Comparable<GrammarSimilarity<T>> {
    private final Grammar<T> rule;
    private final double similarity;
    public GrammarSimilarity(Grammar<T> rule, double similarity) {
        this.rule = rule;
        this.similarity = similarity;
    }

    public Grammar<T> getRule() {
        return rule;
    }

    @Override
    public int compareTo(GrammarSimilarity o) {
        return (int) (100 * (similarity - o.similarity));
    }

    @Override
    public String toString() {
        return "{ " + rule + " | similarity : " + similarity + " }";
    }
}
