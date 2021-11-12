package com.GUI;

import com.GUI.TokenBlocks.Headers.IfHeaderBlock;
import com.GUI.TokenBlocks.Headers.MethodHeaderBlock;
import com.GUI.TokenBlocks.Headers.WhileHeaderBlock;
import com.GUI.TokenBlocks.Operators.EqSigns.*;
import com.GUI.TokenBlocks.Operators.OpSigns.*;
import com.GUI.TokenBlocks.Values.*;
import com.GUI.TokenBlocks.Virtual.EquationBlock;
import com.GUI.TokenBlocks.Virtual.ExpressionBlock;
import com.GUI.TokenBlocks.Virtual.OperatorBlock;

public class GrammarVisitor {
    public final GUI frame;
    public GrammarVisitor(GUI frame) {
        this.frame = frame;
    }

    public void visit(IdentifierBlock identifierBlock) {
        identifierBlock.setGrammars();
    }

    public void visit(StringBlock stringBlock) {
        stringBlock.setGrammars();
    }

    public void visit(NullBlock nullBlock) {
        nullBlock.setGrammars();
    }

    public void visit(IntegerBlock integerBlock) {
        integerBlock.setGrammars();
    }

    public void visit(FloatBlock floatBlock) {
        floatBlock.setGrammars();
    }

    public void visit(BooleanBlock booleanBlock) {
        booleanBlock.setGrammars();
    }

    public void visit(PlusBlock plusBlock) {
        plusBlock.setGrammars();
    }

    public void visit(MinusBlock minusBlock) {
        minusBlock.setGrammars();
    }

    public void visit(MultiplyBlock multiplyBlock) {
        multiplyBlock.setGrammars();
    }

    public void visit(DivideBlock divideBlock) {
        divideBlock.setGrammars();
    }

    public void visit(ExponentBlock exponentBlock) {
        exponentBlock.setGrammars();
    }

    public void visit(ModuloBlock moduloBlock) {
        moduloBlock.setGrammars();
    }

    public void visit(IndexBlock indexBlock) {
        indexBlock.setGrammars();
    }

    public void visit(EqualsBlock equalsBlock) {
        equalsBlock.setGrammars();
    }

    public void visit(NotEqualsBlock notEqualsBlock) {
        notEqualsBlock.setGrammars();
    }

    public void visit(LessThanBlock lessThanBlock) {
        lessThanBlock.setGrammars();
    }

    public void visit(GreaterThanBlock greaterThanBlock) {
        greaterThanBlock.setGrammars();
    }

    public void visit(LessThanEqualsBlock lessThanEqualsBlock) {
        lessThanEqualsBlock.setGrammars();
    }

    public void visit(GreaterThanEqualsBlock greaterThanEqualsBlock) {
        greaterThanEqualsBlock.setGrammars();
    }

    public void visit(ExpressionBlock expressionBlock) {
        expressionBlock.setGrammars();
    }

    public void visit(EquationBlock equationBlock) {
        equationBlock.setGrammars();
    }

    public void visit(OperatorBlock operatorBlock) {
        operatorBlock.setGrammars();
    }

    public void visit(IfHeaderBlock ifHeaderBlock) {
        ifHeaderBlock.setGrammars();
    }

    public void visit(WhileHeaderBlock whileHeaderBlock) {
        whileHeaderBlock.setGrammars();
    }

    public void visit(MethodHeaderBlock methodHeaderBlock) {
        methodHeaderBlock.setGrammars();
    }

    public void visit(MethodNameBlock methodNameBlock) {
        methodNameBlock.setGrammars();
    }
}
