package com.N2_Parser.Grammar;

import com.N1_Lexer.ContextToken;
import com.N1_Lexer.Lexer;
import com.N2_Parser.Parser;

import java.util.Arrays;

public class Grammar<T extends Item> implements Comparable<Grammar<T>> {
    String name;
    String[] definition;
    private Parser<T> parser;

    public Grammar(Parser<T> parser, String name, String ... definition) {
        this.name = name;
        this.definition = definition;
        setParser(parser);
    }
    public Grammar(Parser<T> parser, String name, ContextToken ... definition) {
        this(parser, name, new String[definition.length]);
        for (int i = 0; i < definition.length; i += 1) {
            this.definition[i] = definition[i].getType().getClassName();
        }
    }
    public Grammar(Parser<T> parser, ContextToken ... definition) {
        this(parser, null, definition);
    }

    public void setParser(Parser<T> p) {
        parser = p;
    }

    public Parser<T> getParser() {
        return parser;
    }

    public String getName() {
        return name;
    }
    public String[] getDefinition() {
        return definition;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder().append(name).append(" ->");
        for (String def : definition) {
            sb.append(" ").append(def);
        }
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof Grammar) {
                Grammar<T> p = (Grammar<T>) o;
                return name.equals(p.getName()) && Arrays.equals(definition, p.getDefinition());
            }
        }
        return false;
    }
    public int hashCode() {
        int hash = name.hashCode();
        for (int i = 0; i < definition.length; i += 1) {
            hash += definition[i].hashCode() * i;
        }
        return hash;
    }

    @Override
    public int compareTo(Grammar o) {
        return o.definition.length - definition.length;
    }

    public double getSimilarity(Grammar<T> rule) {
        double similarity = 0;
        int fullLength = Math.max(definition.length, rule.definition.length);
        double damage = 1.0 / fullLength;
        int streak = 0;
        boolean found = false;
        for (int i = 0; i < fullLength; i += 1) {
            try {
                if (definition[i].equals(rule.definition[i])) {
                    streak += 1;
                    if (found) {
                        if (streak > 1) {
                            similarity += damage;
                        }
                        else {
                            similarity += damage / 2;
                        }
                    }
                    else {
                        similarity += damage;
                    }
                    found = true;
                }
                else {
                    streak = 0;
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                similarity -= damage;
            }
        }
        return Math.max(0, similarity);
    }
}
