package com.N2_Parser;

public class TestParser extends SimpleParser {
    public TestParser(int language) {
        switch (language) {
            case 0:
                addRule("Statement'", "Statement", "EndOfFile");
                addRule("Statement", "BracketLeft", "List", "BracketRight");
                addRule("Statement", "Identifier");
                addRule("List", "Statement");
                addRule("List", "List", "Statement");
                break;
            case 1:
                addRule("Statement", "Expression", "EndOfFile");
                addRule("Expression", "Expression", "Plus", "Term");
                addRule("Expression", "Term");
                addRule("Term", "Identifier");
                break;
        }
        createLRA();
        getActionGrid().printGrid();
    }
}