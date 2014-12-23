package com.viosng.confsql.semantic.model.expressions.binary;

import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 10:59
 */
class DefaultBinaryExpression implements BinaryExpression{
    @NotNull
    protected final Expression left, right;
    @NotNull
    protected final String operation;
    @NotNull
    private Type type;

    protected DefaultBinaryExpression(@NotNull String operation,
                                      @NotNull Expression left,
                                      @NotNull Expression right,
                                      @NotNull Type type) {
        this.left = left;
        this.right = right;
        this.operation = operation;
        this.type = type;
    }

    @Override
    @NotNull
    public Expression getLeftArg() {
        return left;
    }

    @Override
    @NotNull
    public Expression getRightArg() {
        return right;
    }

    @Override
    @NotNull
    public String getName() {
        return operation;
    }

    @NotNull
    @Override
    public Type type() {
        return type;
    }

    @NotNull
    @Override
    public Notification verify(@NotNull Context context) {
        return new Notification();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultBinaryExpression)) return false;
        DefaultBinaryExpression that = (DefaultBinaryExpression) o;
        return left.equals(that.left) && operation.equals(that.operation) && right.equals(that.right) && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = left.hashCode();
        result = 31 * result + right.hashCode();
        result = 31 * result + operation.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + left + " " + operation + " " + right + ")";
    }
}
