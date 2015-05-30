package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.special.expr.Parameter;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 28.02.2015
 * Time: 13:23
 */
@Data
public class SQLFunctionCall implements SQLExpression {
    
    @NotNull
    private final String name;
    
    @NotNull
    private final List<SQLExpression> arguments;
    
    @NotNull
    private final List<SQLParameter> parameters;

    public SQLFunctionCall(@NotNull String name,
                           @NotNull List<SQLExpression> arguments,
                           @NotNull List<SQLParameter> parameters) {
        this.name = name;
        this.arguments = arguments;
        this.parameters = parameters;
    }

    @NotNull
    @Override
    public Expression convert() {
        return ValueExpressionFactory.functionCall(
                name,
                arguments.stream().map(SQLExpression::convert).collect(Collectors.toList()),
                parameters.stream().map(p -> (Parameter)p.convert()).collect(Collectors.toList()));
    }

}
