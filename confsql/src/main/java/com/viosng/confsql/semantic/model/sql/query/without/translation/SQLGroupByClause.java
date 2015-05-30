package com.viosng.confsql.semantic.model.sql.query.without.translation;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLParameter;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 01.03.2015
 * Time: 2:40
 */
@Data
public class SQLGroupByClause implements SQLExpression {
    @NotNull
    private final List<SQLParameter> parameterList;
    
    @NotNull
    private final List<SQLExpression> expressionList;

}
