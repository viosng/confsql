package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 28.02.2015
 * Time: 18:11
 */
public class SQLUnaryExpression implements SQLExpression {

    @NotNull
    private final ArithmeticType operator;

    @NotNull
    private final SQLExpression expression;

    public SQLUnaryExpression(@NotNull ArithmeticType operator, @NotNull SQLExpression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    @NotNull
    public ArithmeticType getOperator() {
        return operator;
    }

    @NotNull
    public SQLExpression getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLUnaryExpression)) return false;

        SQLUnaryExpression that = (SQLUnaryExpression) o;

        return expression.equals(that.expression) && operator.equals(that.operator);
    }

    @Override
    public int hashCode() {
        int result = operator.hashCode();
        result = 31 * result + expression.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLUnaryExpression{" +
                "operator='" + operator + '\'' +
                ", expression=" + expression +
                '}';
    }
}
