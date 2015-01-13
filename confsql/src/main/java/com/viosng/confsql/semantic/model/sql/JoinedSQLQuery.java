package com.viosng.confsql.semantic.model.sql;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface JoinedSQLQuery extends SQLQuery {
    @NotNull
    public List<SingleSQLQuery> joinedQueries();
}
