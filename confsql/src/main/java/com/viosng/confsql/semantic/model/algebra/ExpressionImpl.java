package com.viosng.confsql.semantic.model.algebra;

import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 06.03.2015
 * Time: 18:16
 */
public class ExpressionImpl implements Expression {

    @NotNull
    private String id = UNDEFINED_ID;

    @NotNull
    private final ArithmeticType type;

    @NotNull
    private final List<Expression> arguments;

    @NotNull
    private List<Parameter> parameters = Collections.emptyList();

    public ExpressionImpl(@NotNull ArithmeticType type, @NotNull List<Expression> arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    public ExpressionImpl(@NotNull ArithmeticType type, Expression... arguments) {
        this.type = type;
        this.arguments = Arrays.asList(arguments);
    }

    public void setId(@Nullable String id) {
        this.id = id == null ? this.id : id;
    }

    @NotNull
    @Override
    public ArithmeticType type() {
        return type;
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
    @Override
    public List<Expression> getArguments() {
        return arguments;
    }

    @NotNull
    @Override
    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpressionImpl)) return false;

        ExpressionImpl that = (ExpressionImpl) o;

        return arguments.equals(that.arguments) && id.equals(that.id) && parameters.equals(that.parameters) && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + arguments.hashCode();
        result = 31 * result + parameters.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ExpressionImpl{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", arguments=" + arguments +
                ", parameters=" + parameters +
                '}';
    }
}
