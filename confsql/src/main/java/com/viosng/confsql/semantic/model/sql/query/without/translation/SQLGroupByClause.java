package com.viosng.confsql.semantic.model.sql.query.without.translation;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLParameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 01.03.2015
 * Time: 2:40
 */
public class SQLGroupByClause implements SQLExpression {
    @NotNull
    private final List<SQLParameter> parameterList;
    
    @NotNull
    private final List<SQLExpression> expressionList;

    public SQLGroupByClause(@NotNull List<SQLParameter> parameterList, @NotNull List<SQLExpression> expressionList) {
        this.parameterList = parameterList;
        this.expressionList = expressionList;
    }

    @NotNull
    public List<SQLParameter> getParameterList() {
        return parameterList;
    }

    @NotNull
    public List<SQLExpression> getExpressionList() {
        return expressionList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLGroupByClause)) return false;

        SQLGroupByClause that = (SQLGroupByClause) o;

        return expressionList.equals(that.expressionList) && parameterList.equals(that.parameterList);
    }

    @Override
    public int hashCode() {
        int result = parameterList.hashCode();
        result = 31 * result + expressionList.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLGroupByClause{" +
                "parameterList=" + parameterList +
                ", expressionList=" + expressionList +
                '}';
    }
}
