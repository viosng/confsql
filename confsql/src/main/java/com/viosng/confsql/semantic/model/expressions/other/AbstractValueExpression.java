package com.viosng.confsql.semantic.model.expressions.other;

import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 15:11
 */
public abstract class AbstractValueExpression implements Expression{
    @NotNull
    protected String value;

    protected AbstractValueExpression(@NotNull String value) {
        this.value = value;
    }

    @NotNull
    @Override
    public String getName() {
        return value;
    }

    @NotNull
    @Override
    public Notification verify(@NotNull Context context) {
        return new Notification();
    }

    @Override
    public String toString() {
        return value;
    }
}
