package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebraold.expressions.other.ValueExpressionFactory;
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
    public Expression convert() {
        int index = name.lastIndexOf('.');
        return index == -1 
                ? ValueExpressionFactory.attribute("", name) 
                : ValueExpressionFactory.attribute(name.substring(0, index), name.substring(index + 1));
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
