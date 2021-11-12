package com.N1_Lexer;

import com.Automata.DFA;
import com.Automata.NFA;
import com.Classes.ObjectFactory;
import com.Exceptions.LexicalError;
import com.GUI.DefinedBlock;
import com.GUI.TokenBlocks.Values.*;
import com.GUI.TokenBlocks.Virtual.EquationBlock;
import com.GUI.TokenBlocks.Virtual.ExpressionBlock;
import com.GUI.TokenBlocks.Virtual.OperatorBlock;
import com.Methods.stringMethods;
import com.N1_Lexer.Regex.RegexBase;
import com.N3_SemanticActions.Tokens.Equations.*;
import com.N3_SemanticActions.Tokens.Expressions.And;
import com.N3_SemanticActions.Tokens.Expressions.In;
import com.N3_SemanticActions.Tokens.Expressions.Not;
import com.N3_SemanticActions.Tokens.Expressions.Or;
import com.N3_SemanticActions.Tokens.Operators.*;
import com.N3_SemanticActions.Tokens.Phrase;
import com.N3_SemanticActions.Tokens.Statements.*;
import com.N3_SemanticActions.Tokens.Token;
import com.N3_SemanticActions.Tokens.Values.*;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Lexer<T extends Character> {
    public enum type {
        END_OF_FILE("EndOfFile"),
        WHITESPACE("Whitespace"),
        FULLSPACE("Fullspace"),

        VALUE("Value"),
        NUMBER("Number"),
        VARIABLE("Variable"),
        NAME("Name"),
        ATTRIBUTE("Attribute"),
        ITEM("Item", Item.class),
        IDENTIFIER("Identifier", Identifier.class, IdentifierBlock.class),

        INTEGER("Integer", IntegerLiteral.class, IntegerBlock.class),
        REAL("Real", FloatLiteral.class, FloatBlock.class),
        BOOLEAN("Boolean", BooleanLiteral.class, BooleanBlock.class),
        STRING("String", StringLiteral.class, StringBlock.class),
        NULL("Null", NullLiteral.class, NullBlock.class),

        CONDITIONAL("Conditional"),
        IF("If"),
        ELSE_IF("ElseIf"),
        ELSE("Else"),
        FOR("For"),
        WHILE("While"),

        CONDITIONAL_TREE("ConditionalTree", ConditionalBranch.class),
        IF_HEADER("IfHeader"),
        IF_STATEMENT("IfStatement", IfStatement.class),
        ELSE_IF_HEADER("ElseIfStatement"),
        ELSE_IF_STATEMENT("ElseIfStatement", IfStatement.class),
        ELSE_STATEMENT("ElseStatement", ElseStatement.class),
        LOOP_STATEMENT("LoopStatement"),
        FOR_HEADER("ForHeader"),
        FOR_LOOP("ForLoop", ForLoop.class),
        WHILE_HEADER("WhileHeader"),
        WHILE_LOOP("WhileLoop", WhileLoop.class),

        RETURN("Return"),
        VAR("Var"),
        ASSIGN("Assign"),
        ASSIGNMENT_STATEMENT("AssignmentStatement", AssignmentStatement.class),

        EQUATION("Equation", null, EquationBlock.class),
        EQUALS_STATEMENT("EqualsStatement", Equals.class),
        NOT_EQUALS_STATEMENT("NotEqualsStatement", NotEquals.class),
        LESS_THAN_STATEMENT("LessThanStatement", LessThan.class),
        GREATER_THAN_STATEMENT("GreaterThanStatement", MoreThan.class),
        LESS_THAN_EQUALS_STATEMENT("LessThanOrEqualsStatement", LessThanEquals.class),
        GREATER_THAN_EQUALS_STATEMENT("GreaterThanOrEqualsStatement", MoreThanEquals.class),

        EQUALS("Equals"),
        NOT_EQUALS("NotEquals"),
        LESS_THAN("LessThan"),
        GREATER_THAN("GreaterThan"),
        LESS_THAN_EQUALS("LessThanOrEquals"),
        GREATER_THAN_EQUALS("GreaterThanOrEquals"),

        AND("And"),
        OR("Or"),
        NOT("Not"),
        IN("In"),
        TO("To"),

        AND_STATEMENT("AndStatement", And.class),
        OR_STATEMENT("OrStatement", Or.class),
        NOT_STATEMENT("NotStatement", Not.class),
        IN_STATEMENT("InStatement", In.class),

        PLUS("Plus"),
        MINUS("Minus"),
        MULTIPLY("Multiply"),
        DIVIDE("Divide"),
        POWER("Power"),
        MODULUS("Modulus"),

        OPERATOR("Operator", null, OperatorBlock.class),
        PLUS_OPERATOR("PlusOperator", PlusOperator.class),
        MINUS_OPERATOR("MinusOperator", MinusOperator.class),
        MULTIPLY_OPERATOR("MultiplyOperator", MultiplyOperator.class),
        DIVIDE_OPERATOR("DivisionOperator", DivisionOperator.class),
        POWER_OPERATOR("ExponentOperator", ExponentOperator.class),
        MODULUS_OPERATOR("ModulusOperator", ModulusOperator.class),

        ENCLOSED_PHRASE("EnclosedPhrase"),
        LEFT_BRACKET("BracketLeft"),
        RIGHT_BRACKET("BracketRight"),
        LEFT_SQUARE_BRACKET("SquareBracketLeft"),
        RIGHT_SQUARE_BRACKET("SquareBracketRight"),
        LEFT_CURLY_BRACKET("CurlyBracketLeft"),
        RIGHT_CURLY_BRACKET("CurlyBracketRight"),

        COMMA("Comma"),
        DOT("Dot"),
        COLON("Colon"),
        SEMICOLON("Semicolon"),

        RETURN_STATEMENT("ReturnStatement", ReturnStatement.class),
        PHRASE("Phrase", Phrase.class),
        STATEMENT("Statement"),
        PRINT_KEYWORD("Print"),

        ITERATOR("Iterator", Iterator.class),
        ARRAY("Array", Array.class),
        LIST("List", VirtualList.class),
        TERM("Term"),
        FACTOR("Factor"),

        METHOD_HEADER("MethodHeader"),
        METHOD_DEFINITION("MethodDefinition", MethodDefinition.class),
        DEFINITION("Def"),
        METHOD("MethodCall", MethodCall.class, MethodNameBlock.class),
        ARGUMENT("Argument"),
        ARGUMENTS("Arguments", ArgumentList.class),

        EXPRESSION("Expression", null, ExpressionBlock.class),
        ASSIGNEE("Assignee"),
        POINTER("Pointer"),
        NEW_LINE("NewLine");

        private final String className;
        private final Class<? extends Token> token;
        private final Class<? extends DefinedBlock<?>> block;
        private final ObjectFactory<Token> tokenFactory;
        private final ObjectFactory<DefinedBlock<?>> blockFactory;
        type(String className) {
            this(className, null, null);
        }
        type(String className, Class<? extends Token> classToken) {
            this(className, classToken, null);
        }
        type(String className, Class<? extends Token> classToken, Class<? extends DefinedBlock<?>> classBlock) {
            this.className = className;
            this.token = classToken;
            this.block = classBlock;
            this.tokenFactory = new ObjectFactory<>(classToken);
            this.blockFactory = new ObjectFactory<>(classBlock);
        }

        public static Lexer.type getTokenFactoryType(ObjectFactory<?> factory) {
            for (Lexer.type id : values()) {
                if (id.tokenFactory == factory) {
                    return id;
                }
            }
            return null;
        }

        public String getClassName() {
            return className;
        }
        public ObjectFactory<Token> getTokenFactory() {
            return tokenFactory;
        }
        public static Lexer.type getIdentifier(String name) {
            for (Lexer.type id : values()) {
                if (id.className.equals(name)) {
                    return id;
                }
            }
            return null;
        }
        public static Class<? extends DefinedBlock<?>> getBlock(String name) {
            for (Lexer.type id : values()) {
                if (id.className.equals(name)) {
                    return id.block;
                }
            }
            return null;
        }
        public static String getName(DefinedBlock<?> block) {
            for (Lexer.type id : values()) {
                if (id.block == block.getClass()) {
                    return id.className;
                }
            }
            return null;
        }

        public static ObjectFactory<DefinedBlock<?>> getBlockFactory(ContextToken token) {
            return getBlockFactory(token.getType());
        }
        public static ObjectFactory<DefinedBlock<?>> getBlockFactory(Lexer.type type) {
            for (Lexer.type v : values()) {
                if (type == v) {
                    return v.blockFactory;
                }
            }
            return null;
        }
    }
    private HashMap<type, RegexBase<T>> tokenMap = new HashMap<>();

    public ArrayList<ContextToken> analyse(String input, boolean silent) throws LexicalError {
        Character[] in = stringMethods.toCharacterList(input);
        Lexer.type validType;
        int i = 0, len, tempLen;
        int line = 1, column = 1;
        ArrayList<ContextToken> output = new ArrayList<>();
        RegexBase<T> regex;
        String str;
        while (i < in.length) {
            stringMethods.silentPrintLine(silent);
            validType = null;
            len = 0;
            for (type t : type.values()) {
                if (t != type.END_OF_FILE) {
                    regex = tokenMap.get(t);
                    if (regex != null) {
                        try {
                            tempLen = regex.check((T[]) in, i) - i;
                        }
                        catch (ClassCastException e) {
                            return null;
                        }
                        stringMethods.silentPrintLine(stringMethods.tuple(t, tempLen + i), silent);
                        if (tempLen >= len) {
                            len = tempLen;
                            validType = t;
                        }
                    }
                }
            }
            if (validType != null) {
                str = input.substring(i, i + len);
                stringMethods.silentPrint(stringMethods.tuple(validType, str), silent);
                if (validType == type.NEW_LINE) {
                    line += 1;
                    column = 1;
                }
                else {
                    output.add(new ContextToken(validType, line, column, str));
                    column += len;
                }
                i += len;
                stringMethods.silentPrintLine(" " + stringMethods.tuple(i, in.length, i < in.length), silent);
            }
            else {
                stringMethods.silentPrintLine(stringMethods.tuple(null, input.charAt(i)) + " " + stringMethods.tuple(i), silent);
                throw new LexicalError(line, column, input.charAt(i));
            }
        }
        output.add(new ContextToken(type.END_OF_FILE));
        return output;
    }
    public void addToken(type type, String pattern) {
        pattern = (pattern.length() == 1 || (pattern.length() == 2 && pattern.startsWith("\\")) ? "(" + pattern + ")()" : pattern);
        addToken(type, pattern, true);
    }
    public void addToken(type type, String pattern, boolean silent) {
        tokenMap.put(type, new RegexBase<>(pattern, silent));
        //System.out.println(tokenMap.get(type).getPattern());
    }
    public String printCheck(type type, String input) {
        try {
            return stringMethods.tuple(input, type, getDFA(type).traverse((T[]) stringMethods.toCharacterList(input)) == input.length());
        }
        catch (NullPointerException e) {
            return null;
        }
    }
    public DFA<T> getDFA(type type) {
        return tokenMap.get(type).getNFA().getDFA();
    }
    public NFA<T> getNFA(type type) {
        return tokenMap.get(type).getNFA();
    }
}
