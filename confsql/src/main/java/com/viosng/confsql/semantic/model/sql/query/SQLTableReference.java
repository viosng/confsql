package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.queries.QueryBuilder;
import com.viosng.confsql.semantic.model.algebra.special.expr.Parameter;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLField;
import com.viosng.confsql.semantic.model.sql.query.without.translation.SQLJoinedTablePrimary;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 01.03.2015
 * Time: 21:31
 */
@Data
public class SQLTableReference implements SQLExpression {
    
    @NotNull
    private final SQLTablePrimary tablePrimary;
    
    @NotNull
    private final List<SQLJoinedTablePrimary> joinedTablePrimaryList;

    @Override
    @NotNull
    public Expression convert() {
        if (joinedTablePrimaryList.isEmpty()) {
            return tablePrimary.convert();
        }
        return joinedTablePrimaryList.stream().reduce((Query) tablePrimary.convert(),
                (query, sqlJoinedTablePrimary) -> {
                    if (sqlJoinedTablePrimary.getTablePrimary().getSource() instanceof SQLField) {
                        SQLField tableName = (SQLField) sqlJoinedTablePrimary.getTablePrimary().getSource();
                        if (tableName.getName().startsWith(query.id() + ".")) {
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(new Parameter("unNestObject", tableName.convert()));
                            parameters.addAll(sqlJoinedTablePrimary.getParameterList().stream().map(p ->
                                    (Parameter)p.convert()).collect(Collectors.toList()));
                            return new QueryBuilder()
                                    .queryType(Query.QueryType.UNNEST)
                                    .parameters(parameters)
                                    .subQueries(query)
                                    .id(query.id())
                                    .create();
                        }
                    }
                    List<Parameter> parameters = new ArrayList<>();
                    parameters.add(new Parameter("joinType", ValueExpressionFactory.constant(sqlJoinedTablePrimary.getJoinType())));
                    if(sqlJoinedTablePrimary.getOnCondition() != null) {
                        parameters.add(new Parameter("onCondition", sqlJoinedTablePrimary.getOnCondition().convert()));
                    }
                    parameters.addAll(sqlJoinedTablePrimary.getParameterList().stream().map(p ->
                            (Parameter)p.convert()).collect(Collectors.toList()));
                    return new QueryBuilder()
                            .queryType(Query.QueryType.JOIN)
                            .parameters(parameters)
                            .subQueries(query, (Query)sqlJoinedTablePrimary.getTablePrimary().convert())
                            .id(sqlJoinedTablePrimary.getTablePrimary().getQueryId())
                            .create();
                }, (query, query2) -> null);
    }

}
