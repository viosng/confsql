package com.viosng.confsql.semantic.model.expressions.other;

import com.google.common.base.Joiner;
import com.viosng.confsql.semantic.model.expressions.ArithmeticExpression;
import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.PredicateExpression;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 15:14
 */
public class FunctionCallExpression extends AbstractValueExpression implements ArithmeticExpression, PredicateExpression {

    @NotNull
    private List<Expression> arguments;
    
    public FunctionCallExpression(@NotNull String value, Expression... arguments) {
        super(value);
        this.arguments = Arrays.asList(arguments);
    }

    @NotNull
    @Override
    public Type type() {
        return Type.FUNCTION_CALL;
    }

    @Override
    public String toString() {
        return value + "(" + Joiner.on(", ").join(arguments) + ")";
    }
}
