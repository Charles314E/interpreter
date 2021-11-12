package com;

import com.Methods.stringMethods;

public class main {

    public static void main(String[] args) {
        System.out.println(stringMethods.tuple(args));
        switch (args[0]) {
            case "ln": debug.lexerNFATest(); break;
            case "pl": debug.printLexerTest(); break;
            case "lp": debug.lexerPrecedenceTest(); break;
            case "eg": debug.equalPointedGrammarTest(); break;
            case "cl": debug.closureTest(); break;
            case "rd": debug.reduceTest(); break;
            case "al": debug.artLexerTest(); break;
            case "tl": debug.testLexerTest(); break;
            case "ac": debug.artConstructionTest(); break;
            case "at": debug.artTableTest(); break;
            case "ot": debug.operatorTableTest(); break;
            case "1p": debug.firstParseTest(); break;
            case "2p": debug.secondParseTest(); break;
            case "ap": debug.artParseTest(Integer.parseInt(args[1])); break;
            case "op": debug.operatorParseTest(); break;
            case "va": debug.valueTest(); break;
            case "lk": debug.lookaheadTest(); break;
            case "gui": debug.interfaceTest(); break;
            case "file": debug.fileParseTest(args[1]); break;
        }
    }
}
