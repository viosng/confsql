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
 * Date: 01.03.2015
 * Time: 22:11
 */
public class SQLFromClause implements SQLExpression {
    
    @NotNull
    private final List<SQLParameter> parameterList;

    @NotNull
    private final List<SQLTableReference> tableReferenceList;

    public SQLFromClause(@NotNull List<SQLParameter> parameterList, @NotNull List<SQLTableReference> tableReferenceList) {
        this.parameterList = parameterList;
        this.tableReferenceList = tableReferenceList;
    }

    @Override
    @NotNull
    public Expression convert() {
        if (tableReferenceList.size() == 1 && parameterList.isEmpty()) {
            return tableReferenceList.get(0).convert();
        }
        return new QueryBuilder()
                .queryType(tableReferenceList.size() > 1 ? Query.QueryType.JOIN : Query.QueryType.FILTER)
                .parameters(parameterList.stream().map(p -> (Parameter) p.convert()).collect(Collectors.toList()))
                .subQueries(tableReferenceList.stream().map(t -> (Query) t.convert()).collect(Collectors.toList()))
                .create();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLFromClause)) return false;

        SQLFromClause that = (SQLFromClause) o;

        return parameterList.equals(that.parameterList) && tableReferenceList.equals(that.tableReferenceList);
    }

    @Override
    public int hashCode() {
        int result = parameterList.hashCode();
        result = 31 * result + tableReferenceList.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLFromClause{" +
                "parameterList=" + parameterList +
                ", tableReferenceList=" + tableReferenceList +
                '}';
    }
}
