package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.google.common.base.Splitter;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 28.02.2015
 * Time: 13:17
 */
@Data
public class SQLField implements SQLExpression {
    @NotNull
    private final String name;

    @Override
    @NotNull
    public Expression convert() {
        return ValueExpressionFactory.attribute(Splitter.on('.').splitToList(name));
    }

}
