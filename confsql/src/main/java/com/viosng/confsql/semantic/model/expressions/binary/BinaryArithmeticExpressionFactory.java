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

    public static interface BinaryArithmeticExpression extends ArithmeticExpression, BinaryExpression {}

    private static class BinaryArithmeticExpressionImpl extends DefaultBinaryExpression implements BinaryArithmeticExpression {

        BinaryArithmeticExpressionImpl(@NotNull String id,
                                       @NotNull Type type,
                                       @NotNull ArithmeticExpression left,
                                       @NotNull ArithmeticExpression right) {
            super(id, type, left, right);
        }

        public BinaryArithmeticExpressionImpl(@NotNull Type type, @NotNull Expression left, @NotNull Expression right) {
            super(type, left, right);
        }
    }

    public static BinaryArithmeticExpression plus(@NotNull ArithmeticExpression left,
                                            @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpressionImpl(Expression.Type.PLUS, left, right);
    }

    public static BinaryArithmeticExpression minus(@NotNull ArithmeticExpression left,
                                             @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpressionImpl(Expression.Type.MINUS, left, right);
    }

    public static BinaryArithmeticExpression multiplication(@NotNull ArithmeticExpression left,
                                                      @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpressionImpl(Expression.Type.MULTIPLICATION, left, right);
    }

    public static BinaryArithmeticExpression division(@NotNull ArithmeticExpression left,
                                                @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpressionImpl(Expression.Type.DIVISION, left, right);
    }

    public static BinaryArithmeticExpression power(@NotNull ArithmeticExpression left,
                                             @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpressionImpl(Expression.Type.POWER, left, right);
    }

    public static BinaryArithmeticExpression plus(@NotNull ArithmeticExpression left,
                                            @NotNull ArithmeticExpression right, 
                                            @NotNull String id) {
        return new BinaryArithmeticExpressionImpl(id, Expression.Type.PLUS, left, right);
    }

    public static BinaryArithmeticExpression minus(@NotNull ArithmeticExpression left,
                                             @NotNull ArithmeticExpression right,
                                             @NotNull String id) {
        return new BinaryArithmeticExpressionImpl(id, Expression.Type.MINUS, left, right);
    }

    public static BinaryArithmeticExpression multiplication(@NotNull ArithmeticExpression left,
                                                      @NotNull ArithmeticExpression right,
                                                      @NotNull String id) {
        return new BinaryArithmeticExpressionImpl(id, Expression.Type.MULTIPLICATION, left, right);
    }

    public static BinaryArithmeticExpression division(@NotNull ArithmeticExpression left,
                                                @NotNull ArithmeticExpression right,
                                                @NotNull String id) {
        return new BinaryArithmeticExpressionImpl(id, Expression.Type.DIVISION, left, right);
    }

    public static BinaryArithmeticExpression power(@NotNull ArithmeticExpression left,
                                             @NotNull ArithmeticExpression right,
                                             @NotNull String id) {
        return new BinaryArithmeticExpressionImpl(id, Expression.Type.POWER, left, right);
    }
}
