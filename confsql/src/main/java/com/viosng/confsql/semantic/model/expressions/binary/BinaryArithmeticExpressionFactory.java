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

        BinaryArithmeticExpression(@NotNull String id, 
                                   @NotNull Type type, 
                                   @NotNull ArithmeticExpression left, 
                                   @NotNull ArithmeticExpression right) {
            super(id, type, left, right);
        }

        public BinaryArithmeticExpression(@NotNull Type type, @NotNull Expression left, @NotNull Expression right) {
            super(type, left, right);
        }
    }

    public static ArithmeticExpression plus(@NotNull ArithmeticExpression left,
                                            @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpression(Expression.Type.PLUS, left, right);
    }

    public static ArithmeticExpression minus(@NotNull ArithmeticExpression left,
                                             @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpression(Expression.Type.MINUS, left, right);
    }

    public static ArithmeticExpression multiplication(@NotNull ArithmeticExpression left,
                                                      @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpression(Expression.Type.MULTIPLICATION, left, right);
    }

    public static ArithmeticExpression division(@NotNull ArithmeticExpression left,
                                                @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpression(Expression.Type.DIVISION, left, right);
    }

    public static ArithmeticExpression power(@NotNull ArithmeticExpression left,
                                             @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpression(Expression.Type.POWER, left, right);
    }

    public static ArithmeticExpression plus(@NotNull ArithmeticExpression left,
                                            @NotNull ArithmeticExpression right, 
                                            @NotNull String id) {
        return new BinaryArithmeticExpression(id, Expression.Type.PLUS, left, right);
    }

    public static ArithmeticExpression minus(@NotNull ArithmeticExpression left,
                                             @NotNull ArithmeticExpression right,
                                             @NotNull String id) {
        return new BinaryArithmeticExpression(id, Expression.Type.MINUS, left, right);
    }

    public static ArithmeticExpression multiplication(@NotNull ArithmeticExpression left,
                                                      @NotNull ArithmeticExpression right,
                                                      @NotNull String id) {
        return new BinaryArithmeticExpression(id, Expression.Type.MULTIPLICATION, left, right);
    }

    public static ArithmeticExpression division(@NotNull ArithmeticExpression left,
                                                @NotNull ArithmeticExpression right,
                                                @NotNull String id) {
        return new BinaryArithmeticExpression(id, Expression.Type.DIVISION, left, right);
    }

    public static ArithmeticExpression power(@NotNull ArithmeticExpression left,
                                             @NotNull ArithmeticExpression right,
                                             @NotNull String id) {
        return new BinaryArithmeticExpression(id, Expression.Type.POWER, left, right);
    }
}
