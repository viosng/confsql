package com.viosng.confsql.semantic.model.algebraold.expressions.unary;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 10:59
 */
public class DefaultUnaryExpression implements UnaryExpression{
    @NotNull
    private final String id;
    @NotNull
    protected final Expression arg;
    @NotNull
    private final ArithmeticType type;

    public DefaultUnaryExpression(@NotNull String id, @NotNull ArithmeticType type, @NotNull Expression arg) {
        this.id = id;
        this.arg = arg;
        this.type = type;
    }

    public DefaultUnaryExpression(@NotNull ArithmeticType type, @NotNull Expression arg) {
        this.id = UNDEFINED_ID;
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
    public ArithmeticType type() {
        return type;
    }

    @Override
    public Expression findExpressionByType(ArithmeticType arithmeticType) {
        return this.type == arithmeticType ? this : arg.findExpressionByType(arithmeticType);
    }

    @NotNull
    @Override
    public Expression getArg() {
        return arg;
    }

    @NotNull
    @Override
    public Notification verify(@NotNull Context context) {
        return arg.verify(context);
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
        return "(" + type + " " + arg + ")";
    }

}
