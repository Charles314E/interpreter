package com;

import com.GUI.GUI;
import com.Methods.stringMethods;
import com.N1_Lexer.ArtLexer;
import com.N1_Lexer.ContextToken;
import com.N1_Lexer.Lexer;
import com.N1_Lexer.TestLexer;
import com.N2_Parser.*;
import com.N2_Parser.Actions.ReduceAction;
import com.N2_Parser.Grammar.*;
import com.N2_Parser.Lookahead.LookaheadTestParser;
import com.N3_SemanticActions.SyntaxTree;
import com.N3_SemanticActions.Tokens.Value;
import com.N3_SemanticActions.Tokens.Values.BooleanLiteral;
import com.N3_SemanticActions.Tokens.Values.FloatLiteral;
import com.N3_SemanticActions.Tokens.Values.IntegerLiteral;
import com.N3_SemanticActions.Visitor;
import com.N4_SymbolTables.MethodTable;
import com.N4_SymbolTables.VariableTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class debug {
    public static void lexerNFATest() {
        ArtLexer lexer = new ArtLexer();
        System.out.println();
        lexer.getNFA(Lexer.type.INTEGER).printStates(false);
        System.out.println();
        lexer.getNFA(Lexer.type.NOT).printStates(false);
        System.out.println();
        lexer.getNFA(Lexer.type.LEFT_BRACKET).printStates(false);
    }
    public static void printLexerTest() {
        ArtLexer lexer = new ArtLexer();
        System.out.println(stringMethods.tuple(
                lexer.printCheck(Lexer.type.NEW_LINE, "@n")));
        System.out.println(stringMethods.tuple(
                lexer.printCheck(Lexer.type.TO, "."),
                lexer.printCheck(Lexer.type.TO, ".."),
                lexer.printCheck(Lexer.type.TO, ". .."),
                lexer.printCheck(Lexer.type.TO, "...")));
        System.out.println(stringMethods.tuple(
                lexer.printCheck(Lexer.type.WHITESPACE, "b"),
                lexer.printCheck(Lexer.type.WHITESPACE, ""),
                lexer.printCheck(Lexer.type.WHITESPACE, " "),
                lexer.printCheck(Lexer.type.WHITESPACE, "b "),
                lexer.printCheck(Lexer.type.WHITESPACE, "  "),
                lexer.printCheck(Lexer.type.WHITESPACE, " b"),
                lexer.printCheck(Lexer.type.WHITESPACE, " b ")));
        System.out.println(stringMethods.tuple(
                lexer.printCheck(Lexer.type.IDENTIFIER, "bc"),
                lexer.printCheck(Lexer.type.IDENTIFIER, "camel_case"),
                lexer.printCheck(Lexer.type.IDENTIFIER, "c4m3lC453"),
                lexer.printCheck(Lexer.type.IDENTIFIER, "1x"),
                lexer.printCheck(Lexer.type.IDENTIFIER, "1"),
                lexer.printCheck(Lexer.type.IDENTIFIER, "id0"),
                lexer.printCheck(Lexer.type.IDENTIFIER, "0df1")));
        System.out.println(stringMethods.tuple(
                lexer.printCheck(Lexer.type.INTEGER, "bc"),
                lexer.printCheck(Lexer.type.INTEGER, "a1"),
                lexer.printCheck(Lexer.type.INTEGER, "1"),
                lexer.printCheck(Lexer.type.INTEGER, "12"),
                lexer.printCheck(Lexer.type.INTEGER, "13.0"),
                lexer.printCheck(Lexer.type.INTEGER, "-1")));
        System.out.println(stringMethods.tuple(
                lexer.printCheck(Lexer.type.REAL, "bc"),
                lexer.printCheck(Lexer.type.REAL, "256.057"),
                lexer.printCheck(Lexer.type.REAL, "2"),
                lexer.printCheck(Lexer.type.REAL, "-0.2"),
                lexer.printCheck(Lexer.type.REAL, "."),
                lexer.printCheck(Lexer.type.REAL, ""),
                lexer.printCheck(Lexer.type.REAL, "0.")));
        System.out.println(stringMethods.tuple(
                lexer.printCheck(Lexer.type.BOOLEAN, "bc"),
                lexer.printCheck(Lexer.type.BOOLEAN, "true"),
                lexer.printCheck(Lexer.type.BOOLEAN, "tru"),
                lexer.printCheck(Lexer.type.BOOLEAN, "ttrue"),
                lexer.printCheck(Lexer.type.BOOLEAN, "false"),
                lexer.printCheck(Lexer.type.BOOLEAN, "fal"),
                lexer.printCheck(Lexer.type.BOOLEAN, "falsee")));
        System.out.println(stringMethods.tuple(
                lexer.printCheck(Lexer.type.STRING, "bc"),
                lexer.printCheck(Lexer.type.STRING, "'Dark'"),
                lexer.printCheck(Lexer.type.STRING, "'Hello'"),
                lexer.printCheck(Lexer.type.STRING, "'Hello, World?'"),
                lexer.printCheck(Lexer.type.STRING, "'Hello World. It's a lovely day outside; I think that [(4 + 4) - 6 == 2 && {3, 5} * {5, 6} == {3, 5, 6}]'"),
                lexer.printCheck(Lexer.type.STRING, "''"),
                lexer.printCheck(Lexer.type.STRING, "falsee")));
    }
    
    public static void lexerPrecedenceTest() {
        ArtLexer lexer = new ArtLexer();
        System.out.println(stringMethods.tuple(lexer.analyse("definition", true), lexer.analyse("def initiom", true)));
    }

    public static void equalPointedGrammarTest() {
        Grammar<PointedGrammar> g = new Grammar<>(null, "S'", "S", "EndOfFile");
        PointedGrammar[] g1 = { new PointedGrammar(g, 0), new PointedGrammar(g, 1) };
        PointedGrammar[] g2 = { new PointedGrammar(g, 0), new PointedGrammar(null, 0) };
        PointedGrammar[] g3 = { new PointedGrammar(g, 0), new PointedGrammar(g, 1) };
        System.out.println(stringMethods.tuple(g1[0], g2[1]) + " " + (g1[0]).equals(g2[1]));
        System.out.println(stringMethods.tuple(g1[0], g1[1]) + " " + (g1[0]).equals(g1[1]));
        System.out.println(stringMethods.tuple(g1[0], g2[0]) + " " + (g1[0]).equals(g2[0]));
        ItemList<PointedGrammar> h1 = new ItemList<>(PointedGrammar.class);
        h1.addAll(Arrays.asList(g1));
        System.out.println(stringMethods.tuple(h1, Arrays.asList(g3)) + " " + h1.contains(g2[0]));
    }

    public static void closureTest() {
        TestParser parser = new TestParser(0);
        ItemList<PointedGrammar> list = new ItemList<>(PointedGrammar.class);
        //System.out.println(parser.getRule("Statement", "BracketLeft", "List", "BracketRight"));
        list.add(new PointedGrammar(parser.getRule("Statement", "BracketLeft", "List", "BracketRight"), 1));
        System.out.println(parser.getLRA().closure(list));
    }

    public static void reduceTest() {
        String[][][] tests = {
                {{"Statement", "EndOfFile"}, {"Statement"}},
                {{"BracketLeft", "List", "BracketRight"}, {"BracketLeft", "Identifier"}},
                {{"Identifier"}, {"List", "BracketLeft"}},
                {{"Statement"}, {"Plus", "Statement"}},
                {{"List", "Statement"}, {"List", "Plus"}}
        };
        TestParser parser = new TestParser(0);
        ArrayList<Grammar<PointedGrammar>> grammars = parser.getRules();
        Stack<ContextToken> stack;
        ReduceAction<PointedGrammar> reduce;
        for (int i = 0; i < grammars.size(); i += 1) {
            for (String[] item : tests[i]) {
                stack = new Stack<>();
                for (String token : item) {
                    stack.push(new ContextToken(Lexer.type.getIdentifier(token)));
                }
                reduce = new ReduceAction<>(null, grammars.get(i));
                System.out.println(stringMethods.tuple(Arrays.asList(item), reduce.action(null, stack), grammars.get(i), stack));
            }
            System.out.println();
        }
    }

    
    public static void testLexerTest() {
        TestLexer lexer = new TestLexer();
        String[] strings = {"x, y, x + 1, (x, y)"};
        System.out.println(lexer.analyse(strings[0].replace(" ", ""), false));
    }

    
    public static void firstParseTest() {
        TestParser p1 = new TestParser(0);
        System.out.println();
        p1.parse(Lexer.type.LEFT_BRACKET, Lexer.type.IDENTIFIER, Lexer.type.LEFT_BRACKET, Lexer.type.IDENTIFIER, Lexer.type.RIGHT_BRACKET, Lexer.type.IDENTIFIER, Lexer.type.RIGHT_BRACKET, Lexer.type.END_OF_FILE);
    }

    
    public static void secondParseTest() {
        TestParser p2 = new TestParser(1);
        System.out.println();
        p2.parse(Lexer.type.IDENTIFIER, Lexer.type.PLUS, Lexer.type.IDENTIFIER, Lexer.type.PLUS, Lexer.type.IDENTIFIER, Lexer.type.END_OF_FILE);
    }

    
    public static void artLexerTest() {
        ArtLexer lexer = new ArtLexer();
        String[] strings = {"x = 0; y = x + 1; print(x, y);"};
        System.out.println(lexer.analyse(strings[0], false));
    }
    
    public static void artTableTest() {
        long startTime = System.currentTimeMillis();
        new ArtParser();
        long endTime = System.currentTimeMillis();
        System.out.println("Time Taken : " + ((endTime - startTime) / 1000.0) + " seconds");
    }
    
    public static void artConstructionTest() {
        new ArtParser(false, true);
    }

    
    public static void operatorTableTest() {
        long startTime = System.currentTimeMillis();
        new OperatorTestParser();
        long endTime = System.currentTimeMillis();
        System.out.println("Time Taken : " + ((endTime - startTime) / 1000.0) + " seconds");
    }

    
    public static void artParseTest(int level) {
        ArtLexer lexer = new ArtLexer();
        String[] strings = {
                "var y = 1;", //1
                "item[5 * 3 + 1] = 1;", //2
                "var a = 20; var b = 10; var c = 15; var d = 5; var e = [(a + b) * c / d, a + (b * c) / d, a * b + c / d]; return e;", //3
                "var x = 'lord of death'; return x;", //4
                "var x = 1; var y = x + 1 - 2; var z = sum(x, y);", //5
                "var x = 30; var y = x * 5 / power(2.235, 2); return y;", //6
                "def sum(var x, var y, var z) { return x + y * z; }; var x = 5 * power(3, 3); return sum(x, 2);", //7
                "def factorial(var n) { var sum = sum; for (var i = 1...n) { var sum = sum * i; }; return sum; }; return factorial(100);", //8
                "if (50 == 50) { return true; };" //9
        };
        new ArtParser(true, false).parse(lexer.analyse(strings[level - 1], true));
    }

    public static void operatorParseTest() {
        ArtLexer lexer = new ArtLexer();
        String[] strings = {"x + 1"};
        for (String input : strings) {
            new OperatorTestParser(true).parse(lexer.analyse(input, true));
        }
    }

    public static void valueTest() {
        Visitor.Interpreter v = new Visitor.Interpreter(new VariableTable(), new MethodTable(), false);
        Value[] values = { new IntegerLiteral(2), new FloatLiteral(4.65), new BooleanLiteral(false), new BooleanLiteral(true) };
        for (Value value : values) {
            System.out.println(stringMethods.tuple(value.getClass(), value.accept(v), value.getValue()));
        }
    }

    public static void lookaheadTest() {
        LookaheadTestParser parser = new LookaheadTestParser(false, false);
        System.out.println(parser.getLookaheadForGrammar(parser.getRule("V", "*", "E")));
    }

    public static void fileParseTest() {
        System.out.print("Please input a file to interpret. | >>_: ");
        Scanner file = new Scanner(System.in);
        SyntaxTree tree = fileParseTest(file.nextLine());
        try {
            System.out.println(tree.execute());
        }
        catch (NullPointerException e) {

        }
    }
    public static SyntaxTree fileParseTest(String filename) {
        File file = new File("code/" + filename);
        try {
            Scanner fileReader = new Scanner(file);
            StringBuilder code = new StringBuilder();
            while (fileReader.hasNextLine()) {
                code.append(fileReader.nextLine());
                code.append("@n");
            }
            ArtLexer lexer = new ArtLexer();
            SyntaxTree tree = new SyntaxTree(new ArtParser(false, false).parse(lexer.analyse(code.toString(), true)));
            System.out.println();
            System.out.println("AST:");
            System.out.println(tree);
            System.out.println();
            System.out.println("RESULT: " + tree.execute());
            return tree;
        }
        catch (FileNotFoundException e) {
            System.out.println("[ERR]: The file '" + file.getAbsolutePath() + "' cannot be found.");
            return null;
        }
    }

    public static void interfaceTest() {
        System.out.println(System.getProperty("java.library.path"));
        GUI gui = new GUI(1024, 768);
    }
}
