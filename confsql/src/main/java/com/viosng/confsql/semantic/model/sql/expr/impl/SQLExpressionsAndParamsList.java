package com.viosng.confsql.semantic.model.sql.expr.impl;

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
    private final SQLExpressionList expressionList;

    @NotNull
    private final SQLParameterList parameterList;

    public SQLExpressionsAndParamsList(@NotNull SQLExpressionList expressionList, @NotNull SQLParameterList parameterList) {
        this.expressionList = expressionList;
        this.parameterList = parameterList;
    }

    @NotNull
    public SQLExpressionList getExpressionList() {
        return expressionList;
    }

    @NotNull
    public SQLParameterList getParameterList() {
        return parameterList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLExpressionsAndParamsList)) return false;

        SQLExpressionsAndParamsList that = (SQLExpressionsAndParamsList) o;

        return expressionList.equals(that.expressionList) && parameterList.equals(that.parameterList);
    }

    @Override
    public int hashCode() {
        int result = expressionList.hashCode();
        result = 31 * result + parameterList.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLExpressionsAndParamsList{" +
                "expressionList=" + expressionList +
                ", parameterList=" + parameterList +
                '}';
    }
}
