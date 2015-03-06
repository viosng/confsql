package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebraold.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.02.2015
 * Time: 13:03
 */
public class SQLConstant implements SQLExpression {
    @NotNull
    private final String value;

    public SQLConstant(@NotNull String value) {
        this.value = value;
    }

    @NotNull
    public String getValue() {
        return value;
    }

    @Override
    public Expression convert() {
        return ValueExpressionFactory.constant(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLConstant)) return false;

        SQLConstant that = (SQLConstant) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "SQLConstant{" +
                "value='" + value + '\'' +
                '}';
    }
}
