package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.queries.QueryBuilder;
import com.viosng.confsql.semantic.model.algebra.queries.QueryFactory;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLParameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @NotNull
    public List<SQLSelectItem> getSelectItemList() {
        return selectItemList;
    }

    @Nullable
    public SQLTableExpression getTableExpression() {
        return tableExpression;
    }

    @Override
    public Expression convert() {
        Query subQuery = tableExpression != null ? (Query) tableExpression.convert() : QueryFactory.fictive();
        return new QueryBuilder()
                .queryType(Query.QueryType.FILTER)
                .parameters(parameterList.stream().map(p -> (Parameter) p.convert()).collect(Collectors.toList()))
                .subQueries(subQuery)
                .requiredSchemaAttributes(selectItemList.stream().map(SQLExpression::convert).collect(Collectors.toList()))
                .create();
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
