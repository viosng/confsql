package com.viosng.confsql.semantic.model.algebra.expressions.binary;

import com.viosng.confsql.semantic.model.algebra.expressions.ArithmeticExpression;
import com.viosng.confsql.semantic.model.algebra.expressions.Expression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
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
                                       @NotNull ArithmeticType type,
                                       @NotNull ArithmeticExpression left,
                                       @NotNull ArithmeticExpression right) {
            super(id, type, left, right);
        }

        public BinaryArithmeticExpressionImpl(@NotNull ArithmeticType type, @NotNull Expression left, @NotNull Expression right) {
            super(type, left, right);
        }
    }

    public static BinaryArithmeticExpression plus(@NotNull ArithmeticExpression left,
                                            @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpressionImpl(ArithmeticType.PLUS, left, right);
    }

    public static BinaryArithmeticExpression minus(@NotNull ArithmeticExpression left,
                                             @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpressionImpl(ArithmeticType.MINUS, left, right);
    }

    public static BinaryArithmeticExpression multiplication(@NotNull ArithmeticExpression left,
                                                      @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpressionImpl(ArithmeticType.MULTIPLY, left, right);
    }

    public static BinaryArithmeticExpression division(@NotNull ArithmeticExpression left,
                                                @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpressionImpl(ArithmeticType.DIVIDE, left, right);
    }

    public static BinaryArithmeticExpression power(@NotNull ArithmeticExpression left,
                                             @NotNull ArithmeticExpression right) {
        return new BinaryArithmeticExpressionImpl(ArithmeticType.POWER, left, right);
    }

    public static BinaryArithmeticExpression plus(@NotNull ArithmeticExpression left,
                                            @NotNull ArithmeticExpression right, 
                                            @NotNull String id) {
        return new BinaryArithmeticExpressionImpl(id, ArithmeticType.PLUS, left, right);
    }

    public static BinaryArithmeticExpression minus(@NotNull ArithmeticExpression left,
                                             @NotNull ArithmeticExpression right,
                                             @NotNull String id) {
        return new BinaryArithmeticExpressionImpl(id, ArithmeticType.MINUS, left, right);
    }

    public static BinaryArithmeticExpression multiplication(@NotNull ArithmeticExpression left,
                                                      @NotNull ArithmeticExpression right,
                                                      @NotNull String id) {
        return new BinaryArithmeticExpressionImpl(id, ArithmeticType.MULTIPLY, left, right);
    }

    public static BinaryArithmeticExpression division(@NotNull ArithmeticExpression left,
                                                @NotNull ArithmeticExpression right,
                                                @NotNull String id) {
        return new BinaryArithmeticExpressionImpl(id, ArithmeticType.DIVIDE, left, right);
    }

    public static BinaryArithmeticExpression power(@NotNull ArithmeticExpression left,
                                             @NotNull ArithmeticExpression right,
                                             @NotNull String id) {
        return new BinaryArithmeticExpressionImpl(id, ArithmeticType.POWER, left, right);
    }
}
