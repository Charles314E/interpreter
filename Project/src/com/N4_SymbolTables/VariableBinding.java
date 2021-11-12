package com.N4_SymbolTables;

import com.N3_SemanticActions.Tokens.Expression;
import com.N3_SemanticActions.Tokens.Value;
import com.N3_SemanticActions.Visitor;

public class VariableBinding extends Binding {
    public Value value;
    public VariableBinding construct(Object ... obj) {
        if (!constructed) {
            try {
                interpreter = (Visitor.Interpreter) obj[0];
                value = interpreter.getValue((Expression) obj[1]);
                constructed = true;
            }
            catch (ClassCastException e) {
                return this;
            }
        }
        return this;
    }
}
