package com.viosng.confsql.semantic.model.expressions.binary.arithmetic;

import com.viosng.confsql.semantic.model.expressions.ArithmeticExpression;
import com.viosng.confsql.semantic.model.expressions.binary.AbstractBinaryExpression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 12:24
 */
public class BinaryArithmeticExpression extends AbstractBinaryExpression implements ArithmeticExpression {

    BinaryArithmeticExpression(@NotNull String operation,
                               @NotNull ArithmeticExpression left,
                               @NotNull ArithmeticExpression right,
                               @NotNull Type type) {
        super(operation, left, right, type);
    }

    @NotNull
    @Override
    public Notification verify(@NotNull Context context) {
        return new Notification();
    }
}
