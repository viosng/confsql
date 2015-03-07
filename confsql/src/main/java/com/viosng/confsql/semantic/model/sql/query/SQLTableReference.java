package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.queries.QueryBuilder;
import com.viosng.confsql.semantic.model.algebraold.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.query.without.translation.SQLJoinedTablePrimary;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 01.03.2015
 * Time: 21:31
 */
public class SQLTableReference implements SQLExpression {
    
    @NotNull
    private final SQLTablePrimary tablePrimary;
    
    @NotNull
    private final List<SQLJoinedTablePrimary> joinedTablePrimaryList;

    public SQLTableReference(@NotNull SQLTablePrimary tablePrimary, @NotNull List<SQLJoinedTablePrimary> joinedTablePrimaryList) {
        this.joinedTablePrimaryList = joinedTablePrimaryList;
        this.tablePrimary = tablePrimary;
    }

    @NotNull
    public SQLTablePrimary getTablePrimary() {
        return tablePrimary;
    }

    @NotNull
    public List<SQLJoinedTablePrimary> getJoinedTablePrimaryList() {
        return joinedTablePrimaryList;
    }

    @Override
    public Expression convert() {
        if (joinedTablePrimaryList.isEmpty()) {
            return tablePrimary.convert();
        }
        return joinedTablePrimaryList.stream().reduce((Query) tablePrimary.convert(), new BiFunction<Query, SQLJoinedTablePrimary, Query>() {
            @Override
            public Query apply(Query query, SQLJoinedTablePrimary sqlJoinedTablePrimary) {
                List<Parameter> parameters = new ArrayList<>();
                parameters.add(new Parameter("onCondition", sqlJoinedTablePrimary.getOnCondition().convert()));
                parameters.add(new Parameter("joinType", ValueExpressionFactory.constant(sqlJoinedTablePrimary.getJoinType())));
                parameters.addAll(sqlJoinedTablePrimary.getParameterList().stream().map(p ->
                        (Parameter)p.convert()).collect(Collectors.toList()));
                return new QueryBuilder()
                        .queryType(Query.QueryType.JOIN)
                        .parameters(parameters)
                        .subQueries(query, (Query)sqlJoinedTablePrimary.getTablePrimary().convert())
                        .create();
            }
        }, (query, query2) -> null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLTableReference)) return false;

        SQLTableReference that = (SQLTableReference) o;

        return joinedTablePrimaryList.equals(that.joinedTablePrimaryList) && tablePrimary.equals(that.tablePrimary);
    }

    @Override
    public int hashCode() {
        int result = tablePrimary.hashCode();
        result = 31 * result + joinedTablePrimaryList.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLTableReference{" +
                "tablePrimary=" + tablePrimary +
                ", joinedTablePrimaryList=" + joinedTablePrimaryList +
                '}';
    }
}
