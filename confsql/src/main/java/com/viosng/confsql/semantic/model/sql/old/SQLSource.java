package com.viosng.confsql.semantic.model.sql.old;

import org.jetbrains.annotations.NotNull;

public interface SQLSource extends SQLSingleQuery {
    @NotNull
    public String sourceName();
}
