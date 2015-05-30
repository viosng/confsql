package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.queries.QueryBuilder;
import com.viosng.confsql.semantic.model.algebra.special.expr.Parameter;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLField;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 01.03.2015
 * Time: 13:01
 */
@Data
public class SQLTablePrimary implements SQLExpression {
    
    @NotNull
    private final SQLExpression source;
    
    @Nullable
    private final String alias;

    @NotNull
    private final String queryId;
    
    @NotNull
    private final List<String> columnList;

    public SQLTablePrimary(@NotNull SQLExpression source, @Nullable String alias, @NotNull List<String> columnList) {
        this.source = source;
        this.alias = alias;
        this.columnList = columnList;
        if (alias != null) {
            this.queryId = alias;
        } else if (source instanceof SQLField) {
            this.queryId = ((SQLField)source).getName();
        } else {
            throw new IllegalArgumentException("Null table primary id");
        }
    }

    @Override
    @NotNull
    public Expression convert() {
        Expression exp;
        if (source instanceof SQLField) {
            SQLField table = (SQLField) source;
            exp = new QueryBuilder()
                    .queryType(Query.QueryType.PRIMARY)
                    .parameters(new Parameter("sourceName", ValueExpressionFactory.constant(table.getName())))
                    .id(table.getName())
                    .create();
            if (!columnList.isEmpty()) {
                exp = new QueryBuilder()
                        .queryType(Query.QueryType.FILTER)
                        .requiredSchemaAttributes(columnList.stream().map(c ->
                                ValueExpressionFactory.attribute(Arrays.asList(table.getName(), c))).collect(Collectors.toList()))
                        .subQueries((Query)exp)
                        .id(exp.id())
                        .create();
            }
        } else {
            exp = source.convert();
        }
        exp.setId(alias);
        return exp;
    }

}
