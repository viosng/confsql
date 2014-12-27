package com.viosng.confsql.semantic.model.expressions.unary;

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
class DefaultUnaryExpression implements UnaryExpression{
    @NotNull
    protected final Expression arg;
    @NotNull
    protected final String operation;
    @NotNull
    private Type type;

    protected DefaultUnaryExpression(@NotNull String operation,
                                     @NotNull Expression arg,
                                     @NotNull Type type) {
        this.arg = arg;
        this.operation = operation;
        this.type = type;
    }

    @NotNull
    @Override
    public Type type() {
        return type;
    }

    @NotNull
    @Override
    public Expression getArg() {
        return arg;
    }

    @NotNull
    @Override
    public Notification verify(@NotNull Context context) {
        return new Notification();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultUnaryExpression)) return false;
        DefaultUnaryExpression that = (DefaultUnaryExpression) o;
        return arg.equals(that.arg) && operation.equals(that.operation) && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = arg.hashCode();
        result = 31 * result + operation.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + operation + " " + arg + ")";
    }

}
