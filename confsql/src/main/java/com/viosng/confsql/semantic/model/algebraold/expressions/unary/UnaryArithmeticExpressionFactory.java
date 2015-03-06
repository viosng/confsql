package com.viosng.confsql.semantic.model.algebraold.expressions.unary;

import com.viosng.confsql.semantic.model.algebraold.expressions.ArithmeticExpression;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
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
                                      @NotNull ArithmeticType type,
                                      @NotNull ArithmeticExpression arg) {
            super(id, type, arg);
        }
    }

    public static UnaryArithmeticExpression minus(@NotNull ArithmeticExpression arg) {
        return new UnaryArithmeticExpressionImpl(Expression.UNDEFINED_ID, ArithmeticType.MINUS, arg);
    }

    public static UnaryArithmeticExpression minus(@NotNull ArithmeticExpression arg, @NotNull String id) {
        return new UnaryArithmeticExpressionImpl(id, ArithmeticType.MINUS, arg);
    }
}
