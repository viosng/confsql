package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.queries.QueryBuilder;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

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

    public SQLTablePrimary(@NotNull SQLExpression source, @Nullable String alias, @NotNull List<String> columnList) {
        this.source = source;
        this.alias = alias != null ? alias : Expression.UNDEFINED_ID;
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
    public Expression convert() {
        Expression exp;
        if (source instanceof SQLField) {
            SQLField table = (SQLField) source;
            exp = new QueryBuilder()
                    .queryType(Query.QueryType.PRIMARY)
                    .parameters(new Parameter("sourceName", ValueExpressionFactory.constant(table.getName())))
                    .create();
            if (!columnList.isEmpty()) {
                exp = new QueryBuilder()
                        .queryType(Query.QueryType.FILTER)
                        .requiredSchemaAttributes(columnList.stream().map(c ->
                                ValueExpressionFactory.attribute(table.getName(), c)).collect(Collectors.toList()))
                        .subQueries((Query)exp)
                        .create();
            }
        } else {
            exp = source.convert();
        }
        exp.setId(alias);
        return exp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLTablePrimary)) return false;

        SQLTablePrimary that = (SQLTablePrimary) o;

        return alias.equals(that.alias) && columnList.equals(that.columnList) && source.equals(that.source);
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
