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
public class SQLParameterList implements SQLExpression {

    @NotNull
    private final List<SQLParameter> parameterList;

    public SQLParameterList(@NotNull List<SQLParameter> parameterList) {
        this.parameterList = parameterList;
    }

    @NotNull
    public List<SQLParameter> getParameterList() {
        return parameterList;
    }

    @Override
    public Expression convert() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLParameterList)) return false;

        SQLParameterList that = (SQLParameterList) o;

        return parameterList.equals(that.parameterList);
    }

    @Override
    public int hashCode() {
        return parameterList.hashCode();
    }

    @Override
    public String toString() {
        return "SQLParameterList{" +
                "parameterList=" + parameterList +
                '}';
    }
}
