package com.viosng.confsql.semantic.model.expressions.unary;

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
public class UnaryPredicateExpressionFactory {

    private UnaryPredicateExpressionFactory(){}

    public static class UnaryPredicateExpression extends DefaultUnaryExpression implements PredicateExpression {

        UnaryPredicateExpression(@NotNull String id,
                                 @NotNull Type type, @NotNull Expression arg) {
            super(id, type, arg);
        }

    }

    public static UnaryPredicateExpression not(@NotNull PredicateExpression arg, @NotNull String id) {
        return new UnaryPredicateExpression(id, Expression.Type.NOT, arg) {
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
