package com.N2_Parser;

import com.N2_Parser.Actions.ParserAction;
import com.N2_Parser.Actions.ShiftAction;
import com.Methods.stringMethods;
import com.N2_Parser.Grammar.Item;

import java.util.Collections;
import java.util.HashMap;

public class ArtParserFixer<T extends Item> {
    ArtParser parser;
    HashMap<String, Integer> reductions = new HashMap<>();
    HashMap<String, Integer> states = new HashMap<>();
    boolean silent;

    ArtParserFixer(ArtParser parser, boolean silent) {
        this.parser = parser;
        this.silent = silent;
        reductions.put("Identifier -> Name",                getIndex("Name", "Identifier"));
        reductions.put("Name -> Variable",                  getIndex("Variable", "Name"));
        reductions.put("Item",                              getIndex("Item", "Name", "[", "Expression", "]"));
        reductions.put("Bracketed Expression",              getIndex("Expression", "(", "Expression", ")"));
        reductions.put("Method Call -> Expression",         getIndex("Expression", "MethodCall"));
        reductions.put("Variable -> Expression",            getIndex("Expression", "Variable"));
        reductions.put("Plus -> Operator",                  getIndex("PlusOperator", "Expression", "+", "Expression"));
        reductions.put("Equals -> Conditional",             getIndex("EqualsStatement", "Expression", "==", "Expression"));
        reductions.put("Operator -> Expression",            getIndex("Expression", "Operator"));
        reductions.put("Expression -> Assignee",            getIndex("Assignee", "Expression"));
        reductions.put("Assignment -> Statement",           getIndex("AssignmentStatement", "Var", "Variable", "=", "Assignee"));
        reductions.put("Assignee -> Statement",             getIndex("Statement", "Assignee"));
        reductions.put("Multiple Parameter Method Call",    getIndex("MethodCall", "Name", "(", "List", ")"));
        reductions.put("Single Parameter Method Call",      getIndex("MethodCall", "Name", "(", "Expression", ")"));
        reductions.put("Incomplete List",                   getIndex("List", "List", ",", "Expression"));
        reductions.put("Expression -> List",                getIndex("List", "Expression"));
        reductions.put("Argument",                          getIndex("Argument", "Var", "Identifier"));
        reductions.put("Return -> Statement",               getIndex("ReturnStatement", "Return", "Expression"));
        reductions.put("Assignee Return -> Statement",      getIndex("ReturnStatement", "Return", "Assignee"));
        reductions.put("Enclosed Phrase",                   getIndex("EnclosedPhrase", "{", "Phrase", "}"));
        reductions.put("If Statement -> Conditional Tree",  getIndex("ConditionalTree", "IfStatement"));
        reductions.put("Method Header",                     getIndex("MethodHeader", "Def", "Identifier", "(", "Arguments", ")"));
        reductions.put("Method Definition",                 getIndex("MethodDefinition", "MethodHeader", "EnclosedPhrase"));
        reductions.put("Method Call -> Statement",          getIndex("Statement", "MethodCall"));
        reductions.put("For Header",                        getIndex("ForHeader", "For", "(", "Var", "Identifier", "=", "Iterator", ")"));
        reductions.put("For Loop",                          getIndex("ForLoop", "ForHeader", "EnclosedPhrase"));
        reductions.put("While Loop",                        getIndex("WhileLoop", "WhileHeader", "EnclosedPhrase"));
        reductions.put("If Statement",                      getIndex("IfStatement", "IfHeader", "EnclosedPhrase"));
        reductions.put("Elif Statement",                    getIndex("ElseIfStatement", "ElseIfHeader", "EnclosedPhrase"));
        reductions.put("Else Statement",                    getIndex("ElseStatement", "Else", "EnclosedPhrase"));
        reductions.put("Conditional Tree",                  getIndex("ConditionalTree", "ConditionalTree", "ElseStatement"));
        reductions.put("Array",                             getIndex("Array", "[", "List", "]"));
        reductions.put("Array -> Expression",               getIndex("Expression", "Array"));
        reductions.put("Iterator",                          getIndex("Iterator", "Expression", "...", "Expression"));
        reductions.put("Array Iterator",                    getIndex("Iterator", "Array"));

        parser.getActionGrid().printGrid();

        states.put("phrase",                parser.getActionGrid().getActions(1, "Phrase").get(0).getState().getID());
        stringMethods.silentPrintLine(stringMethods.tuple("phrase", states.get("phrase")), silent);
        states.put("name",                  parser.getActionGrid().getStateWithAction("Name", "r" + reductions.get("Name -> Variable"), 1).getID());
        stringMethods.silentPrintLine(stringMethods.tuple("name", states.get("name")), silent);
        states.put("item",                  parser.getActionGrid().getStateWithAction("SquareBracketRight", "r" + reductions.get("Item"), 0).getID());
        states.put("preItem",               parser.getActionGrid().getActions(states.get("name"), "Name").get(0).getState().getID());
        stringMethods.silentPrintLine(stringMethods.tuple("item", states.get("item")), silent);
        states.put("arithmetic",            parser.getActionGrid().getActions(parser.getActionGrid().getStateWithAction("Expression", "r" + reductions.get("Expression -> List"), 1), "Expression").get(0).getState().getID());
        states.put("preArithmetic",         parser.getActionGrid().getStateWithAction("Expression", "g" + states.get("arithmetic"), 0).getID());
        stringMethods.silentPrintLine(stringMethods.tuple("arithmetic", states.get("arithmetic"), states.get("preArithmetic")), silent);
        states.put("array",                 parser.getActionGrid().getStateWithAction("SquareBracketRight", "r" + reductions.get("Array"), 0).getID());
        states.put("preArray",              parser.getActionGrid().getStateWithAction("List", "g" + states.get("array"), 0).getID());
        states.put("beforeArray",           parser.getActionGrid().getStateWithAction("SquareBracketLeft", "s" + states.get("preArray"), 0).getID());
        states.put("arrayElement",          parser.getActionGrid().getActions(states.get("array"), "Comma").get(0).getState().getID());
        states.put("arrayComma",            parser.getActionGrid().getActions(states.get("arrayElement"), "List").get(0).getState().getID());
        stringMethods.silentPrintLine(stringMethods.tuple("array", states.get("array"), states.get("preArray"), states.get("beforeArray"), states.get("arrayElement"), states.get("arrayComma")), silent);
        states.put("methodDefinition",      parser.getActionGrid().getStateWithAction("Identifier", "r" + reductions.get("Argument"), 0).getID());
        states.put("methodHeader",          parser.getActionGrid().getStateWithAction("BracketRight", "r" + reductions.get("Method Header"), 0).getID());
        states.put("methodArgument",        parser.getActionGrid().getActions(states.get("methodHeader"), "Comma").get(0).getState().getID());
        stringMethods.silentPrintLine(stringMethods.tuple("method definition", states.get("methodDefinition"), states.get("methodHeader"), states.get("methodArgument")), silent);
        states.put("argument",              parser.getActionGrid().getStateWithAction("Var", "s" + states.get("methodDefinition"), 0).getID());
        stringMethods.silentPrintLine(stringMethods.tuple("argument", states.get("argument")), silent);
        states.put("return",                parser.getActionGrid().getStateWithAction("Expression", "r" + reductions.get("Return -> Statement"), 1).getID());
        stringMethods.silentPrintLine(stringMethods.tuple("return", states.get("return")), silent);
        states.put("assignee",              parser.getActionGrid().getStateWithAction("Expression", "r" + reductions.get("Expression -> Assignee"), 0).getID());
        stringMethods.silentPrintLine(stringMethods.tuple("assignee", states.get("assignee")), silent);
        states.put("power",                 ((ParserAction<T>) parser.getActionGrid().getActionsForInput("Power").toArray()[0]).getState().getID());
        states.put("powerExpression",       parser.getActionGrid().getStateWithAction("BracketRight", "r" + (reductions.get("Plus -> Operator") + 8), 0).getID());
        stringMethods.silentPrintLine(stringMethods.tuple("power", states.get("power"), states.get("powerExpression")), silent);
        states.put("returnMethodCall",      parser.getActionGrid().getActions(states.get("return"), "BracketLeft").get(0).getState().getID());
        states.put("returnMethodArgument",  parser.getActionGrid().getActions(states.get("returnMethodCall"), "Expression").get(0).getState().getID());
        stringMethods.silentPrintLine(stringMethods.tuple("return method call", states.get("returnMethodCall"), states.get("returnMethodArgument")), silent);
        states.put("ifCondition",           parser.getActionGrid().getActions(parser.getActionGrid().getActions(states.get("phrase"), "If").get(0).getState(), "BracketLeft").get(0).getState().getID());
        states.put("ifStatement",           parser.getActionGrid().getActions(states.get("phrase"), "IfStatement").get(1).getState().getID());
        stringMethods.silentPrintLine(stringMethods.tuple("if statement", states.get("ifCondition"), states.get("ifStatement")), silent);
        states.put("whileCondition",        getLastStateInChain(states.get("phrase"), "While", "("));
        stringMethods.silentPrintLine(stringMethods.tuple("while statement", states.get("whileCondition")), silent);
        states.put("iterator",              parser.getActionGrid().getStateWithAction("Expression", "r" + reductions.get("Iterator"), 0).getID());
        stringMethods.silentPrintLine(stringMethods.tuple("iterator", states.get("iterator")), silent);
        states.put("enclosedPhrase",        ((ShiftAction<T>) parser.getActionGrid().getActionsForInput("CurlyBracketLeft").toArray()[0]).getState().getID());
        stringMethods.silentPrintLine(stringMethods.tuple("enclosed phrase", states.get("enclosedPhrase")), silent);
        states.put("comma",                 parser.getActionGrid().getStateWithAction("Identifier", "r" + reductions.get("Identifier -> Name"), 0).getID());

        stringMethods.silentPrintLine("name:" + reductions.get("Name -> Variable") + " list:" + reductions.get("Expression -> List") + " plus:" + reductions.get("Plus -> Operator") + " arg:" + reductions.get("Argument") + " enc:" + reductions.get("Enclosed Phrase") + " def:" + reductions.get("Method Definition") + " ret:" + reductions.get("Return -> Statement") + " con:" + reductions.get("Conditional Tree") + " " + parser.getRule(67), silent);
    }
    
    public int getIndex(String name, String ... tokens) {
        return parser.rules.indexOf(parser.getRule(name, tokens));
    }

    public int getLastStateInChain(int first, String ... tokens) {
        int state = first;
        for (String token : tokens) {
            state = parser.getActionGrid().getActions(state, token).get(0).getState().getID();
        }
        return state;
    }

    public void createBodyExpressions(int state) {
        parser.getActionGrid().addAction(state, "EnclosedPhrase", "r" + reductions.get("Method Definition") + "if;");
        parser.getActionGrid().addAction(state, "EnclosedPhrase", "r" + reductions.get("For Loop") + "if;");
        parser.getActionGrid().addAction(state, "EnclosedPhrase", "r" + reductions.get("While Loop") + "if;");
        parser.getActionGrid().addAction(state, "EnclosedPhrase", "r" + reductions.get("If Statement"));
        parser.getActionGrid().addAction(state, "EnclosedPhrase", "r" + reductions.get("Elif Statement"));
        parser.getActionGrid().addAction(state, "EnclosedPhrase", "r" + reductions.get("Else Statement"));
    }
    public void createOperatorTransitions(int state, int i) {
        parser.getActionGrid().addAction(state, "Expression", "r" + i + "if;");
        parser.getActionGrid().addAction(state, "Expression", "r" + i + "if)");
        parser.getActionGrid().addAction(state, "Expression", "r" + i + "if==");
        parser.getActionGrid().addAction(state, "Expression", "r" + i + "if!=");
        parser.getActionGrid().addAction(state, "Expression", "r" + i + "if<");
        parser.getActionGrid().addAction(state, "Expression", "r" + i + "if>");
        parser.getActionGrid().addAction(state, "Expression", "r" + i + "if<=");
        parser.getActionGrid().addAction(state, "Expression", "r" + i + "if>=");
        parser.getActionGrid().addAction(state, "Expression", "r" + i + "if,");
    }

    public void allowInterativeReduction(String outerState, String iteratorState, String element, String list) {
        int reduction = getIndex(list, element);
        parser.getActionGrid().addAction(states.get(outerState), "Comma", "g" + states.get(iteratorState));
        parser.getActionGrid().addAction(states.get(outerState), element, "r" + reduction);
        parser.getActionGrid().addAction(states.get(outerState), list, "r" + (reduction - 1));
    }

    public void updateLRA(boolean silent) {
        parser.getActionGrid().clearAction("g" + states.get("preItem"));
        parser.getActionGrid().replaceAction("r" + reductions.get("Return -> Statement"), "r" + reductions.get("Return -> Statement") + "if;");
        parser.getActionGrid().replaceAction("r" + reductions.get("If Statement -> Conditional Tree"), "r" + reductions.get("If Statement -> Conditional Tree") + "if;");

        parser.getActionGrid().replaceAction("s" + states.get("ifCondition"), "s" + states.get("preArithmetic"));
        parser.getActionGrid().replaceAction("s" + states.get("whileCondition"), "s" + states.get("preArithmetic"));

        parser.getActionGrid().addAction(states.get("beforeArray"), "SquareBracketLeft", "s" + states.get("returnMethodCall"), 0);
        parser.getActionGrid().addAction(states.get("returnMethodCall"), "SquareBracketRight", "r" + reductions.get("Array"));

        allowInterativeReduction("methodDefinition", "argument", "Argument", "Arguments");
        parser.getActionGrid().addAction(states.get("methodDefinition"), "Arguments", "s" + states.get("methodHeader"));
        parser.getActionGrid().addAction(states.get("methodArgument"), "Var", "s" + states.get("methodDefinition"));

        Collections.reverse(parser.getActionGrid().getActions(states.get("return"), "Expression"));
        Collections.reverse(parser.getActionGrid().getActions(states.get("name"), "Name"));

        Collections.reverse(parser.getActionGrid().getActions(states.get("ifStatement"), "Conditional"));

        parser.getActionGrid().addAction(parser.getActionGrid().getActions(states.get("ifStatement"), "Else").get(0).getState().getID(), "CurlyBracketLeft", "s" + states.get("enclosedPhrase"));
        parser.getActionGrid().addAction(states.get("arithmetic"), "Operator", "r" + reductions.get("Operator -> Expression"));
        String[] operators = {"+", "-", "*", "/", "%"};
        Integer[] operatorStates = new Integer[operators.length];
        int state;
        for (int i = 0; i < operators.length; i += 1) {
            parser.getActionGrid().addAction(states.get("arithmetic"), operators[i] + "Op", "r" + (reductions.get("Plus -> Operator") + (i * 2) - 1));
            state = reductions.get("Plus -> Operator") + (i * 2);
            if (operators[i].equals("+") || operators[i].equals("-")) {
                createOperatorTransitions(states.get("arithmetic"), state);
                createOperatorTransitions(states.get("returnMethodCall"), state);
            }
            else {
                parser.getActionGrid().addAction(states.get("arithmetic"), "Expression", "r" + state);
                parser.getActionGrid().addAction(states.get("returnMethodCall"), "Expression", "r" + state);
            }
            operatorStates[i] = parser.getActionGrid().getActions(states.get("arithmetic"), operators[i]).get(0).getState().getID();
            parser.getActionGrid().addAction(1, operators[i], "s" + operatorStates[i]);
            parser.getActionGrid().addAction(states.get("returnMethodCall"), operators[i], "s" + operatorStates[i]);
            stringMethods.silentPrintLine(stringMethods.tuple(operators[i], operatorStates[i]), silent);
        }
        String[] comparators = {"==", "!=", "<", ">", "<=", ">="};
        Integer[] comparisonStates = new Integer[comparators.length];
        for (int i = 0; i < comparators.length; i += 1) {
            parser.getActionGrid().addAction(states.get("arithmetic"), "x" + comparators[i] + "y", "r" + (reductions.get("Equals -> Conditional") + (i * 2) - 1));
            parser.getActionGrid().addAction(states.get("arithmetic"), "Expression", "r" + (reductions.get("Equals -> Conditional") + (i * 2)) + "if;");
            parser.getActionGrid().addAction(states.get("arithmetic"), "Expression", "r" + (reductions.get("Equals -> Conditional") + (i * 2)) + "if)");
            parser.getActionGrid().addAction(states.get("arithmetic"), "Expression", "r" + (reductions.get("Equals -> Conditional") + (i * 2)) + "if]");
            parser.getActionGrid().addAction(states.get("arithmetic"), "Expression", "r" + (reductions.get("Equals -> Conditional") + (i * 2)) + "if,");
            parser.getActionGrid().addAction(states.get("arithmetic"), "Equation", "r" + (reductions.get("If Statement") - 1) + "if)");
            parser.getActionGrid().addAction(states.get("arithmetic"), "Equation", "r" + (reductions.get("Elif Statement") - 1) + "if)");
            parser.getActionGrid().addAction(states.get("arithmetic"), "Equation", "r" + (reductions.get("While Loop") - 1) + "if)");
            comparisonStates[i] = parser.getActionGrid().getActions(states.get("arithmetic"), comparators[i]).get(0).getState().getID();
            parser.getActionGrid().addAction(1, comparators[i], "s" + comparisonStates[i]);
            stringMethods.silentPrintLine(stringMethods.tuple(comparators[i], comparisonStates[i]), silent);
        }
        parser.getActionGrid().clearAction(parser.getActionGrid().removeAction(states.get("assignee"), "Expression", 1));
        parser.getActionGrid().addAction(states.get("assignee"), "BracketLeft", "s" + states.get("assignee"));
        parser.getActionGrid().addAction(states.get("assignee"), "Expression", "r" + reductions.get("Expression -> List") + "if,");
        //parser.getActionGrid().addAction(states.get("assignee"), "Expression", "r" + reductions.get("Expression -> List") + "if)");
        parser.getActionGrid().addAction(states.get("assignee"), "Expression", "r" + reductions.get("Expression -> List") + "if]");
        parser.getActionGrid().addAction(states.get("assignee"), "Comma", "s" + states.get("comma"));
        parser.getActionGrid().addAction(states.get("arithmetic"), "MethodCall", "r" + (reductions.get("Plus -> Operator") - 7));
        parser.getActionGrid().addAction(states.get("arithmetic"), "Comma", "s" + states.get("comma"));
        stringMethods.silentPrintLine(stringMethods.tuple(",", states.get("comma")), silent);
        for (int i = 0; i < operatorStates.length; i += 1) {
            parser.getActionGrid().addAction(operatorStates[i], "Power", "s" + states.get("power"));
            if (operators[i].equals("+") || operators[i].equals("-")) {
                parser.getActionGrid().addAction(operatorStates[i], "Expression", "s" + states.get("arithmetic"), 0);
            }
            else {
                parser.getActionGrid().addAction(operatorStates[i], "Expression", "s" + states.get("arithmetic"));
            }
            parser.getActionGrid().addAction(operatorStates[i], "Expression", "r" + (reductions.get("Plus -> Operator") + (i * 2)) + "if==", 0);
            parser.getActionGrid().addAction(operatorStates[i], "Expression", "r" + (reductions.get("Plus -> Operator") + (i * 2)) + "if!=", 1);
            parser.getActionGrid().addAction(operatorStates[i], "Expression", "r" + (reductions.get("Plus -> Operator") + (i * 2)) + "if<", 2);
            parser.getActionGrid().addAction(operatorStates[i], "Expression", "r" + (reductions.get("Plus -> Operator") + (i * 2)) + "if>", 3);
            parser.getActionGrid().addAction(operatorStates[i], "Expression", "r" + (reductions.get("Plus -> Operator") + (i * 2)) + "if<=", 4);
            parser.getActionGrid().addAction(operatorStates[i], "Expression", "r" + (reductions.get("Plus -> Operator") + (i * 2)) + "if>=", 5);
            parser.getActionGrid().addAction(operatorStates[i], "Expression", "r" + (reductions.get("Plus -> Operator") + (i * 2)) + "if,", 6);
            parser.getActionGrid().addAction(operatorStates[i], "BracketLeft", "s" + states.get("assignee"));
            parser.getActionGrid().addAction(operatorStates[i], "BracketRight", "s" + states.get("arithmetic"));
        }
        for (int compState : comparisonStates) {
            parser.getActionGrid().addAction(compState, "Expression", "s" + states.get("arithmetic"), 0);
            parser.getActionGrid().addAction(compState, "BracketLeft", "s" + states.get("assignee"));
            parser.getActionGrid().addAction(compState, "BracketRight", "s" + states.get("arithmetic"));
        }
        parser.getActionGrid().addAction(states.get("preArithmetic"), "Expression", "r" + reductions.get("Multiple Parameter Method Call"));
        parser.getActionGrid().addAction(states.get("arithmetic"), "BracketRight", "r" + reductions.get("Single Parameter Method Call"));
        parser.getActionGrid().addAction(states.get("arithmetic"), "BracketRight", "r" + reductions.get("Multiple Parameter Method Call"));
        parser.getActionGrid().addAction(states.get("arithmetic"), "BracketRight", "r" + reductions.get("Bracketed Expression"));
        parser.getActionGrid().addAction(states.get("arithmetic"), "Expression", "r" + reductions.get("Expression -> List") + "if,");
        //parser.getActionGrid().addAction(states.get("arithmetic"), "Expression", "r" + reductions.get("Expression -> List") + "if)");
        parser.getActionGrid().addAction(states.get("arithmetic"), "List", "r" + (reductions.get("Expression -> List") - 1));
        parser.getActionGrid().addAction(states.get("arithmetic"), "Expression", "r" + reductions.get("Incomplete List"));
        parser.getActionGrid().addAction(states.get("arithmetic"), "SquareBracketRight", "r" + reductions.get("Array"));
        parser.getActionGrid().addAction(states.get("arithmetic"), "Array", "r" + reductions.get("Array -> Expression"));
        parser.getActionGrid().addAction(states.get("comma"), "BracketRight", "r" + reductions.get("Single Parameter Method Call"));
        parser.getActionGrid().addAction(states.get("comma"), "Expression", "r" + reductions.get("Expression -> Assignee"));
        parser.getActionGrid().addAction(states.get("comma"), "List", "r" + (reductions.get("Expression -> List") - 1));
        parser.getActionGrid().addAction(states.get("comma"), "Name", "r" + reductions.get("Name -> Variable") + "if)or]");
        parser.getActionGrid().addAction(states.get("comma"), "Variable", "r" + reductions.get("Variable -> Expression"));
        parser.getActionGrid().addAction(states.get("item"), "Item", "r" + (reductions.get("Name -> Variable") + 1));
        parser.getActionGrid().addAction(states.get("item"), "Variable", "r" + reductions.get("Variable -> Expression"));
        parser.getActionGrid().addAction(states.get("powerExpression"), "^Op", "r" + (reductions.get("Plus -> Operator") + 7));
        parser.getActionGrid().addAction(states.get("powerExpression"), "Operator", "r" + reductions.get("Operator -> Expression"));
        for (int i = 0; i < operatorStates.length; i += 1) {
            parser.getActionGrid().addAction(states.get("powerExpression"), operators[i] + "Op", "r" + (reductions.get("Plus -> Operator") + (i * 2) - 1));
            parser.getActionGrid().addAction(states.get("powerExpression"), "Expression", "r" + (reductions.get("Plus -> Operator") + (i * 2)));
        }
        parser.getActionGrid().addAction(states.get("powerExpression"), "Expression", "s" + states.get("arithmetic"));
        //Collections.reverse(parser.getActionGrid().getActions(states.get("comma"), "Expression"));

        parser.getActionGrid().appendNewAction("r" + reductions.get("Name -> Variable"), "g" + states.get("preItem"));
        parser.getActionGrid().replaceAction("r" + reductions.get("Name -> Variable"), "r" + reductions.get("Name -> Variable") + "ifnot(or[");
        parser.getActionGrid().replaceAction("r" + reductions.get("Expression -> Assignee"), "r" + reductions.get("Expression -> Assignee") + "if;");
        parser.getActionGrid().appendNewAction("r" + reductions.get("Expression -> Assignee") + "if;", "s" + states.get("arithmetic"));

        parser.getActionGrid().addAction(1, "MethodCall", "r" + (reductions.get("Return -> Statement") - 1)  + "if;");
        Collections.reverse(parser.getActionGrid().getActions(1, "MethodCall"));
        Collections.reverse(parser.getActionGrid().getActions(states.get("phrase"), "Name"));
        Collections.reverse(parser.getActionGrid().getActions(states.get("array"), "Comma"));
        Collections.reverse(parser.getActionGrid().getActions(states.get("arrayElement"), "List"));
        parser.getActionGrid().addAction(states.get("arrayComma"), "Comma", "s" + states.get("arrayElement"));
        parser.getActionGrid().addAction(states.get("arrayComma"), "SquareBracketRight", "r" + reductions.get("Array"));
        parser.getActionGrid().addAction(1, "Expression", "r" + reductions.get("Return -> Statement") + "if;", 0);
        parser.getActionGrid().addAction(1, "Expression", "r" + reductions.get("Expression -> Assignee") + "if;", 1);
        parser.getActionGrid().addAction(1, "Array", "r" + reductions.get("Array -> Expression") + "if;");
        parser.getActionGrid().addAction(1, "Assignee", "r" + reductions.get("Assignment -> Statement") + "if;");
        parser.getActionGrid().addAction(1, "Assignee", "r" + reductions.get("Assignee Return -> Statement") + "if;", 0);
        parser.getActionGrid().addAction(1, "Assignee", "r" + reductions.get("Assignee -> Statement") + "if;");
        parser.getActionGrid().addAction(1, "ElseStatement", "r" + reductions.get("Conditional Tree"));
        parser.getActionGrid().addAction(1, "ElseStatement", "r" + (reductions.get("Conditional Tree") + 1));
        parser.getActionGrid().addAction(1, "BracketRight", "r" + reductions.get("Single Parameter Method Call"));
        parser.getActionGrid().addAction(1, "BracketRight", "r" + reductions.get("Multiple Parameter Method Call"));
        parser.getActionGrid().addAction(1, "MethodCall", "r" + reductions.get("Method Call -> Statement"), 0);
        parser.getActionGrid().addAction(1, "SquareBracketLeft", "s" + states.get("returnMethodCall"));
        parser.getActionGrid().addAction(1, "Phrase", "r1", 0);
        createBodyExpressions(1);
        createBodyExpressions(states.get("phrase"));
        parser.getActionGrid().addAction(states.get("phrase"), "Phrase", "r1");
        parser.getActionGrid().addAction(states.get("phrase"), "CurlyBracketRight", "r" + reductions.get("Enclosed Phrase"));
        parser.getActionGrid().addAction(states.get("returnMethodArgument"), "Comma", "s" + states.get("returnMethodCall"));
        parser.getActionGrid().addAction(states.get("returnMethodArgument"), "BracketRight", "r" + reductions.get("For Header"));
        parser.getActionGrid().addAction(states.get("returnMethodArgument"), "Expression", "g" + states.get("arithmetic"));
        parser.getActionGrid().addAction(states.get("returnMethodArgument"), "Array", "r" + reductions.get("Array Iterator") + "if)");
        parser.getActionGrid().addAction(states.get("returnMethodArgument"), "SquareBracketRight", "r" + reductions.get("Array"));
        parser.getActionGrid().addAction(states.get("returnMethodCall"), "List", "r" + (reductions.get("Expression -> List") - 1));
        parser.getActionGrid().moveAction(states.get("returnMethodCall"), "Expression", 0, states.get("returnMethodCall"), "List");
        parser.getActionGrid().addAction(states.get("returnMethodCall"), "Expression", "r" + reductions.get("Expression -> List") + "if,");
        //parser.getActionGrid().addAction(states.get("returnMethodCall"), "Expression", "r" + reductions.get("Expression -> List") + "if)");
        parser.getActionGrid().addAction(states.get("returnMethodCall"), "Expression", "r" + reductions.get("Expression -> List") + "if]");
        parser.getActionGrid().addAction(states.get("iterator"), "BracketRight", "r" + reductions.get("For Header"));
        parser.getActionGrid().copyAction(states.get("assignee"), "Expression", parser.getActionGrid().getActions(states.get("assignee"), "Expression").size() - 1, states.get("assignee"), "List");
        parser.getActionGrid().moveAction(states.get("arithmetic"), "Expression", 6, states.get("arithmetic"), "Expression");
        parser.getActionGrid().addAction(states.get("arithmetic"), "MethodCall", "r" + reductions.get("Method Call -> Expression"));
        parser.getActionGrid().appendNewAction("r" + reductions.get("Array Iterator") + "if)", "r" + reductions.get("Array -> Expression"));
        stringMethods.silentPrintLine(silent);
    }
}
