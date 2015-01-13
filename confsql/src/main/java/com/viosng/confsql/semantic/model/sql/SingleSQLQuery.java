package com.viosng.confsql.semantic.model.sql;

import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SingleSQLQuery extends SQLQuery {
    @NotNull
    public String alias();

    @NotNull
    public List<Parameter> parameters();
}
