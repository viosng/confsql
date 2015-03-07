package com.viosng.confsql.semantic.model.algebra.special.expr;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebraold.expressions.PredicateExpression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 31.01.2015
 * Time: 16:55
 */
public class IfExpressionFactory {
    
    private static class IfExpressionImpl implements IfExpression {
        @NotNull
        private final String id;

        @NotNull
        private PredicateExpression predicate;

        @NotNull
        protected final Expression expressionOnTrue, expressionOnFalse;

        public IfExpressionImpl(@NotNull String id, 
                                @NotNull PredicateExpression predicate,
                                @NotNull Expression expressionOnTrue, 
                                @NotNull Expression expressionOnFalse) {
            this.id = id;
            this.predicate = predicate;
            this.expressionOnTrue = expressionOnTrue;
            this.expressionOnFalse = expressionOnFalse;
        }

        @NotNull
        @Override
        public PredicateExpression getPredicate() {
            return predicate;
        }

        @NotNull
        @Override
        public Expression getExpressionOnTrue() {
            return expressionOnTrue;
        }

        @NotNull
        @Override
        public Expression getExpressionOnFalse() {
            return expressionOnFalse;
        }

        @Override
        public Expression findExpressionByType(ArithmeticType arithmeticType) {
            if (this.type() == arithmeticType) return this;
            Expression exp = predicate.findExpressionByType(arithmeticType);
            return exp != null ? exp : (
                    (exp = expressionOnTrue.findExpressionByType(arithmeticType)) != null ? exp : expressionOnFalse.findExpressionByType(arithmeticType));
        }

        @NotNull
        @Override
        public Notification verify(@NotNull Context context) {
            return Arrays.asList(predicate, expressionOnTrue, expressionOnFalse).stream().map(a -> a.verify(context))
                    .collect(Notification::new, Notification::accept, Notification::accept);
        }

        @NotNull
        @Override
        public String id() {
            return id;
        }
    }
    
    private IfExpressionFactory(){}

    public static  IfExpression create(@NotNull PredicateExpression predicate,
                                       @NotNull Expression expressionOnTrue,
                                       @NotNull Expression expressionOnFalse) {
        return new IfExpressionImpl(Expression.UNDEFINED_ID, predicate, expressionOnTrue, expressionOnFalse);
    }
    
    public static  IfExpression create(@NotNull PredicateExpression predicate,
                                       @NotNull Expression expressionOnTrue,
                                       @NotNull Expression expressionOnFalse,
                                       @NotNull String id) {
        return new IfExpressionImpl(id, predicate, expressionOnTrue, expressionOnFalse);
    }
}
