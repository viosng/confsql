package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.viosng.confsql.semantic.model.algebra.Expression;
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
    private final List<SQLExpression> expressionList;

    public SQLExpressionList(@NotNull List<SQLExpression> expressionList) {
        this.expressionList = expressionList;
    }

    @NotNull
    public List<SQLExpression> getExpressionList() {
        return expressionList;
    }

    @NotNull
    @Override
    public Expression convert() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLExpressionList)) return false;

        SQLExpressionList that = (SQLExpressionList) o;

        return expressionList.equals(that.expressionList);
    }

    @Override
    public int hashCode() {
        return expressionList.hashCode();
    }

    @Override
    public String toString() {
        return "SQLExpressionList{" +
                "expressionList=" + expressionList +
                '}';
    }
}
