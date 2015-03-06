package com.viosng.confsql.semantic.model.algebraold.expressions.other;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebraold.expressions.PredicateExpression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
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
    default ArithmeticType type() {
        return ArithmeticType.IF;
    }

    @NotNull
    PredicateExpression getPredicate();

    @NotNull
    Expression getExpressionOnTrue();

    @NotNull
    Expression getExpressionOnFalse();
}
