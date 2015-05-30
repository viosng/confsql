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
 * Date: 01.03.2015
 * Time: 22:11
 */
@Data
public class SQLFromClause implements SQLExpression {
    
    @NotNull
    private final List<SQLParameter> parameterList;

    @NotNull
    private final List<SQLTableReference> tableReferenceList;


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

}
