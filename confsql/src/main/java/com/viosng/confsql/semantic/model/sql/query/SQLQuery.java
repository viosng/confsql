package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 02.03.2015
 * Time: 3:35
 */
public class SQLQuery implements SQLExpression {
    
    @NotNull
    private final List<SQLSelectItem> selectItemList;
    
    @Nullable
    private final SQLTableExpression tableExpression;

    public SQLQuery(@NotNull List<SQLSelectItem> selectItemList, @Nullable SQLTableExpression tableExpression) {
        this.selectItemList = selectItemList;
        this.tableExpression = tableExpression;
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
