package com.viosng.confsql.semantic.model.sql.impl;

import com.google.common.collect.ImmutableList;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.sql.SQLSingleQuery;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 06.02.2015
 * Time: 20:55
 */
public abstract class AbstractSQLSingleQuery implements SQLSingleQuery{
    @Nullable
    private final String alias;

    @NotNull
    private final List<Parameter> parameters;

    public AbstractSQLSingleQuery(@Nullable String alias, @NotNull List<Parameter> parameters) {
        this.alias = alias;
        this.parameters = ImmutableList.copyOf(parameters);
    }

    @Nullable
    @Override
    public String alias() {
        return alias;
    }

    @NotNull
    @Override
    public List<Parameter> parameters() {
        return parameters;
    }
}
