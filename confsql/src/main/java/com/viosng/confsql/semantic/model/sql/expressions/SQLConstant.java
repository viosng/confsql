package com.viosng.confsql.semantic.model.sql.expressions;

import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.02.2015
 * Time: 13:03
 */
public class SQLConstant implements SQLExpression {
    @NotNull
    private final String value;

    public SQLConstant(@NotNull String value) {
        this.value = value;
    }

    @NotNull
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SQLConstant{" +
                "value='" + value + '\'' +
                '}';
    }
}
