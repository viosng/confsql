package com.viosng.confsql.semantic.model.sql;


import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.PredicateExpression;
import com.viosng.confsql.semantic.model.expressions.other.ValueExpression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SQLQuery {
    
    public static class OrderClauseAttribute {
        enum Order { ASC, DESC }

        @NotNull
        private final ValueExpression.AttributeExpression attribute;
        
        @NotNull
        private final Order order;

        public OrderClauseAttribute(@NotNull ValueExpression.AttributeExpression attribute, @NotNull Order order) {
            this.attribute = attribute;
            this.order = order;
        }

        @NotNull
        public ValueExpression.AttributeExpression attribute() {
            return attribute;
        }

        @NotNull
        public Order order() {
            return order;
        }
    }
    
    public List<Expression> select();

    public List<Parameter> selectParameters();
    
    public SQLQuery from();
    
    public PredicateExpression where();
    
    public List<Expression> groupBy();

    public List<Parameter> groupByParameters();

    public PredicateExpression having();
    
    public List<OrderClauseAttribute> orderBy();

    public List<Parameter> orderByParameters();
    
    @NotNull
    public Context getContext();

    @NotNull
    public Notification verify();
}
