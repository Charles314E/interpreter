package com.N3_SemanticActions;

import com.Exceptions.TypeError;
import com.Exceptions.UndefinedError;
import com.N3_SemanticActions.Tokens.*;
import com.N3_SemanticActions.Tokens.Equations.*;
import com.N3_SemanticActions.Tokens.Expressions.*;
import com.N3_SemanticActions.Tokens.Operators.*;
import com.N3_SemanticActions.Tokens.Statements.*;
import com.N3_SemanticActions.Tokens.Statements.VirtualList;
import com.N3_SemanticActions.Tokens.Values.*;
import com.N4_SymbolTables.*;
import com.Methods.stringMethods;

import javax.naming.PartialResultException;
import java.lang.constant.Constable;
import java.util.ArrayList;
import java.util.Arrays;


public interface Visitor {
    Double visit(IntegerLiteral integer);

    Double visit(FloatLiteral real);

    Boolean visit(BooleanLiteral bool);

    String visit(StringLiteral string);

    Constable visit(NullLiteral none);

    Value visit(Identifier id);

    Value visit(Item item);

    FloatLiteral visit(PlusOperator plus);

    FloatLiteral visit(MinusOperator minus);

    FloatLiteral visit(MultiplyOperator multiply);

    FloatLiteral visit(DivisionOperator division);

    FloatLiteral visit(ModulusOperator modulus);

    FloatLiteral visit(ExponentOperator exponent);

    BooleanLiteral visit(Equals equals);

    BooleanLiteral visit(NotEquals notEquals);

    BooleanLiteral visit(LessThan lessThan);

    BooleanLiteral visit(MoreThan moreThan);

    BooleanLiteral visit(LessThanEquals lessThanEquals);

    BooleanLiteral visit(MoreThanEquals moreThanEquals);

    BooleanLiteral visit(And and);

    BooleanLiteral visit(Or or);

    BooleanLiteral visit(Not not);

    Object visit(Array array);

    Object visit(VirtualList list);

    Object visit(AssignmentStatement assignment);

    Expression visit(ConditionalBranch branch);

    BooleanLiteral visit(IfStatement ifStatement);

    BooleanLiteral visit(ElseStatement elseStatement);

    Value visit(Phrase phrase);

    BooleanLiteral visit(In in);

    Value visit(ForLoop forLoop);

    Object visit(Iterator iterator);

    Value visit(WhileLoop whileLoop);

    Value visit(MethodCall methodCall);

    Value visit(ReturnStatement returnStatement);

    Object visit(MethodDefinition def);

    Phrase visit(ArgumentList arguments);

    class Interpreter implements Visitor {
        public final VariableTable variables;
        public final MethodTable methods;
        private boolean silent;

        public Interpreter(VariableTable variables, MethodTable methods, boolean silent) {
            this.variables = variables;
            this.methods = methods;
            this.silent = silent;
        }

        public Value getValue(Expression exp) {
            if (exp != null) {
                if (exp instanceof Value) {
                    if (exp instanceof Identifier) {
                        return (Value) exp.accept(this);
                    } else {
                        return (Value) exp;
                    }
                } else {
                    return (Value) exp.accept(this);
                }
            }
            return null;
        }

        @Override
        public Double visit(IntegerLiteral integer) {
            return (double) Integer.parseInt(integer.getValue());
        }

        @Override
        public Double visit(FloatLiteral real) {
            return Double.parseDouble(real.getValue());
        }

        @Override
        public Boolean visit(BooleanLiteral bool) {
            return bool.getValue().equals("true");
        }

        @Override
        public String visit(StringLiteral string) {
            return string.getValue();
        }

        @Override
        public Constable visit(NullLiteral none) {
            return null;
        }

        @Override
        public Value visit(Identifier id) throws UndefinedError {
            try {
                stringMethods.silentPrintLine(stringMethods.tuple("lookup", id, variables.lookup(id).value), silent);
                return variables.lookup(id).value;
            }
            catch (NullPointerException e) {
                throw new UndefinedError(id);
            }
        }

        @Override
        public Value visit(Item item) throws UndefinedError, TypeError {
            try {
                Array value = (Array) item.getIdentifier().accept(this);
                Value index = getValue(item.getIndex());
                return getValue(value.getArray().get((Integer) index.accept(this)));
            }
            catch (ClassCastException e) {
                throw new TypeError(item.getIdentifier());
            }
        }

        @Override
        public FloatLiteral visit(PlusOperator plus) throws TypeError, UndefinedError {
            try {
                stringMethods.silentPrintLine(stringMethods.tuple("ADDITION", getValue(plus.getAugend()).accept(this), getValue(plus.getAddend()).accept(this)), silent);
                return new FloatLiteral(
                        (Double) getValue(plus.getAugend()).accept(this)
                        + (Double) getValue(plus.getAddend()).accept(this)
                );
            }
            catch (UndefinedError e) {
                throw e;
            }
        }

        @Override
        public FloatLiteral visit(MinusOperator minus) throws TypeError, UndefinedError {
            try {
                stringMethods.silentPrintLine(stringMethods.tuple("SUBTRACTION", getValue(minus.getSubtrahend()).accept(this), getValue(minus.getMinuend()).accept(this)), silent);
                return new FloatLiteral(
                        (Double) getValue(minus.getSubtrahend()).accept(this)
                        - (Double) getValue(minus.getMinuend()).accept(this)
                );
            }
            catch (UndefinedError e) {
                throw e;
            }
        }

        @Override
        public FloatLiteral visit(MultiplyOperator multiply) throws TypeError, UndefinedError {
            try {
                stringMethods.silentPrintLine(stringMethods.tuple("MULTIPLICATION", getValue(multiply.getMultiplicand()).accept(this), getValue(multiply.getMultiplier()).accept(this)), silent);
                return new FloatLiteral(
                        (Double) getValue(multiply.getMultiplicand()).accept(this)
                        * (Double) getValue(multiply.getMultiplier()).accept(this)
                );
            }
            catch (UndefinedError e) {
                throw e;
            }
        }

        @Override
        public FloatLiteral visit(DivisionOperator division) throws TypeError, UndefinedError {
            try {
                stringMethods.silentPrintLine(stringMethods.tuple("DIVISION", getValue(division.getDividend()).accept(this), getValue(division.getDivisor()).accept(this)), silent);
                return new FloatLiteral(
                        (Double) getValue(division.getDividend()).accept(this)
                        / (Double) getValue(division.getDivisor()).accept(this)
                );
            }
            catch (UndefinedError e) {
                throw e;
            }
        }

        @Override
        public FloatLiteral visit(ModulusOperator modulus) throws TypeError, UndefinedError {
            try {
                stringMethods.silentPrintLine(stringMethods.tuple("MODULO", getValue(modulus.getBase()).accept(this), getValue(modulus.getDivisor()).accept(this)), silent);
                return new FloatLiteral(
                        (Double) getValue(modulus.getBase()).accept(this)
                        % (Double) getValue(modulus.getDivisor()).accept(this)
                );
            }
            catch (UndefinedError e) {
                throw e;
            }
        }

        @Override
        public FloatLiteral visit(ExponentOperator exponent) throws TypeError, UndefinedError {
            try {
                stringMethods.silentPrintLine(stringMethods.tuple("POWER", getValue(exponent.getBase()).accept(this), getValue(exponent.getExponent()).accept(this)), silent);
                return new FloatLiteral(
                        Math.pow(
                                (Double) getValue(exponent.getBase()).accept(this),
                                (Double) getValue(exponent.getExponent()).accept(this))
                );
            }
            catch (UndefinedError e) {
                throw e;
            }
        }

        @Override
        public BooleanLiteral visit(Equals equals) {
            boolean eq = getValue(equals.getPredicate()).equals(getValue(equals.getSubject()), this);
            stringMethods.silentPrintLine(stringMethods.tuple("EQUALS", getValue(equals.getPredicate()).accept(this), getValue(equals.getSubject()).accept(this), eq), silent);
            return new BooleanLiteral(eq);
        }

        @Override
        public BooleanLiteral visit(NotEquals notEquals) {
            boolean ne = getValue(notEquals.getPredicate()).equals(getValue(notEquals.getSubject()), this);
            stringMethods.silentPrintLine(stringMethods.tuple("NOT_EQUALS", getValue(notEquals.getPredicate()).accept(this), getValue(notEquals.getSubject()).accept(this), ne), silent);
            return new BooleanLiteral(ne);
        }

        @Override
        public BooleanLiteral visit(LessThan lessThan) throws ClassCastException {
            boolean lt = ((Double) getValue(lessThan.getPredicate()).accept(this) < (Double) getValue(lessThan.getSubject()).accept(this));
            stringMethods.silentPrintLine(stringMethods.tuple("LESS_THAN", getValue(lessThan.getPredicate()).accept(this), getValue(lessThan.getSubject()).accept(this), lt), silent);
            return new BooleanLiteral(lt);
        }

        @Override
        public BooleanLiteral visit(MoreThan moreThan) throws ClassCastException {
            boolean mt = ((Double) getValue(moreThan.getPredicate()).accept(this) > (Double) getValue(moreThan.getSubject()).accept(this));
            stringMethods.silentPrintLine(stringMethods.tuple("MORE_THAN", getValue(moreThan.getPredicate()).accept(this), getValue(moreThan.getSubject()).accept(this), mt), silent);
            return new BooleanLiteral(mt);
        }

        @Override
        public BooleanLiteral visit(LessThanEquals lessThanEquals) throws ClassCastException {
            boolean lte = ((Double) getValue(lessThanEquals.getPredicate()).accept(this) <= (Double) getValue(lessThanEquals.getSubject()).accept(this));
            stringMethods.silentPrintLine(stringMethods.tuple("LESS_THAN_EQUALS", getValue(lessThanEquals.getPredicate()).accept(this), getValue(lessThanEquals.getSubject()).accept(this), lte), silent);
            return new BooleanLiteral(lte);
        }

        @Override
        public BooleanLiteral visit(MoreThanEquals moreThanEquals) throws ClassCastException {
            boolean mte = ((Double) getValue(moreThanEquals.getPredicate()).accept(this) >= (Double) getValue(moreThanEquals.getSubject()).accept(this));
            stringMethods.silentPrintLine(stringMethods.tuple("MORE_THAN_EQUALS", getValue(moreThanEquals.getPredicate()).accept(this), getValue(moreThanEquals.getSubject()).accept(this), mte), silent);
            return new BooleanLiteral(mte);
        }

        @Override
        public BooleanLiteral visit(And and) {
            return new BooleanLiteral(and.getPredicate().accept(this).accept(this) && and.getSubject().accept(this).accept(this));
        }

        @Override
        public BooleanLiteral visit(Or or) {
            return new BooleanLiteral(or.getPredicate().accept(this).accept(this) || or.getSubject().accept(this).accept(this));
        }

        @Override
        public BooleanLiteral visit(Not not) {
            return new BooleanLiteral(!not.getSubject().accept(this).accept(this));
        }

        @Override
        public Object visit(Array array) {
            return null;
        }

        @Override
        public Object visit(VirtualList list) {
            return null;
        }

        @Override
        public Object visit(AssignmentStatement assignment) {
            VariableBinding binding;
            if (assignment.getValue() instanceof Value) {
                binding = variables.insert(assignment.getIdentifier(), assignment.getValue());
            }
            else {
                binding = variables.insert(assignment.getIdentifier(), ((Expression) assignment.getValue().accept(this)));
            }
            stringMethods.silentPrintLine(stringMethods.tuple("ASSIGN", assignment.getIdentifier(), binding.value), silent);
            return null;
        }

        @Override
        public Expression visit(ConditionalBranch tree) {
            for (Conditional con : tree.getBranches()) {
                if (con.accept(this).accept(this)) {
                    return con.getResult().accept(this);
                }
            }
            return null;
        }

        @Override
        public BooleanLiteral visit(IfStatement ifStatement) {
            BooleanLiteral con = ifStatement.getCondition().accept(this);
            System.out.println(stringMethods.tuple("IF", con.accept(this), con.getValue()));
            return con;
        }

        @Override
        public BooleanLiteral visit(ElseStatement elseStatement) {
            BooleanLiteral con = new BooleanLiteral(true);
            System.out.println(stringMethods.tuple("ELSE", con.accept(this), con.getValue()));
            return con;
        }

        @Override
        public Value visit(Phrase phrase) {
            Value result = null;
            System.out.println(stringMethods.tuple("PHRASE", phrase.getStatements().size()));
            for (Statement statement : phrase.getStatements()) {
                System.out.println(stringMethods.tuple("STATEMENT", statement.toString()));
                result = getValue((Expression) statement.accept(this));
                if (result != null) {
                    return result;
                }
            }
            return result;
        }

        @Override
        public BooleanLiteral visit(In in) {
            return null;
        }

        @Override
        public Value visit(ForLoop forLoop) {
            forLoop.getIterator().accept(this);
            return forLoop.loopThroughBody(this);
        }

        @Override
        public Object visit(Iterator iterator) {
            ArrayList<Expression> elements;
            if (iterator.array == null) {
                System.out.println(stringMethods.tuple("ITERATOR", "bounded"));
                int first, last;
                double d;
                try {
                    d = (double) getValue(iterator.first).accept(this);
                    first = (int) d;
                }
                catch (ClassCastException e) {
                    throw new TypeError(iterator.first);
                }
                try {
                    d = (double) getValue(iterator.last).accept(this);
                    last = (int) d;
                }
                catch (ClassCastException e) {
                    throw new TypeError(iterator.last);
                }
                elements = new ArrayList<>();
                System.out.println(stringMethods.tuple("bounds", first, last));
                try {
                    int size = Math.abs(first - last);
                    if (size > 0) {
                        int interval = (last - first) / size;
                        for (int i = first; i != last + interval; i += interval) {
                            elements.add(new IntegerLiteral(i));
                        }
                    }
                }
                catch (ArithmeticException e) {

                }
            }
            else {
                System.out.println(stringMethods.tuple("ITERATOR", "array"));
                elements = iterator.array.getArray();
            }
            iterator.constructArray(elements);
            System.out.println(stringMethods.tuple("ITERATOR_ARRAY", Arrays.asList(iterator.assignments)));
            return null;
        }

        @Override
        public Value visit(WhileLoop whileLoop) {
            return whileLoop.loopThroughBody(this);
        }

        @Override
        public Value visit(MethodCall call) {
            MethodBinding method = methods.lookup(call.getIdentifier());
            stringMethods.silentPrintLine(stringMethods.tuple("METHOD", call.getIdentifier(), call.getArgumentLength()), true);
            if (method.getArgumentLength() == call.getArgumentLength()) {
                for (int i = 0; i < method.getArgumentLength(); i += 1) {
                    variables.insert(method.getArgument(i), new ScopeBlock());
                    variables.insert(method.getArgument(i), call.getArgument(i));
                }
                Value result = methods.lookup(call.getIdentifier()).getBody().accept(this);
                for (int i = 0; i < method.getArgumentLength(); i += 1) {
                    while (!(variables.pop(method.getArgument(i)) instanceof ScopeBlock)) {
                        if (variables.lookup(method.getArgument(i)) == null) {
                            break;
                        }
                    }
                    variables.pop(method.getArgument(i));
                }
                stringMethods.silentPrintLine(stringMethods.tuple("RETURN", call.getIdentifier(), result), true);
                return result;
            }
            return null;
        }

        @Override
        public Value visit(ReturnStatement output) {
            return getValue(output.getExpression());
        }

        @Override
        public Object visit(MethodDefinition def) {
            methods.insert(def.getIdentifier(), def);
            return null;
        }

        @Override
        public Phrase visit(ArgumentList args) {
            return null;
        }
    }
}
