package com.viosng.confsql.semantic.model.sql.impl;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;

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
    private final SQLExpressionsAndParamsList expressionsAndParamsList;

    public SQLFunctionCall(@NotNull String name, @NotNull SQLExpressionsAndParamsList expressionsAndParamsList) {
        this.name = name;
        this.expressionsAndParamsList = expressionsAndParamsList;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public SQLExpressionsAndParamsList getExpressionsAndParamsList() {
        return expressionsAndParamsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLFunctionCall)) return false;

        SQLFunctionCall that = (SQLFunctionCall) o;

        return expressionsAndParamsList.equals(that.expressionsAndParamsList) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + expressionsAndParamsList.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLFunctionCall{" +
                "name='" + name + '\'' +
                ", expressionsAndParamsList=" + expressionsAndParamsList +
                '}';
    }
}
