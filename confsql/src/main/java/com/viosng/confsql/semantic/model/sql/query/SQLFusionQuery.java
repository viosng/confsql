package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.queries.QueryBuilder;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLParameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 09.03.2015
 * Time: 2:13
 */
public class SQLFusionQuery implements SQLExpression {

    @NotNull
    private final List<SQLParameter> parameterList;

    @NotNull
    private final List<SQLExpression> queryList;

    public SQLFusionQuery(@NotNull List<SQLParameter> parameterList, @NotNull List<SQLExpression> queryList) {
        this.parameterList = parameterList;
        this.queryList = queryList;
    }

    @Override
    public Expression convert() {
        return new QueryBuilder()
                .queryType(Query.QueryType.FUSION)
                .parameters(parameterList.stream().map(p -> (Parameter) p.convert()).collect(Collectors.toList()))
                .subQueries(queryList.stream().map(q -> (Query)q.convert()).collect(Collectors.toList()))
                .create();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLFusionQuery)) return false;

        SQLFusionQuery that = (SQLFusionQuery) o;

        return parameterList.equals(that.parameterList) && queryList.equals(that.queryList);
    }

    @Override
    public int hashCode() {
        int result = parameterList.hashCode();
        result = 31 * result + queryList.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLFusionQuery{" +
                "parameterList=" + parameterList +
                ", queryList=" + queryList +
                '}';
    }
}
