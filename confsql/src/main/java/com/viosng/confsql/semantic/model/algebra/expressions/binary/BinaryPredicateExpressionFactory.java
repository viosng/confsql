package com.viosng.confsql.semantic.model.algebra.expressions.binary;

import com.viosng.confsql.semantic.model.algebra.expressions.ArithmeticExpression;
import com.viosng.confsql.semantic.model.algebra.expressions.Expression;
import com.viosng.confsql.semantic.model.algebra.expressions.PredicateExpression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
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
                                      @NotNull ArithmeticType type,
                                      @NotNull Expression left,
                                      @NotNull Expression right) {
            super(id, type, left, right);
        }

        public BinaryPredicateExpressionImpl(@NotNull ArithmeticType type, @NotNull Expression left, @NotNull Expression right) {
            super(type, left, right);
        }
    }

    public static BinaryPredicateExpression less(@NotNull ArithmeticExpression left,
                                                 @NotNull ArithmeticExpression right) {
        return new BinaryPredicateExpressionImpl(ArithmeticType.LT, left, right);
    }

    public static BinaryPredicateExpression lessOrEqual(@NotNull ArithmeticExpression left,
                                                        @NotNull ArithmeticExpression right) {
        return new BinaryPredicateExpressionImpl(ArithmeticType.LE, left, right);
    }

    public static BinaryPredicateExpression greater(@NotNull ArithmeticExpression left,
                                                    @NotNull ArithmeticExpression right) {
        return new BinaryPredicateExpressionImpl(ArithmeticType.GT, left, right);
    }

    public static BinaryPredicateExpression greaterOrEqual(@NotNull ArithmeticExpression left,
                                                           @NotNull ArithmeticExpression right) {
        return new BinaryPredicateExpressionImpl(ArithmeticType.GE, left, right);
    }

    public static BinaryPredicateExpression equal(@NotNull ArithmeticExpression left,
                                                  @NotNull ArithmeticExpression right) {
        return new BinaryPredicateExpressionImpl(ArithmeticType.EQUAL, left, right);
    }

    public static BinaryPredicateExpression and(@NotNull PredicateExpression left,
                                                @NotNull PredicateExpression right) {
        return new BinaryPredicateExpressionImpl(ArithmeticType.AND, left, right) {
            @NotNull
            @Override
            public Notification verify(@NotNull Context context) {
                return verifyPredicate(context, left, right);
            }
        };
    }

    public static BinaryPredicateExpression or(@NotNull PredicateExpression left,
                                               @NotNull PredicateExpression right) {
        return new BinaryPredicateExpressionImpl(ArithmeticType.OR, left, right) {
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
        return new BinaryPredicateExpressionImpl(id, ArithmeticType.LT, left, right);
    }

    public static BinaryPredicateExpression lessOrEqual(@NotNull ArithmeticExpression left,
                                                        @NotNull ArithmeticExpression right,
                                                        @NotNull String id) {
        return new BinaryPredicateExpressionImpl(id, ArithmeticType.LE, left, right);
    }

    public static BinaryPredicateExpression greater(@NotNull ArithmeticExpression left,
                                                    @NotNull ArithmeticExpression right,
                                                    @NotNull String id) {
        return new BinaryPredicateExpressionImpl(id, ArithmeticType.GT, left, right);
    }

    public static BinaryPredicateExpression greaterOrEqual(@NotNull ArithmeticExpression left,
                                                           @NotNull ArithmeticExpression right,
                                                           @NotNull String id) {
        return new BinaryPredicateExpressionImpl(id, ArithmeticType.GE, left, right);
    }

    public static BinaryPredicateExpression equal(@NotNull ArithmeticExpression left,
                                                  @NotNull ArithmeticExpression right,
                                                  @NotNull String id) {
        return new BinaryPredicateExpressionImpl(id, ArithmeticType.EQUAL, left, right);
    }

    public static BinaryPredicateExpression and(@NotNull PredicateExpression left,
                                                @NotNull PredicateExpression right,
                                                @NotNull String id) {
        return new BinaryPredicateExpressionImpl(id, ArithmeticType.AND, left, right) {
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
        return new BinaryPredicateExpressionImpl(id, ArithmeticType.OR, left, right) {
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
