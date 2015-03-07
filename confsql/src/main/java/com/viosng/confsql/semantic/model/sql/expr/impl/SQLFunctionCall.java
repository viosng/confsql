package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebraold.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 28.02.2015
 * Time: 13:23
 */
public class SQLFunctionCall implements SQLExpression {
    
    @NotNull
    private final String name;
    
    @NotNull
    private final List<SQLExpression> arguments;
    
    @NotNull
    private final List<SQLParameter> parameters;

    public SQLFunctionCall(@NotNull String name, @NotNull List<SQLExpression> arguments, @NotNull List<SQLParameter> parameters) {
        this.name = name;
        this.arguments = arguments;
        this.parameters = parameters;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public List<SQLExpression> getArguments() {
        return arguments;
    }

    @NotNull
    public List<SQLParameter> getParameters() {
        return parameters;
    }

    @Override
    public Expression convert() {
        return ValueExpressionFactory.functionCall(
                name,
                arguments.stream().map(SQLExpression::convert).collect(Collectors.toList()),
                parameters.stream().map(p -> (Parameter)p.convert()).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLFunctionCall)) return false;

        SQLFunctionCall that = (SQLFunctionCall) o;

        return arguments.equals(that.arguments) && name.equals(that.name) && parameters.equals(that.parameters);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + arguments.hashCode();
        result = 31 * result + parameters.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLFunctionCall{" +
                "name='" + name + '\'' +
                ", arguments=" + arguments +
                ", parameters=" + parameters +
                '}';
    }
}
