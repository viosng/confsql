package com.viosng.confsql.semantic.model.sql;

import com.viosng.confsql.semantic.model.expressions.PredicateExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SQLJoinQuery extends SQLSingleQuery {
    @NotNull
    SQLQuery getLeft();
    
    @Nullable
    PredicateExpression joinCondition();
    
    @Nullable
    SQLJoinQuery getRight();
}
