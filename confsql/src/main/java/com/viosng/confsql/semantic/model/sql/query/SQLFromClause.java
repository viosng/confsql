package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLParameter;
import com.viosng.confsql.semantic.model.sql.query.without.translation.SQLTableReference;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

    @NotNull
    public List<SQLParameter> getParameterList() {
        return parameterList;
    }

    @NotNull
    public List<SQLTableReference> getTableReferenceList() {
        return tableReferenceList;
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
