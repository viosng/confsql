package com.viosng.confsql.semantic.model.expressions;

import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 10:55
 */
public interface UnaryExpression extends Expression {
    @NotNull
    public Expression getArg();
}
