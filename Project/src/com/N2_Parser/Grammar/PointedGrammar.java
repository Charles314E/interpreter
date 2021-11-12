package com.N2_Parser.Grammar;

public class PointedGrammar extends Item<PointedGrammar> {
    private final int id;
    public PointedGrammar(int rule, int pointer) {
        this.id = rule;
        this.pointer = pointer;
    }
    public PointedGrammar(Grammar<PointedGrammar> rule, int pointer) {
        this.id = rule.getParser().getRules().indexOf(rule);
        this.rule = rule;
        this.pointer = pointer;
    }

    public final void setGrammarRule(Grammar<PointedGrammar> rule) {
        this.rule = rule;
    }

    public int getID() {
        return id;
    }

    public PointedGrammar shift(int p) {
        return new PointedGrammar(rule, p);
    }
    public PointedGrammar shift() {
        return new PointedGrammar(rule, Math.min(pointer + 1, rule.getDefinition().length - 1));
    }

    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof PointedGrammar) {
                PointedGrammar p = (PointedGrammar) o;
                if (rule == null) {
                    return p.getGrammar() == null;
                } else {
                    return rule.equals(p.getGrammar()) && pointer == p.getPointerPosition();
                }
            }
        }
        return false;
    }

    public int hashCode() {
        return rule.hashCode() * pointer;
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
            return sb.toString();
        }
        catch (NullPointerException e) {
            return null;
        }
    }
}
