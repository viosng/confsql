package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.google.common.base.Splitter;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 28.02.2015
 * Time: 13:17
 */
public class SQLField implements SQLExpression {
    @NotNull
    private final String name;

    public SQLField(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @Override
    @NotNull
    public Expression convert() {
        return ValueExpressionFactory.attribute(Splitter.on('.').splitToList(name));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLField)) return false;

        SQLField sqlField = (SQLField) o;

        return name.equals(sqlField.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "SQLField{" +
                "name='" + name + '\'' +
                '}';
    }
}
