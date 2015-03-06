package com.viosng.confsql.semantic.model.other;

import com.viosng.confsql.semantic.model.algebra.Expression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 2:08
 */
public class Parameter implements Expression {
    
    @NotNull
    private final String id;
    
    @NotNull
    private final Expression value;

    public Parameter(@NotNull String id, @NotNull Expression value) {
        if (id.length() == 0) throw new IllegalArgumentException("Empty parameter sourceName");
        this.id = id;
        this.value = value;
    }

    @NotNull
    @Override
    public String id() {
        return id;
    }

    @NotNull
    @Override
    public ArithmeticType type() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression findExpressionByType(ArithmeticType arithmeticType) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Notification verify(@NotNull Context context) {
        return new Notification();
    }

    @NotNull
    public Expression getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Parameter parameter = (Parameter) o;
        return id.equals(parameter.id) && value.equals(parameter.value);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "sourceName='" + id + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
