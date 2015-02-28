package com.viosng.confsql.semantic.model.sql.impl;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 28.02.2015
 * Time: 18:11
 */
public class SQLBinaryExpression implements SQLExpression {
    
    @NotNull
    private final ArithmeticType operator;
    
    @NotNull
    private final SQLExpression left, right;

    public SQLBinaryExpression(@NotNull ArithmeticType operator, @NotNull SQLExpression left, @NotNull SQLExpression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @NotNull
    public ArithmeticType getOperator() {
        return operator;
    }

    @NotNull
    public SQLExpression getLeft() {
        return left;
    }

    @NotNull
    public SQLExpression getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLBinaryExpression)) return false;

        SQLBinaryExpression that = (SQLBinaryExpression) o;

        return left.equals(that.left) && operator.equals(that.operator) && right.equals(that.right);
    }

    @Override
    public int hashCode() {
        int result = operator.hashCode();
        result = 31 * result + left.hashCode();
        result = 31 * result + right.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLBinaryExpression{" +
                "operator='" + operator + '\'' +
                ", left=" + left +
                ", right=" + right +
                '}';
    }
}
