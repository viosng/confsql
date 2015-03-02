package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLParameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 01.03.2015
 * Time: 20:09
 */
public class SQLJoinedTablePrimary implements SQLExpression {
    
    @NotNull
    private final String joinType;
    
    @NotNull
    private final List<SQLParameter> parameterList;
    
    @NotNull
    private final SQLTablePrimary tablePrimary;
    
    private final SQLExpression onCondition;

    public SQLJoinedTablePrimary(@NotNull String joinType, @NotNull List<SQLParameter> parameterList,
                                 @NotNull SQLTablePrimary tablePrimary, SQLExpression onCondition) {
        this.joinType = joinType.toLowerCase();
        this.parameterList = parameterList;
        this.tablePrimary = tablePrimary;
        this.onCondition = onCondition;
    }

    @NotNull
    public String getJoinType() {
        return joinType;
    }

    @NotNull
    public List<SQLParameter> getParameterList() {
        return parameterList;
    }

    @NotNull
    public SQLTablePrimary getTablePrimary() {
        return tablePrimary;
    }

    public SQLExpression getOnCondition() {
        return onCondition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLJoinedTablePrimary)) return false;

        SQLJoinedTablePrimary that = (SQLJoinedTablePrimary) o;

        return joinType.equals(that.joinType) 
                && !(onCondition != null ? !onCondition.equals(that.onCondition) : that.onCondition != null) 
                && parameterList.equals(that.parameterList) 
                && tablePrimary.equals(that.tablePrimary);
    }

    @Override
    public int hashCode() {
        int result = joinType.hashCode();
        result = 31 * result + parameterList.hashCode();
        result = 31 * result + tablePrimary.hashCode();
        result = 31 * result + (onCondition != null ? onCondition.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SQLJoinedTablePrimary{" +
                "joinType='" + joinType + '\'' +
                ", parameterList=" + parameterList +
                ", tablePrimary=" + tablePrimary +
                ", onCondition=" + onCondition +
                '}';
    }
}
