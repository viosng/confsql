package com.viosng.confsql.semantic.model.algebra.special.expr;

import com.google.common.collect.Lists;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import com.viosng.confsql.semantic.model.other.NotificationCollector;
import lombok.Data;
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
@Data
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
}
