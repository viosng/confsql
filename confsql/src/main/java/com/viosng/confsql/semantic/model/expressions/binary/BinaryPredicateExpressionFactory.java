package com.viosng.confsql.semantic.model.expressions.binary;

import com.viosng.confsql.semantic.model.expressions.ArithmeticExpression;
import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.PredicateExpression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 23:39
 */
public class BinaryPredicateExpressionFactory {

    private BinaryPredicateExpressionFactory() {}

    public static interface BinaryPredicateExpression extends PredicateExpression, BinaryExpression {}
    
    private static class BinaryPredicateExpressionImpl extends DefaultBinaryExpression implements BinaryPredicateExpression {

        BinaryPredicateExpressionImpl(@NotNull String id,
                                      @NotNull Type type,
                                      @NotNull Expression left,
                                      @NotNull Expression right) {
            super(id, type, left, right);
        }

        public BinaryPredicateExpressionImpl(@NotNull Type type, @NotNull Expression left, @NotNull Expression right) {
            super(type, left, right);
        }
    }

    public static BinaryPredicateExpression less(@NotNull ArithmeticExpression left,
                                                 @NotNull ArithmeticExpression right) {
        return new BinaryPredicateExpressionImpl(Expression.Type.LESS, left, right);
    }

    public static BinaryPredicateExpression lessOrEqual(@NotNull ArithmeticExpression left,
                                                        @NotNull ArithmeticExpression right) {
        return new BinaryPredicateExpressionImpl(Expression.Type.LESS_OR_EQUAL, left, right);
    }

    public static BinaryPredicateExpression greater(@NotNull ArithmeticExpression left,
                                                    @NotNull ArithmeticExpression right) {
        return new BinaryPredicateExpressionImpl(Expression.Type.GREATER, left, right);
    }

    public static BinaryPredicateExpression greaterOrEqual(@NotNull ArithmeticExpression left,
                                                           @NotNull ArithmeticExpression right) {
        return new BinaryPredicateExpressionImpl(Expression.Type.GREATER_OR_EQUAL, left, right);
    }

    public static BinaryPredicateExpression equal(@NotNull ArithmeticExpression left,
                                                  @NotNull ArithmeticExpression right) {
        return new BinaryPredicateExpressionImpl(Expression.Type.EQUAL, left, right);
    }

    public static BinaryPredicateExpression and(@NotNull PredicateExpression left,
                                                @NotNull PredicateExpression right) {
        return new BinaryPredicateExpressionImpl(Expression.Type.AND, left, right) {
            @NotNull
            @Override
            public Notification verify(@NotNull Context context) {
                return verifyPredicate(context, left, right);
            }
        };
    }

    public static BinaryPredicateExpression or(@NotNull PredicateExpression left,
                                               @NotNull PredicateExpression right) {
        return new BinaryPredicateExpressionImpl(Expression.Type.OR, left, right) {
            @NotNull
            @Override
            public Notification verify(@NotNull Context context) {
                return verifyPredicate(context, left, right);
            }
        };
    }

    public static BinaryPredicateExpression less(@NotNull ArithmeticExpression left,
                                                 @NotNull ArithmeticExpression right,
                                                 @NotNull String id) {
        return new BinaryPredicateExpressionImpl(id, Expression.Type.LESS, left, right);
    }

    public static BinaryPredicateExpression lessOrEqual(@NotNull ArithmeticExpression left,
                                                        @NotNull ArithmeticExpression right,
                                                        @NotNull String id) {
        return new BinaryPredicateExpressionImpl(id, Expression.Type.LESS_OR_EQUAL, left, right);
    }

    public static BinaryPredicateExpression greater(@NotNull ArithmeticExpression left,
                                                    @NotNull ArithmeticExpression right,
                                                    @NotNull String id) {
        return new BinaryPredicateExpressionImpl(id, Expression.Type.GREATER, left, right);
    }

    public static BinaryPredicateExpression greaterOrEqual(@NotNull ArithmeticExpression left,
                                                           @NotNull ArithmeticExpression right,
                                                           @NotNull String id) {
        return new BinaryPredicateExpressionImpl(id, Expression.Type.GREATER_OR_EQUAL, left, right);
    }

    public static BinaryPredicateExpression equal(@NotNull ArithmeticExpression left,
                                                  @NotNull ArithmeticExpression right,
                                                  @NotNull String id) {
        return new BinaryPredicateExpressionImpl(id, Expression.Type.EQUAL, left, right);
    }

    public static BinaryPredicateExpression and(@NotNull PredicateExpression left,
                                                @NotNull PredicateExpression right,
                                                @NotNull String id) {
        return new BinaryPredicateExpressionImpl(id, Expression.Type.AND, left, right) {
            @NotNull
            @Override
            public Notification verify(@NotNull Context context) {
                return verifyPredicate(context, left, right);
            }
        };
    }

    public static BinaryPredicateExpression or(@NotNull PredicateExpression left,
                                               @NotNull PredicateExpression right,
                                               @NotNull String id) {
        return new BinaryPredicateExpressionImpl(id, Expression.Type.OR, left, right) {
            @NotNull
            @Override
            public Notification verify(@NotNull Context context) {
                return verifyPredicate(context, left, right);
            }
        };
    }

    private static Notification verifyPredicate(@NotNull Context context,
                                                @NotNull Expression left,
                                                @NotNull Expression right) {
        Notification notification = new Notification();
        notification.addNotification(left.verify(context));
        notification.addNotification(right.verify(context));
        if (PredicateExpression.isInvalidBooleanConstant(left) || PredicateExpression.isInvalidBooleanConstant(right)) {
            notification.error("Predicate arguments aren't boolean");
        }
        return notification;
    }

}
