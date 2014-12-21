package com.viosng.confsql.semantic.model.expressions.binary.predicate;

import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.PredicateExpression;
import com.viosng.confsql.semantic.model.expressions.binary.AbstractBinaryExpression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 15:00
 */
public class BinaryPredicateExpression extends AbstractBinaryExpression implements PredicateExpression {

    BinaryPredicateExpression(@NotNull String operation,
                              @NotNull Expression left,
                              @NotNull Expression right,
                              @NotNull Type type) {
        super(operation, left, right, type);
    }

    @NotNull
    @Override
    public Notification verify(@NotNull Context context) {
        return new Notification();
    }

}
