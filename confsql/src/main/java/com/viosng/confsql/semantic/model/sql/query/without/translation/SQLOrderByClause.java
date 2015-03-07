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
    private final List<SQLOrderByArg> orderByArgs;

    public SQLOrderByClause(@NotNull List<SQLParameter> paramList, @NotNull List<SQLOrderByArg> orderByArgs) {
        this.paramList = paramList;
        this.orderByArgs = orderByArgs;
    }

    @NotNull
    public List<SQLParameter> getParamList() {
        return paramList;
    }

    @NotNull
    public List<SQLOrderByArg> getOrderByArgs() {
        return orderByArgs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLOrderByClause)) return false;

        SQLOrderByClause that = (SQLOrderByClause) o;

        return orderByArgs.equals(that.orderByArgs) && paramList.equals(that.paramList);
    }

    @Override
    public int hashCode() {
        int result = paramList.hashCode();
        result = 31 * result + orderByArgs.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLOrderByClause{" +
                "paramList=" + paramList +
                ", orderByArgs=" + orderByArgs +
                '}';
    }
}
