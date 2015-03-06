package com.viosng.confsql.semantic.model.sql.query.without.translation;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLParameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 01.03.2015
 * Time: 1:54
 */
public class SQLOrderByClause implements SQLExpression{
    @NotNull
    private final List<SQLParameter> paramList;
    
    @NotNull
    private final List<SQLExpression> expressionList;
    
    @NotNull
    private final String orderType;

    public SQLOrderByClause(@NotNull List<SQLParameter> paramList, @NotNull List<SQLExpression> expressionList, @NotNull String orderType) {
        this.paramList = paramList;
        this.expressionList = expressionList;
        this.orderType = orderType.toUpperCase();
    }

    @NotNull
    public List<SQLParameter> getParamList() {
        return paramList;
    }

    @NotNull
    public List<SQLExpression> getExpressionList() {
        return expressionList;
    }

    @NotNull
    public String getOrderType() {
        return orderType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLOrderByClause)) return false;

        SQLOrderByClause that = (SQLOrderByClause) o;

        return expressionList.equals(that.expressionList) && orderType.equals(that.orderType) && paramList.equals(that.paramList);
    }

    @Override
    public int hashCode() {
        int result = paramList.hashCode();
        result = 31 * result + expressionList.hashCode();
        result = 31 * result + orderType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLOrderByClause{" +
                "paramList=" + paramList +
                ", expressionList=" + expressionList +
                ", orderType='" + orderType + '\'' +
                '}';
    }
}
