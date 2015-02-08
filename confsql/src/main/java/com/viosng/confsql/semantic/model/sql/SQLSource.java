package com.viosng.confsql.semantic.model.sql;

import org.jetbrains.annotations.NotNull;

public interface SQLSource extends SQLSingleQuery {
    @NotNull
    public String sourceName();
}
