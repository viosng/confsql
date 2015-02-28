package com.viosng.confsql.semantic.model.sql.impl;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 24.02.2015
 * Time: 1:30
 */
public class SQLExpressionList implements SQLExpression {
    
    @NotNull
    private final List<SQLExpression> sqlExpressions;

    public SQLExpressionList(@NotNull List<SQLExpression> sqlExpressions) {
        this.sqlExpressions = sqlExpressions;
    }

    @NotNull
    public List<SQLExpression> getSqlExpressions() {
        return sqlExpressions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLExpressionList)) return false;

        SQLExpressionList that = (SQLExpressionList) o;

        return sqlExpressions.equals(that.sqlExpressions);
    }

    @Override
    public int hashCode() {
        return sqlExpressions.hashCode();
    }

    @Override
    public String toString() {
        return "SQLExpressionList{" +
                "sqlExpressions=" + sqlExpressions +
                '}';
    }
}
