package com.viosng.confsql.semantic.model.expressions.binary;

import com.viosng.confsql.semantic.model.expressions.Expression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 10:53
 */
public interface BinaryExpression extends Expression {
    @NotNull
    public Expression getLeftArg();
    @NotNull
    public Expression getRightArg();
}
