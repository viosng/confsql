package com.viosng.confsql.semantic.model.sql.impl;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 28.02.2015
 * Time: 3:15
 */
public class SQLParameter implements SQLExpression {
    @NotNull
    private final String name;
    
    @NotNull
    private final SQLExpression value;

    public SQLParameter(@NotNull String name, @NotNull SQLExpression value) {
        this.name = name;
        this.value = value;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public SQLExpression getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLParameter)) return false;

        SQLParameter that = (SQLParameter) o;

        return name.equals(that.name) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLParameter{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
