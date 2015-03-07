package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.queries.QueryBuilder;
import com.viosng.confsql.semantic.model.algebraold.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLParameter;
import com.viosng.confsql.semantic.model.sql.query.without.translation.SQLGroupByClause;
import com.viosng.confsql.semantic.model.sql.query.without.translation.SQLOrderByClause;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 01.03.2015
 * Time: 22:44
 */
public class SQLTableExpression implements SQLExpression{
    
    @NotNull
    private final SQLFromClause fromClause;
    
    @Nullable
    private final SQLExpression whereClause, havingClause, limitClause;
    
    @Nullable
    private final SQLGroupByClause groupByClause;
    
    @Nullable
    private final SQLOrderByClause orderByClause; // todo make orderType and params(?) for every column

    public SQLTableExpression(@NotNull SQLFromClause fromClause, @Nullable SQLExpression whereClause, 
                              @Nullable SQLGroupByClause groupByClause, @Nullable SQLExpression havingClause, 
                              @Nullable SQLOrderByClause orderByClause, @Nullable SQLExpression limitClause) {
        this.fromClause = fromClause;
        this.whereClause = whereClause;
        this.havingClause = havingClause;
        this.limitClause = limitClause;
        this.groupByClause = groupByClause;
        this.orderByClause = orderByClause;
    }

    @NotNull
    public SQLFromClause getFromClause() {
        return fromClause;
    }

    @Nullable
    public SQLExpression getWhereClause() {
        return whereClause;
    }

    @Nullable
    public SQLExpression getHavingClause() {
        return havingClause;
    }

    @Nullable
    public SQLExpression getLimitClause() {
        return limitClause;
    }

    @Nullable
    public SQLGroupByClause getGroupByClause() {
        return groupByClause;
    }

    @Nullable
    public SQLOrderByClause getOrderByClause() {
        return orderByClause;
    }

    private List<Parameter> mergeExpressionsAndParameters(List<SQLExpression> sqlExpressions, List<SQLParameter> sqlParameters,
                                                          String expressionPrefix) {
        List<Parameter> parameters = new ArrayList<>();
        for (int i = 0; i < sqlExpressions.size(); i++) {
            parameters.add(new Parameter(expressionPrefix+ i, sqlExpressions.get(i).convert()));
        }

        parameters.addAll(sqlParameters.stream().map(p -> (Parameter)p.convert()).collect(Collectors.toList()));

        return parameters;
    }

    @Override
    public Expression convert() {
        Query current = (Query) fromClause.convert();
        if (whereClause != null) {
            current = new QueryBuilder()
                    .queryType(Query.QueryType.FILTER)
                    .subQueries(current)
                    .parameters(new Parameter("filterExpression", whereClause.convert()))
                    .create();
        }
        if (groupByClause != null) {
            current = new QueryBuilder()
                    .queryType(Query.QueryType.AGGREGATION)
                    .subQueries(current)
                    .parameters(mergeExpressionsAndParameters(groupByClause.getExpressionList(),
                            groupByClause.getParameterList(), "groupByArg"))
                    .create();
        }
        if (havingClause != null) {
            current = new QueryBuilder()
                    .queryType(Query.QueryType.FILTER)
                    .subQueries(current)
                    .parameters(new Parameter("filterExpression", havingClause.convert()))
                    .create();
        }
        if (orderByClause != null) {
            List<Parameter> parameters = mergeExpressionsAndParameters(orderByClause.getExpressionList(),
                    orderByClause.getParamList(), "orderByArg");
            parameters.add(new Parameter("type", ValueExpressionFactory.constant("order")));
            parameters.add(new Parameter("orderType", ValueExpressionFactory.constant(orderByClause.getOrderType())));
            current = new QueryBuilder()
                    .queryType(Query.QueryType.FILTER)
                    .subQueries(current)
                    .parameters(parameters)
                    .create();
        }
        if (limitClause != null) {
            current = new QueryBuilder()
                    .queryType(Query.QueryType.FILTER)
                    .subQueries(current)
                    .parameters(new Parameter("type", ValueExpressionFactory.constant("limit")),
                            new Parameter("limitValue", limitClause.convert()))
                    .create();
        }
        return current;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLTableExpression)) return false;

        SQLTableExpression that = (SQLTableExpression) o;

        if (!fromClause.equals(that.fromClause)) return false;
        if (groupByClause != null ? !groupByClause.equals(that.groupByClause) : that.groupByClause != null)
            return false;
        if (havingClause != null ? !havingClause.equals(that.havingClause) : that.havingClause != null) return false;
        if (limitClause != null ? !limitClause.equals(that.limitClause) : that.limitClause != null) return false;
        if (orderByClause != null ? !orderByClause.equals(that.orderByClause) : that.orderByClause != null)
            return false;
        if (whereClause != null ? !whereClause.equals(that.whereClause) : that.whereClause != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fromClause.hashCode();
        result = 31 * result + (whereClause != null ? whereClause.hashCode() : 0);
        result = 31 * result + (havingClause != null ? havingClause.hashCode() : 0);
        result = 31 * result + (limitClause != null ? limitClause.hashCode() : 0);
        result = 31 * result + (groupByClause != null ? groupByClause.hashCode() : 0);
        result = 31 * result + (orderByClause != null ? orderByClause.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SQLTableExpression{" +
                "fromClause=" + fromClause +
                ", whereClause=" + whereClause +
                ", havingClause=" + havingClause +
                ", limitClause=" + limitClause +
                ", groupByClause=" + groupByClause +
                ", orderByClause=" + orderByClause +
                '}';
    }
}
