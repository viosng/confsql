package com.viosng.confsql.semantic.model;

import com.viosng.confsql.semantic.model.algebra.Expression;
import org.jetbrains.annotations.NotNull;

public interface ExpressionConverter<T> {

    @NotNull
    T convert(@NotNull Expression expression);

    @NotNull
    T convert(@NotNull String query);
}
