package com.viosng.confsql.semantic.model.sql.old;


import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.PredicateExpression;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public interface SQLQueryAll {

    public static class OrderClauseExpression {
        enum Order { ASC, DESC }

        @NotNull
        private final Expression expression;
        
        @NotNull
        private final Order order;

        public OrderClauseExpression(@NotNull Expression expression, @NotNull Order order) {
            this.expression = expression;
            this.order = order;
        }

        @NotNull
        public Expression expression() {
            return expression;
        }

        @NotNull
        public Order order() {
            return order;
        }
    }

    @NotNull
    public default List<Expression> select() {
        return Collections.emptyList();
    }

    @NotNull
    public default List<Parameter> selectParameters() {
        return Collections.emptyList();
    }

    @Nullable
    public default SQLQueryAll from() { return null; }

    @Nullable
    public default PredicateExpression where() { return null; }

    @NotNull
    public default List<Expression> groupBy() {
        return Collections.emptyList();
    }

    @NotNull
    public default List<Parameter> groupByParameters() {
        return Collections.emptyList();
    }

    @Nullable
    public default PredicateExpression having() { return null; }

    @NotNull
    public default List<OrderClauseExpression> orderBy() {
        return Collections.emptyList();
    }

    @NotNull
    public default List<Parameter> orderByParameters() {
        return Collections.emptyList();
    }
    

}
