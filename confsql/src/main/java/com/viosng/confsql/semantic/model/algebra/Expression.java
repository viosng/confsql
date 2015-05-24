package com.viosng.confsql.semantic.model.algebra;

import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 2:12
 */
public interface Expression{

    @NotNull
    public final static String UNDEFINED_ID = "";

    @NotNull
    public default String id() {
        return UNDEFINED_ID;
    }

    public default void setId(@Nullable String id) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public ArithmeticType type();

    @NotNull
    public default List<Expression> getArguments() {
        return Collections.emptyList();
    }

    @NotNull
    public Notification verify(Context context);
}