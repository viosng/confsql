package com.viosng.confsql.semantic.model.expressions.unary;

import com.viosng.confsql.semantic.model.expressions.ArithmeticExpression;
import com.viosng.confsql.semantic.model.expressions.Expression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 23:57
 */
public class UnaryArithmeticExpressionFactory {

    private static class UnaryArithmeticExpression extends DefaultUnaryExpression implements ArithmeticExpression {

        UnaryArithmeticExpression(@NotNull String operation,
                                  @NotNull ArithmeticExpression arg,
                                  @NotNull Type type) {
            super(operation, arg, type);
        }

    }

    public static ArithmeticExpression minus(@NotNull ArithmeticExpression arg) {
        return new UnaryArithmeticExpression("-", arg, Expression.Type.UNARY_MINUS);
    }
}