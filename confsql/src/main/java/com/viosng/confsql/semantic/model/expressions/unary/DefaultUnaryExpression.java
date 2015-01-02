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
    private final String id;
    @NotNull
    protected final Expression arg;
    @NotNull
    private Type type;

    protected DefaultUnaryExpression(@NotNull String id, @NotNull Type type, @NotNull Expression arg) {
        this.id = id;
        this.arg = arg;
        this.type = type;
    }

    @NotNull
    @Override
    public String id() {
        return id;
    }

    @NotNull
    @Override
    public Type type() {
        return type;
    }

    @Override
    public boolean containsType(Type type) {
        return arg.type() == type || arg.containsType(type);
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
        return arg.equals(that.arg) && id.equals(that.id) && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = arg.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + type.getName() + " " + arg + ")";
    }

}
