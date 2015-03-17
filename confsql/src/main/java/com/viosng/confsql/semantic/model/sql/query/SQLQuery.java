package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.queries.QueryBuilder;
import com.viosng.confsql.semantic.model.algebra.queries.QueryFactory;
import com.viosng.confsql.semantic.model.algebra.special.expr.Parameter;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLParameter;
import com.viosng.confsql.semantic.model.sql.query.without.translation.SQLTableExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 02.03.2015
 * Time: 3:35
 */
public class SQLQuery implements SQLExpression {

    @NotNull
    private final List<SQLParameter> parameterList;

    @NotNull
    private final List<SQLSelectItem> selectItemList;
    
    @Nullable
    private final SQLTableExpression tableExpression;

    public SQLQuery(@NotNull List<SQLSelectItem> selectItemList,
                    @Nullable SQLTableExpression tableExpression,
                    @NotNull List<SQLParameter> parameterList) {
        this.selectItemList = selectItemList;
        this.tableExpression = tableExpression;
        this.parameterList = parameterList;
    }


    private List<Parameter> mergeExpressionsAndParameters(List<? extends SQLExpression> sqlExpressions,
                                                          List<SQLParameter> sqlParameters,
                                                          String expressionPrefix) {
        List<Parameter> parameters = new ArrayList<>();
        for (int i = 0; i < sqlExpressions.size(); i++) {
            parameters.add(new Parameter(expressionPrefix+ i, sqlExpressions.get(i).convert()));
        }

        parameters.addAll(sqlParameters.stream().map(p -> (Parameter)p.convert()).collect(Collectors.toList()));

        return parameters;
    }


    @Override
    @NotNull
    public Expression convert() {
        Query current;
        if (tableExpression != null) {
            current = (Query) tableExpression.getFromClause().convert();
            if (tableExpression.getWhereClause() != null) {
                current = new QueryBuilder()
                        .queryType(Query.QueryType.FILTER)
                        .subQueries(current)
                        .parameters(new Parameter("filterExpression", tableExpression.getWhereClause().convert()))
                        .create();
            }
            if (tableExpression.getGroupByClause() != null) {
                current = new QueryBuilder()
                        .queryType(Query.QueryType.AGGREGATION)
                        .subQueries(current)
                        .parameters(mergeExpressionsAndParameters(tableExpression.getGroupByClause().getExpressionList(),
                                tableExpression.getGroupByClause().getParameterList(), "groupByArg"))
                        .create();
            }
            if (tableExpression.getHavingClause() != null) {
                current = new QueryBuilder()
                        .queryType(Query.QueryType.FILTER)
                        .subQueries(current)
                        .parameters(new Parameter("filterExpression", tableExpression.getHavingClause().convert()))
                        .create();
            }
            if (tableExpression.getOrderByClause() != null) {
                List<Parameter> parameters = mergeExpressionsAndParameters(tableExpression.getOrderByClause().getOrderByArgs(),
                        tableExpression.getOrderByClause().getParamList(), "orderByArg");
                parameters.add(new Parameter("type", ValueExpressionFactory.constant("order")));
                current = new QueryBuilder()
                        .queryType(Query.QueryType.FILTER)
                        .subQueries(current)
                        .parameters(parameters)
                        .create();
            }
            if (tableExpression.getLimitClause() != null) {
                current = new QueryBuilder()
                        .queryType(Query.QueryType.FILTER)
                        .subQueries(current)
                        .parameters(new Parameter("type", ValueExpressionFactory.constant("limit")),
                                new Parameter("limitValue", tableExpression.getLimitClause().convert()))
                        .create();
            }
        } else {
            current = QueryFactory.fictive();
        }

        if (!selectItemList.isEmpty()) {
            current = new QueryBuilder()
                    .queryType(Query.QueryType.FILTER)
                    .parameters(parameterList.stream().map(p -> (Parameter) p.convert()).collect(Collectors.toList()))
                    .subQueries(current)
                    .requiredSchemaAttributes(selectItemList.stream().map(SQLExpression::convert).collect(Collectors.toList()))
                    .create();
        }
        return current;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLQuery)) return false;

        SQLQuery sqlQuery = (SQLQuery) o;

        return selectItemList.equals(sqlQuery.selectItemList) 
                && !(tableExpression != null ? !tableExpression.equals(sqlQuery.tableExpression) : sqlQuery.tableExpression != null);
    }

    @Override
    public int hashCode() {
        int result = selectItemList.hashCode();
        result = 31 * result + (tableExpression != null ? tableExpression.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SQLQuery{" +
                "selectItemList=" + selectItemList +
                ", tableExpression=" + tableExpression +
                '}';
    }
}
