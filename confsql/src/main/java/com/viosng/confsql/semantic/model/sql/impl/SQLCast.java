package com.viosng.confsql.semantic.model.sql.impl;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 28.02.2015
 * Time: 17:34
 */
public class SQLCast implements SQLExpression{
    
    @NotNull
    private final SQLExpression expression;
    
    @NotNull
    private final String type;

    public SQLCast(@NotNull SQLExpression expression, @NotNull String type) {
        this.expression = expression;
        this.type = type;
    }

    @NotNull
    public SQLExpression getExpression() {
        return expression;
    }

    @NotNull
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLCast)) return false;

        SQLCast sqlCast = (SQLCast) o;

        return expression.equals(sqlCast.expression) && type.equals(sqlCast.type);
    }

    @Override
    public int hashCode() {
        int result = expression.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLCast{" +
                "expression=" + expression +
                ", type='" + type + '\'' +
                '}';
    }
}
