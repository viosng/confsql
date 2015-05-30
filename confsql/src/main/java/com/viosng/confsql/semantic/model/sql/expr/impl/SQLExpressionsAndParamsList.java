package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 28.02.2015
 * Time: 13:20
 */
@Data
public class SQLExpressionsAndParamsList implements SQLExpression{
    @NotNull
    private final SQLExpressionList expressionList;

    @NotNull
    private final SQLParameterList parameterList;
}
