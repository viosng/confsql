package com.viosng.confsql.semantic.model.algebraold.expressions.unary;

import com.viosng.confsql.semantic.model.algebra.Expression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 23.12.2014
 * Time: 19:17
 */
public interface UnaryExpression extends Expression {
    @NotNull
    public Expression getArg();
}
