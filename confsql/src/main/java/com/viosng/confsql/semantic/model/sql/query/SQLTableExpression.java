package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.query.without.translation.SQLGroupByClause;
import com.viosng.confsql.semantic.model.sql.query.without.translation.SQLOrderByClause;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 01.03.2015
 * Time: 22:44
 */
public class SQLTableExpression implements SQLExpression{
    
    @NotNull
    private final SQLFromClause fromClause;
    
    @Nullable
    private final SQLExpression whereClause, havingClause, limitClause;
    
    @Nullable
    private final SQLGroupByClause groupByClause;
    
    @Nullable
    private final SQLOrderByClause orderByClause;

    public SQLTableExpression(@NotNull SQLFromClause fromClause, @Nullable SQLExpression whereClause, 
                              @Nullable SQLGroupByClause groupByClause, @Nullable SQLExpression havingClause, 
                              @Nullable SQLOrderByClause orderByClause, @Nullable SQLExpression limitClause) {
        this.fromClause = fromClause;
        this.whereClause = whereClause;
        this.havingClause = havingClause;
        this.limitClause = limitClause;
        this.groupByClause = groupByClause;
        this.orderByClause = orderByClause;
    }

    @NotNull
    public SQLFromClause getFromClause() {
        return fromClause;
    }

    @Nullable
    public SQLExpression getWhereClause() {
        return whereClause;
    }

    @Nullable
    public SQLExpression getHavingClause() {
        return havingClause;
    }

    @Nullable
    public SQLExpression getLimitClause() {
        return limitClause;
    }

    @Nullable
    public SQLGroupByClause getGroupByClause() {
        return groupByClause;
    }

    @Nullable
    public SQLOrderByClause getOrderByClause() {
        return orderByClause;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLTableExpression)) return false;

        SQLTableExpression that = (SQLTableExpression) o;

        if (!fromClause.equals(that.fromClause)) return false;
        if (groupByClause != null ? !groupByClause.equals(that.groupByClause) : that.groupByClause != null)
            return false;
        if (havingClause != null ? !havingClause.equals(that.havingClause) : that.havingClause != null) return false;
        if (limitClause != null ? !limitClause.equals(that.limitClause) : that.limitClause != null) return false;
        if (orderByClause != null ? !orderByClause.equals(that.orderByClause) : that.orderByClause != null)
            return false;
        if (whereClause != null ? !whereClause.equals(that.whereClause) : that.whereClause != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fromClause.hashCode();
        result = 31 * result + (whereClause != null ? whereClause.hashCode() : 0);
        result = 31 * result + (havingClause != null ? havingClause.hashCode() : 0);
        result = 31 * result + (limitClause != null ? limitClause.hashCode() : 0);
        result = 31 * result + (groupByClause != null ? groupByClause.hashCode() : 0);
        result = 31 * result + (orderByClause != null ? orderByClause.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SQLTableExpression{" +
                "fromClause=" + fromClause +
                ", whereClause=" + whereClause +
                ", havingClause=" + havingClause +
                ", limitClause=" + limitClause +
                ", groupByClause=" + groupByClause +
                ", orderByClause=" + orderByClause +
                '}';
    }
}
