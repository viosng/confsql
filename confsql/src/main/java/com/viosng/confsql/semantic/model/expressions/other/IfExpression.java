package com.viosng.confsql.semantic.model.expressions.other;

import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.PredicateExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 31.01.2015
 * Time: 16:50
 */
public interface IfExpression extends Expression {
    
    @Override
    @NotNull
    default Type type() {
        return Type.IF;
    }

    @NotNull
    PredicateExpression getPredicate();

    @NotNull
    Expression getExpressionOnTrue();

    @NotNull
    Expression getExpressionOnFalse();
}
