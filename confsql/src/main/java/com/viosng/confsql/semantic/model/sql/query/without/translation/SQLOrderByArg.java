package com.viosng.confsql.semantic.model.sql.query.without.translation;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.special.expr.OrderByArgExpression;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 08.03.2015
 * Time: 0:10
 */
public class SQLOrderByArg implements SQLExpression {

    @NotNull
    private final SQLExpression argument;

    @NotNull
    private final String orderType;

    public SQLOrderByArg(@NotNull SQLExpression argument, @NotNull String orderType) {
        this.argument = argument;
        this.orderType = orderType.toLowerCase();
    }

    @NotNull
    public SQLExpression getArgument() {
        return argument;
    }

    @NotNull
    public String getOrderType() {
        return orderType;
    }

    @Override
    public Expression convert() {
        return new OrderByArgExpression(argument.convert(), orderType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLOrderByArg)) return false;

        SQLOrderByArg that = (SQLOrderByArg) o;

        return argument.equals(that.argument) && orderType.equals(that.orderType);

    }

    @Override
    public int hashCode() {
        int result = argument.hashCode();
        result = 31 * result + orderType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SQLOrderByArg{" +
                "argument=" + argument +
                ", orderType='" + orderType + '\'' +
                '}';
    }
}
