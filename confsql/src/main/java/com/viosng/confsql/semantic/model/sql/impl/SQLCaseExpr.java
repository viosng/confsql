package com.viosng.confsql.semantic.model.sql.impl;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 28.02.2015
 * Time: 13:38
 */
public class SQLCaseExpr implements SQLExpression{
    
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

    public SQLCaseExpr(SQLExpression expression, @NotNull List<SQLWhenThenClause> whenThenClauses, SQLExpression elseExpression) {
        this.expression = expression;
        this.elseExpression = elseExpression;
        this.whenThenClauses = whenThenClauses;
    }

    @Nullable
    public SQLExpression getExpression() {
        return expression;
    }
    
    @NotNull
    public List<SQLWhenThenClause> getWhenThenClauses() {
        return whenThenClauses;
    }

    @Nullable
    public SQLExpression getElseExpression() {
        return elseExpression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLCaseExpr)) return false;

        SQLCaseExpr that = (SQLCaseExpr) o;

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
                ", elseExpression=" + elseExpression +
                ", whenThenClauses=" + whenThenClauses +
                '}';
    }
}
