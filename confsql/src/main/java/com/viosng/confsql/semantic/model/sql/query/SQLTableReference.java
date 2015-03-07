package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
