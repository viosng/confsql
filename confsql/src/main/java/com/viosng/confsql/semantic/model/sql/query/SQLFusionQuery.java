package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.queries.QueryBuilder;
import com.viosng.confsql.semantic.model.algebra.special.expr.Parameter;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLParameter;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 09.03.2015
 * Time: 2:13
 */
@Data
public class SQLFusionQuery implements SQLExpression {

    @NotNull
    private final List<SQLParameter> parameterList;

    @NotNull
    private final List<SQLExpression> queryList;

    @Override
    @NotNull
    public Expression convert() {
        return new QueryBuilder()
                .queryType(Query.QueryType.FUSION)
                .parameters(parameterList.stream().map(p -> (Parameter) p.convert()).collect(Collectors.toList()))
                .subQueries(queryList.stream().map(q -> (Query)q.convert()).collect(Collectors.toList()))
                .create();
    }

}
