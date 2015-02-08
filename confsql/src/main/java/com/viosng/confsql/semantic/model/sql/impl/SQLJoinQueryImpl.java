package com.viosng.confsql.semantic.model.sql.impl;

import com.viosng.confsql.semantic.model.algebra.Query;
import com.viosng.confsql.semantic.model.algebra.QueryBuilder;
import com.viosng.confsql.semantic.model.expressions.PredicateExpression;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.sql.SQLJoinQuery;
import com.viosng.confsql.semantic.model.sql.SQLQuery;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 06.02.2015
 * Time: 20:16
 */
public class SQLJoinQueryImpl extends AbstractSQLSingleQuery implements SQLJoinQuery {
    
    @NotNull
    private final SQLQuery leftQuery;
    
    @Nullable
    private final PredicateExpression joinCondition;
    
    @Nullable
    private final SQLJoinQuery rightQuery;

    public SQLJoinQueryImpl(@Nullable String alias, 
                            @NotNull List<Parameter> parameters, 
                            @NotNull SQLQuery leftQuery,
                            @Nullable PredicateExpression joinCondition,
                            @Nullable SQLJoinQuery rightQuery) {
        super(alias, parameters);
        this.leftQuery = leftQuery;
        this.joinCondition = joinCondition;
        this.rightQuery = rightQuery;
    }

    public SQLJoinQueryImpl(@NotNull List<Parameter> parameters, 
                            @NotNull SQLQuery leftQuery, 
                            @Nullable SQLJoinQuery rightQuery) {
        super(null, parameters);
        this.leftQuery = leftQuery;
        this.rightQuery = rightQuery;
        this.joinCondition = null;
    }

    @NotNull
    @Override
    public SQLQuery getLeft() {
        return leftQuery;
    }

    @Nullable
    @Override
    public PredicateExpression joinCondition() {
        return joinCondition;
    }

    @Nullable
    @Override
    public SQLJoinQuery getRight() {
        return rightQuery;
    }

    @NotNull
    @Override
    public Query convertToQuery() {
        if (rightQuery == null) return leftQuery.convertToQuery();
        return new QueryBuilder()
                .queryType(Query.QueryType.JOIN)
                .parameters(parameters())
                .id(alias())
                .argumentExpressions(joinCondition)
                .subQueries(leftQuery.convertToQuery(), rightQuery.convertToQuery())
                .create();
    }
}
