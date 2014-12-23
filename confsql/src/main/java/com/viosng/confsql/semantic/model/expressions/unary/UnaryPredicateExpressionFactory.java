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

    private static class UnaryPredicateExpression extends DefaultUnaryExpression implements PredicateExpression {

        UnaryPredicateExpression(@NotNull String operation,
                                 @NotNull Expression arg,
                                 @NotNull Type type) {
            super(operation, arg, type);
        }

    }

    public static PredicateExpression not(@NotNull PredicateExpression arg) {
        return new UnaryPredicateExpression("not", arg, Expression.Type.NOT) {
            @NotNull
            @Override
            public Notification verify(@NotNull Context context) {
                Notification notification = new Notification();
                if (PredicateExpression.isInvalidBooleanConstant(arg)) {
                    notification.error("Predicate argument isn't boolean");
                }
                return notification;
            }
        };
    }

}
