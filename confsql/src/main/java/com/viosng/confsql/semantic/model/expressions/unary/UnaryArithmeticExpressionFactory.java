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

    private UnaryArithmeticExpressionFactory(){}

    public static interface UnaryArithmeticExpression extends ArithmeticExpression, UnaryExpression {}

    public static class UnaryArithmeticExpressionImpl extends DefaultUnaryExpression implements UnaryArithmeticExpression {

        UnaryArithmeticExpressionImpl(@NotNull String id,
                                      @NotNull Type type,
                                      @NotNull ArithmeticExpression arg) {
            super(id, type, arg);
        }
    }

    public static UnaryArithmeticExpression minus(@NotNull ArithmeticExpression arg) {
        return new UnaryArithmeticExpressionImpl(Expression.UNDEFINED_ID, Expression.Type.UNARY_MINUS, arg);
    }

    public static UnaryArithmeticExpression minus(@NotNull ArithmeticExpression arg, @NotNull String id) {
        return new UnaryArithmeticExpressionImpl(id, Expression.Type.UNARY_MINUS, arg);
    }
}
