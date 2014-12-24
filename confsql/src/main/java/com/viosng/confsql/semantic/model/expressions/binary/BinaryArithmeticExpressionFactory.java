package com.viosng.confsql.semantic.model.expressions.binary;

import com.viosng.confsql.semantic.model.expressions.ArithmeticExpression;
import com.viosng.confsql.semantic.model.expressions.Expression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 23:57
 */
public class BinaryArithmeticExpressionFactory {
    
    private BinaryArithmeticExpressionFactory(){}

    public static class BinaryArithmeticExpression extends DefaultBinaryExpression implements ArithmeticExpression {

        BinaryArithmeticExpression(@NotNull String operation,
                                   @NotNull ArithmeticExpression left,
                                   @NotNull ArithmeticExpression right,
                                   @NotNull Type type) {
            super(operation, left, right, type);
        }

    }

    public static ArithmeticExpression plus(@NotNull ArithmeticExpression left, @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpression("+", left, right, Expression.Type.PLUS);
    }

    public static ArithmeticExpression minus(@NotNull ArithmeticExpression left, @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpression("-", left, right, Expression.Type.MINUS);
    }

    public static ArithmeticExpression multiplication(@NotNull ArithmeticExpression left, @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpression("*", left, right, Expression.Type.MULTIPLICATION);
    }

    public static ArithmeticExpression division(@NotNull ArithmeticExpression left, @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpression("/", left, right, Expression.Type.DIVISION);
    }

    public static ArithmeticExpression power(@NotNull ArithmeticExpression left, @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpression("^", left, right, Expression.Type.POWER);
    }
}
