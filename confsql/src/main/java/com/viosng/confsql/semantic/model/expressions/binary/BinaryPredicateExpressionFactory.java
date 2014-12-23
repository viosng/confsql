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

    private static class BinaryPredicateExpression extends DefaultBinaryExpression implements PredicateExpression {

        BinaryPredicateExpression(@NotNull String operation,
                                  @NotNull Expression left,
                                  @NotNull Expression right,
                                  @NotNull Type type) {
            super(operation, left, right, type);
        }

    }
    
    public static PredicateExpression less(@NotNull ArithmeticExpression left, @NotNull ArithmeticExpression right) {
        return new BinaryPredicateExpression("<", left, right, Expression.Type.LESS);
    }

    public static PredicateExpression lessOrEqual(@NotNull ArithmeticExpression left, @NotNull ArithmeticExpression right) {
        return new BinaryPredicateExpression("<=", left, right, Expression.Type.LESS_OR_EQUAL);
    }

    public static PredicateExpression greater(@NotNull ArithmeticExpression left, @NotNull ArithmeticExpression right) {
        return new BinaryPredicateExpression(">", left, right, Expression.Type.GREATER);
    }

    public static PredicateExpression greaterOrEqual(@NotNull ArithmeticExpression left, @NotNull ArithmeticExpression right) {
        return new BinaryPredicateExpression(">=", left, right, Expression.Type.GREATER_OR_EQUAL);
    }

    public static PredicateExpression equal(@NotNull ArithmeticExpression left, @NotNull ArithmeticExpression right) {
        return new BinaryPredicateExpression("==", left, right, Expression.Type.EQUAL);
    }

    public static PredicateExpression and(@NotNull PredicateExpression left, @NotNull PredicateExpression right) {
        return new BinaryPredicateExpression("and", left, right, Expression.Type.AND) {
            @NotNull
            @Override
            public Notification verify(@NotNull Context context) {
                return verifyPredicate(left, right);
            }
        };
    }

    public static PredicateExpression or(@NotNull PredicateExpression left, @NotNull PredicateExpression right) {
        return new BinaryPredicateExpression("or", left, right, Expression.Type.OR) {
            @NotNull
            @Override
            public Notification verify(@NotNull Context context) {
                return verifyPredicate(left, right);
            }
        };
    }

    private static Notification verifyPredicate(@NotNull Expression left, @NotNull Expression right) {
        Notification notification = new Notification();
        if (PredicateExpression.isInvalidBooleanConstant(left) || PredicateExpression.isInvalidBooleanConstant(right)) {
            notification.error("Predicate arguments aren't boolean");
        }
        return notification;
    }

}
