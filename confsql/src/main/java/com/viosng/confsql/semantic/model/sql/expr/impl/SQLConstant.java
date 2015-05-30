package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.02.2015
 * Time: 13:03
 */

@Data
public class SQLConstant implements SQLExpression {
    @NotNull
    private final String value;

    @NotNull
    @Override
    public Expression convert() {
        return ValueExpressionFactory.constant(value);
    }

}
