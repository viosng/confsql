package com.viosng.confsql.semantic.model.expressions.binary.arithmetic;

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
