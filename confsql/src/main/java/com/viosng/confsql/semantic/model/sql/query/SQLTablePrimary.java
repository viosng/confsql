package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 01.03.2015
 * Time: 13:01
 */
public class SQLTablePrimary implements SQLExpression {
    
    @NotNull
    private final SQLExpression source;
    
    @NotNull
    private final String alias;
    
    @NotNull
    private final List<String> columnList;

    public SQLTablePrimary(@NotNull SQLExpression source, @NotNull String alias, @NotNull List<String> columnList) {
        this.source = source;
        this.alias = alias;
        this.columnList = columnList;
    }

    @NotNull
    public SQLExpression getSource() {
        return source;
    }

    @NotNull
    public String getAlias() {
        return alias;
    }

    @NotNull
    public List<String> getColumnList() {
        return columnList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLTablePrimary)) return false;

        SQLTablePrimary that = (SQLTablePrimary) o;

        if (!alias.equals(that.alias)) return false;
        if (!columnList.equals(that.columnList)) return false;
        if (!source.equals(that.source)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + alias.hashCode();
        result = 31 * result + columnList.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLTablePrimary{" +
                "source=" + source +
                ", alias='" + alias + '\'' +
                ", columnList=" + columnList +
                '}';
    }
}
