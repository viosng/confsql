package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.special.expr.CaseExpression;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 28.02.2015
 * Time: 13:38
 */
public class SQLCase implements SQLExpression{
    
    public static class SQLWhenThenClause implements SQLExpression {
        
        @NotNull
        private final SQLExpression when, then;
        
        public SQLWhenThenClause(@NotNull SQLExpression when, @NotNull SQLExpression then) {
            this.when = when;
            this.then = then;
        }

        @NotNull
        public SQLExpression getWhen() {
            return when;
        }

        @NotNull
        public SQLExpression getThen() {
            return then;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SQLWhenThenClause)) return false;

            SQLWhenThenClause that = (SQLWhenThenClause) o;

            return then.equals(that.then) && when.equals(that.when);
        }

        @Override
        public int hashCode() {
            int result = when.hashCode();
            result = 31 * result + then.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "SQLWhenThenClause{" +
                    "when=" + when +
                    ", then=" + then +
                    '}';
        }
    }
    
    @Nullable
    private final SQLExpression expression, elseExpression;
    
    @NotNull
    private final List<SQLWhenThenClause> whenThenClauses;

    public SQLCase(@Nullable SQLExpression expression, @NotNull List<SQLWhenThenClause> whenThenClauses, @Nullable SQLExpression elseExpression) {
        this.expression = expression;
        this.elseExpression = elseExpression;
        this.whenThenClauses = whenThenClauses;
    }

    @NotNull
    @Override
    public Expression convert() {
        AtomicInteger index = new AtomicInteger();
        List<Parameter> parameters = whenThenClauses.stream().flatMap(
                w -> Stream.of(
                        new Parameter("whenExpression" + index, w.getWhen().convert()),
                        new Parameter("thenExpression" + index.getAndIncrement(), w.getThen().convert())))
                .collect(Collectors.toList());
        if (elseExpression != null) {
            parameters.add(new Parameter("elseExpression", elseExpression.convert()));
        }
        return new CaseExpression(expression != null ? expression.convert() : null, parameters);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLCase)) return false;

        SQLCase that = (SQLCase) o;

        return !(elseExpression != null ? !elseExpression.equals(that.elseExpression) : that.elseExpression != null) 
                && !(expression != null ? !expression.equals(that.expression) : that.expression != null) 
                && whenThenClauses.equals(that.whenThenClauses);
    }

    @Override
    public int hashCode() {
        int result = expression != null ? expression.hashCode() : 0;
        result = 31 * result + (elseExpression != null ? elseExpression.hashCode() : 0);
        result = 31 * result + whenThenClauses.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLCaseExpr{" +
                "expression=" + expression +
                ", whenThenClauses=" + whenThenClauses +
                ", elseExpression=" + elseExpression +
                '}';
    }
}
