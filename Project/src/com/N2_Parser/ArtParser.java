package com.N2_Parser;

import com.Data.exporting;
import com.Data.importing;

public class ArtParser extends SimpleParser {
    public ArtParser() {
        this(true, false);
    }
    public ArtParser(boolean silent) {
        this(silent, false);
    }
    public ArtParser(boolean silent, boolean slow) {
        addAliases(
                "EndOfFile", "$",
                "Comma", ",",
                "Dot", ".",
                "Semicolon", ";",
                "Colon", ":",
                "Assign", "=",
                "BracketLeft", "(",
                "BracketRight", ")",
                "SquareBracketLeft", "[",
                "SquareBracketRight", "]",
                "CurlyBracketLeft", "{",
                "CurlyBracketRight", "}",
                "EnclosedPhrase", "{St;...St;}",
                "Plus", "+",
                "Minus", "-",
                "Multiply", "*",
                "Divide", "/",
                "Power", "^",
                "Modulus", "%",
                "PlusOperator", "+Op",
                "MinusOperator", "-Op",
                "MultiplyOperator", "*Op",
                "DivisionOperator", "/Op",
                "ExponentOperator", "^Op",
                "ModulusOperator", "%Op",
                "Term", "T",
                "Factor", "F",
                "ASOperator", "3Op",
                "MOperator", "4Op",
                "Method", "f(x)",
                "AssignmentStatement", "=St",
                "ConditionalTree", "If?Elif?Else",
                "LoopStatement", "Loop",
                "Variable", "var",
                "String", "'Hello!'",
                "Boolean", "T/F",
                "Number", "num",
                "Integer", "420",
                "Real", "69.6969",
                "IfStatement", "If?",
                "ElseIfStatement", "Elif?",
                "ElseStatement", "Else?",
                "Conditional", "If?Else",
                "If", "if",
                "Else", "else",
                "While", "while",
                "For", "for",
                "WhileLoop", "While...St",
                "ForLoop", "For...St",
                "Expression", "x+y",
                "Equation", "x==y",
                "Phrase", "St;...St;",
                "Name", "Name",
                "List", "a,b,c",
                "Array", "[x,y,z]",
                "Identifier", "x",
                "Attribute", "x.y",
                "Item", "x[n]",
                "Value", "val",
                "Assignee", "?=x",
                "Argument", "arg",
                "Arguments", "arg...arg",
                "To", "...",
                "Equals", "==",
                "NotEquals", "!=",
                "LessThan", "<",
                "GreaterThan", ">",
                "LessThanOrEquals", "<=",
                "GreaterThanOrEquals", ">=",
                "EqualsStatement", "x==y",
                "NotEqualsStatement", "x!=y",
                "LessThanStatement", "x<y",
                "GreaterThanStatement", "x>y",
                "LessThanOrEqualsStatement", "x<=y",
                "GreaterThanOrEqualsStatement", "x>=y"
        );

        addRule("Phrase", "Phrase", "$");
        addRule("Phrase", "Phrase", "Phrase");
        addRule("Phrase", "Statement", ";");

        addRule("Statement", "MethodCall");
        addRule("Statement", "AssignmentStatement");
        addRule("Statement", "ConditionalTree");
        addRule("Statement", "LoopStatement");
        addRule("Statement", "MethodDefinition");
        addRule("Statement", "ReturnStatement");

        addRule("IfHeader", "If", "(", "Equation", ")");
        addRule("IfStatement", "IfHeader", "EnclosedPhrase");
        addRule("ElseIfHeader", "Else", "If", "(", "Equation", ")");
        addRule("ElseIfStatement", "ElseIfHeader", "EnclosedPhrase");
        addRule("ElseStatement", "Else", "EnclosedPhrase");
        addRule("Conditional", "ElseIfStatement");
        addRule("Conditional", "Conditional", "Conditional");
        addRule("ConditionalTree", "IfStatement", "Conditional");
        addRule("ConditionalTree", "ConditionalTree", "ElseStatement");
        addRule("ConditionalTree", "IfStatement", "ElseStatement");
        addRule("ConditionalTree", "IfStatement");

        addRule("WhileHeader", "While", "(", "Equation", ")");
        addRule("WhileLoop", "WhileHeader", "EnclosedPhrase");

        addRule("ForHeader", "For", "(", "Var", "Identifier", "=", "Iterator", ")");
        addRule("ForLoop", "ForHeader", "EnclosedPhrase");

        addRule("Iterator", "Expression", "...", "Expression");
        addRule("Iterator", "Array");

        addRule("LoopStatement", "WhileLoop");
        addRule("LoopStatement", "ForLoop");

        addRule("EnclosedPhrase", "{", "Phrase", "}");

        addRule("Argument", "Var",  "Identifier");
        addRule("ReturnStatement", "Return", "Expression");
        addRule("MethodHeader", "Def", "Identifier", "(", "Arguments", ")");
        addRule("MethodDefinition", "MethodHeader", "EnclosedPhrase");
        addRule("Arguments", "Arguments", ",", "Arguments");
        addRule("Arguments", "Argument");
        addRule("MethodCall", "Name", "(", "List", ")");
        addRule("List", "List" , ",", "List");
        addRule("List", "Expression");
        addRule("Array", "[", "List", "]");
        addRule("Attribute", "Identifier", ".", "Identifier");
        addRule("Item", "Name", "[", "Expression", "]");

        addRule("Assignee", "Expression");
        addRule("AssignmentStatement", "Var", "Variable", "=", "Assignee");

        addRule("Equation", "EqualsStatement");
        addRule("EqualsStatement", "Expression", "==", "Expression");

        addRule("Equation", "NotEqualsStatement");
        addRule("NotEqualsStatement", "Expression", "!=", "Expression");

        addRule("Equation", "LessThanStatement");
        addRule("LessThanStatement", "Expression", "<", "Expression");

        addRule("Equation", "GreaterThanStatement");
        addRule("GreaterThanStatement", "Expression", ">", "Expression");

        addRule("Equation", "LessThanOrEqualsStatement");
        addRule("LessThanOrEqualsStatement", "Expression", "<=", "Expression");

        addRule("Equation", "GreaterThanOrEqualsStatement");
        addRule("GreaterThanOrEqualsStatement", "Expression", ">=", "Expression");

        addRule("Name", "Identifier");
        addRule("Name", "Attribute");
        addRule("Variable", "Name");
        addRule("Variable", "Item");
        addRule("Expression", "MethodCall");
        addRule("Expression", "Variable");
        addRule("Expression", "Integer");
        addRule("Expression", "Real");
        addRule("Expression", "Boolean");
        addRule("Expression", "String");
        addRule("Expression", "Null");
        addRule("Expression", "Operator");
        addRule("Expression", "Equation");
        addRule("Expression", "Array");
        addRule("Expression", "(", "Expression", ")");

        addRule("Operator", "PlusOperator");
        addRule("PlusOperator", "Expression", "+", "Expression");

        addRule("Operator", "MinusOperator");
        addRule("MinusOperator", "Expression", "-", "Expression");

        addRule("Operator", "MultiplyOperator");
        addRule("MultiplyOperator", "Expression", "*", "Expression");

        addRule("Operator", "DivisionOperator");
        addRule("DivisionOperator", "Expression", "/", "Expression");

        addRule("Operator", "ExponentOperator");
        addRule("ExponentOperator", "Power", "(", "Expression", ",", "Expression", ")");

        addRule("Operator", "ModulusOperator");
        addRule("ModulusOperator", "Expression", "%", "Expression");

        boolean goodGrid;
        createLRA(silent);
        if (importing.fileExists("parsing_table.txt")) {
            addExtraRules();
            goodGrid = importing.importTable(getActionGrid(), "parsing_table.txt");
        }
        else {
            populateLRA();
            addExtraRules();
            ArtParserFixer fixer = new ArtParserFixer(this, silent);
            fixer.updateLRA(silent);
            goodGrid = exporting.exportTable(getActionGrid(), "parsing_table.txt");
        }
        if (goodGrid) {
            getActionGrid().printGrid();
        }
    }

    public void addExtraRules() {
        addRule("Statement", "Assignee");
        addRule("MethodCall", "Name", "(", "Expression", ")");
        addRule("List", "List", ",", "Expression");
        addRule("ReturnStatement", "Return", "Assignee");
    }
}