package com.N2_Parser.Lookahead;

import com.Methods.stringMethods;
import com.N2_Parser.Grammar.Grammar;
import com.N2_Parser.Grammar.Item;

import java.util.Objects;

public class LookaheadGrammar extends Item<LookaheadGrammar> {
    private final String lookahead;

    public LookaheadGrammar(Grammar<LookaheadGrammar> rule, int pointer, String lookahead) {
        this.rule = rule;
        this.pointer = pointer;
        this.lookahead = lookahead;
    }

    public String getLookahead() {
        return lookahead;
    }

    public LookaheadGrammar shift(int p) {
        return new LookaheadGrammar(rule, p, lookahead);
    }
    public LookaheadGrammar shift() {
        return new LookaheadGrammar(rule, Math.min(pointer + 1, rule.getDefinition().length - 1), lookahead);
    }

    public boolean equals(Object o) {
        try {
            if (o instanceof LookaheadGrammar) {
                LookaheadGrammar l = (LookaheadGrammar) o;
                if (rule == null) {
                    return l.getGrammar() == null && l.getLookahead() == null;
                } else {
                    return Objects.equals(rule, l.getGrammar()) && pointer == l.getPointerPosition() && Objects.equals(lookahead, l.getLookahead());
                }
            }
        }
        catch (NullPointerException e) {

        }
        return false;
    }

    public int hashCode() {
        return rule.hashCode() * pointer * (lookahead != null ? lookahead.hashCode() : 1);
    }

    public String toString() {
        try {
            StringBuilder sb = new StringBuilder().append(rule.getName()).append(" ->");
            String[] def = rule.getDefinition();
            for (int i = 0; i < def.length; i += 1) {
                if (i == pointer) {
                    sb.append(" .");
                }
                sb.append(" ").append(def[i]);
            }
            return stringMethods.encapsulatedTuple("[", "]", " | ", sb.toString(), lookahead);
        }
        catch (NullPointerException e) {
            return null;
        }
    }
}
