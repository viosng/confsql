package com.viosng.confsql.semantic.model.algebra.special.expr;

import com.google.common.collect.Lists;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.*;
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

    @Override
    public Expression findExpressionByType(ArithmeticType arithmeticType) {
        if (arithmeticType == type()) return this;
        return Stream.concat(Stream.of(argument), parameters.stream()).map(
                e -> e == null ? null : e.findExpressionByType(arithmeticType)).filter(e -> e != null).findFirst().orElse(null);
    }

    @NotNull
    @Override
    public Notification verify(@NotNull Context context) {
        return Stream.concat(Stream.of(argument), parameters.stream()).filter(e -> e != null).map(
                e -> e.verify(context)).collect(Notification::new, Notification::accept, Notification::accept);
    }

    @NotNull
    @Override
    public List<Expression> getArguments() {
        return argument != null ? Lists.newArrayList(argument) : Collections.<Expression>emptyList();
    }

    @NotNull
    @Override
    public Verifier verify(@NotNull Verifier verifier) {
        return Stream.concat(Stream.of(argument), parameters.stream()).filter(e -> e != null).map(
                e -> e.verify(verifier)).collect(Verifier::new, Verifier::accept, Verifier::accept).attribute(UNDEFINED_ID, id);
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