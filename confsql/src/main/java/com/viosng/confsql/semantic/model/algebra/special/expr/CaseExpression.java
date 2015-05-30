package com.viosng.confsql.semantic.model.algebra.special.expr;

import com.google.common.collect.Lists;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import com.viosng.confsql.semantic.model.other.NotificationCollector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 13.03.2015
 * Time: 17:17
 */
public class CaseExpression implements Expression {

    @NotNull
    private String id = UNDEFINED_ID;

    @Nullable
    private final Expression argument;

    @NotNull
    private final List<Parameter> parameters;

    public CaseExpression(@Nullable Expression argument, @NotNull List<Parameter> parameters) {
        this.argument = argument;
        this.parameters = parameters;
    }

    @NotNull
    @Override
    public String id() {
        return id;
    }

    @Override
    public void setId(@Nullable String id) {
        this.id = id == null ? this.id : id;
    }

    @NotNull
    public List<Parameter> getParameters() {
        return parameters;
    }

    @NotNull
    @Override
    public ArithmeticType type() {
        return ArithmeticType.CASE;
    }

    @NotNull
    @Override
    public List<Expression> getArguments() {
        return argument != null ? Lists.newArrayList(argument) : Collections.<Expression>emptyList();
    }

    @NotNull
    @Override
    public Notification verify(Context context) {
        return Stream.concat(Stream.of(argument), parameters.stream())
                .parallel()
                .filter(a -> a != null)
                .map(p -> p.verify(context)).collect(new NotificationCollector());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CaseExpression)) return false;

        CaseExpression that = (CaseExpression) o;

        return !(argument != null ? !argument.equals(that.argument) : that.argument != null)
                && id.equals(that.id) && parameters.equals(that.parameters);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (argument != null ? argument.hashCode() : 0);
        result = 31 * result + parameters.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CaseExpression{" +
                "id='" + id + '\'' +
                ", argument=" + argument +
                ", parameters=" + parameters +
                '}';
    }
}
