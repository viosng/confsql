package com.viosng.confsql.semantic.model.sql.impl;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 28.02.2015
 * Time: 13:20
 */
public class SQLExpressionsAndParamsList implements SQLExpression{
    @NotNull
    private final SQLExpressionList expressions, params;

    public SQLExpressionsAndParamsList(@NotNull SQLExpressionList expressions, @NotNull SQLExpressionList params) {
        this.expressions = expressions;
        this.params = params;
    }

    @NotNull
    public SQLExpressionList getExpressions() {
        return expressions;
    }

    @NotNull
    public SQLExpressionList getParams() {
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLExpressionsAndParamsList)) return false;

        SQLExpressionsAndParamsList that = (SQLExpressionsAndParamsList) o;

        return expressions.equals(that.expressions) && params.equals(that.params);
    }

    @Override
    public int hashCode() {
        int result = expressions.hashCode();
        result = 31 * result + params.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLExpressionsAndParamsList{" +
                "expressions=" + expressions +
                ", params=" + params +
                '}';
    }
}
