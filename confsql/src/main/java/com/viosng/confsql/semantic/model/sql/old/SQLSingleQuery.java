package com.viosng.confsql.semantic.model.sql.old;

import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SQLSingleQuery extends SQLQuery {
    @Nullable
    public String alias();

    @NotNull
    public List<Parameter> parameters();
}
