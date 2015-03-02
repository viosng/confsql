package com.viosng.confsql.semantic.model.algebra.expressions.unary;

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
public class UnaryPredicateExpressionFactory {

    private UnaryPredicateExpressionFactory(){}

    public static interface UnaryPredicateExpression extends PredicateExpression, UnaryExpression {}

    public static class UnaryPredicateExpressionImpl extends DefaultUnaryExpression implements UnaryPredicateExpression {

        UnaryPredicateExpressionImpl(@NotNull String id,
                                     @NotNull ArithmeticType type, @NotNull Expression arg) {
            super(id, type, arg);
        }

    }

    public static UnaryPredicateExpression not(@NotNull PredicateExpression arg, @NotNull String id) {
        return new UnaryPredicateExpressionImpl(id, ArithmeticType.NOT, arg) {
            @NotNull
            @Override
            public Notification verify(@NotNull Context context) {
                Notification notification = new Notification();
                notification.addNotification(arg.verify(context));
                if (PredicateExpression.isInvalidBooleanConstant(arg)) {
                    notification.error("Predicate argument isn't boolean");
                }
                return notification;
            }
        };
    }

    public static UnaryPredicateExpression not(@NotNull PredicateExpression arg) {
        return not(arg, Expression.UNDEFINED_ID);
    }
    
}
