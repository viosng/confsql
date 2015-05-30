package com.viosng.confsql.semantic.model.sql.query.without.translation;

import com.viosng.confsql.semantic.model.algebra.special.expr.Parameter;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLParameter;
import com.viosng.confsql.semantic.model.sql.query.SQLFromClause;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 01.03.2015
 * Time: 22:44
 */
@Data
public class SQLTableExpression implements SQLExpression{
    
    @NotNull
    private final SQLFromClause fromClause;
    
    @Nullable
    private final SQLExpression whereClause;
    
    @Nullable
    private final SQLGroupByClause groupByClause;

    @Nullable
    private final SQLExpression havingClause;
    
    @Nullable
    private final SQLOrderByClause orderByClause;

    @Nullable
    private final SQLExpression limitClause;

    private List<Parameter> mergeExpressionsAndParameters(@NotNull List<? extends SQLExpression> sqlExpressions,
                                                          @NotNull List<SQLParameter> sqlParameters,
                                                          @NotNull String expressionPrefix) {
        List<Parameter> parameters = new ArrayList<>();
        for (int i = 0; i < sqlExpressions.size(); i++) {
            parameters.add(new Parameter(expressionPrefix + i, sqlExpressions.get(i).convert()));
        }

        parameters.addAll(sqlParameters.stream().map(p -> (Parameter)p.convert()).collect(Collectors.toList()));

        return parameters;
    }

}
