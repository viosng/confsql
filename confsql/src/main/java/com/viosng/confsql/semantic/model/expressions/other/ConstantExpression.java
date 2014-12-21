package com.viosng.confsql.semantic.model.expressions.other;

import com.viosng.confsql.semantic.model.expressions.ArithmeticExpression;
import com.viosng.confsql.semantic.model.expressions.PredicateExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 15:11
 */
public class ConstantExpression extends AbstractValueExpression implements ArithmeticExpression, PredicateExpression {

    public ConstantExpression(@NotNull String value) {
        super(value);
    }

    @NotNull
    @Override
    public Type type() {
        return Type.CONSTANT;
    }
}
