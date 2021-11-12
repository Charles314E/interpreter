package com.N3_SemanticActions;

import com.N3_SemanticActions.Tokens.Expression;
import com.N3_SemanticActions.Tokens.Token;
import com.N4_SymbolTables.MethodTable;
import com.N4_SymbolTables.VariableTable;

//Adapted from the symbol table implementation on page 106 of "Modern Compiler Implementation in Java", by Andrew W. Appel
public class SyntaxTree {
    private final Token concreteRoot;
    private Token abstractRoot;
    private final Visitor.Interpreter interpreter;

    public SyntaxTree(Token root) {
        this(root, root);
    }
    public SyntaxTree(Token root, Token aRoot) {
        interpreter = new Visitor.Interpreter(new VariableTable(), new MethodTable(), false);
        interpreter.variables.setInterpreter(interpreter);
        interpreter.methods.setInterpreter(interpreter);
        concreteRoot = root;
        abstractRoot = rebuildTree(aRoot);
        System.out.println("ROOT: " + concreteRoot + " " + abstractRoot);
    }

    public Token rebuildTree(Token root) {
        try {
            System.out.println("BUILDING AST OF " + root.toString() + "...");
            return root.getToken().getType().getTokenFactory().makeObject().construct(root);
        }
        catch (NullPointerException e) {
            try {
                e.printStackTrace();
                return root.attemptToConstruct();
            }
            catch (NullPointerException e1) {
                return null;
            }
        }
    }

    public Visitor.Interpreter getInterpreter() {
        return interpreter;
    }

    public Token getAbstractRoot() {
        return abstractRoot;
    }
    public Token getConcreteRoot() {
        return concreteRoot;
    }
    public void setRoot(Token root) {
        abstractRoot = root;
    }

    public String toString() {
        try {
            return abstractRoot.stringChildren(0) + "\nResult:\n" + abstractRoot.toString(0);
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public Object execute() {
        return execute(abstractRoot);
    }
    public Object execute(Token token) {
        try {
            return getInterpreter().getValue((Expression) token.accept(interpreter)).accept(interpreter);
        }
        catch (ClassCastException e) {
            return token.accept(interpreter);
        }
    }
}
