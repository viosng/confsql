package com.viosng.confsql.semantic.model.expressions.binary;

import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 10:59
 */
class DefaultBinaryExpression implements BinaryExpression{
    @NotNull
    private final String id;
    @NotNull
    private final Type type;
    @NotNull
    protected final Expression left, right;

    protected DefaultBinaryExpression(@NotNull String id, @NotNull Type type, @NotNull Expression left, @NotNull Expression right) {
        this.id = id;
        this.type = type;
        this.left = left;
        this.right = right;
    }

    protected DefaultBinaryExpression(@NotNull Type type, @NotNull Expression left, @NotNull Expression right) {
        this(Expression.UNDEFINED_ID, type, left, right);
    }

    @NotNull
    @Override
    public String id() {
        return id;
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

    @NotNull
    @Override
    public Type type() {
        return type;
    }

    @Override
    public Expression findExpressionByType(Type type) {
        if (this.type == type) return this;
        Expression leftType = left.findExpressionByType(type);
        return leftType != null ? leftType : right.findExpressionByType(type);
    }


    @NotNull
    @Override
    public Notification verify(@NotNull Context context) {
        return Arrays.asList(left, right).stream().map(a -> a.verify(context))
                .collect(Notification::new, Notification::accept, Notification::accept);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultBinaryExpression)) return false;

        DefaultBinaryExpression that = (DefaultBinaryExpression) o;

        return id.equals(that.id) && left.equals(that.left) && right.equals(that.right) && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = left.hashCode();
        result = 31 * result + right.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + left + " " + type.getName() + " " + right + ")";
    }
}
