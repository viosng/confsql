package com.viosng.confsql.semantic.model.algebra.special.expr;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 08.03.2015
 * Time: 0:06
 */
public class OrderByArgExpression implements Expression {

    @NotNull
    private final Expression argument;

    @NotNull
    private final String orderType;

    public OrderByArgExpression(@NotNull Expression argument, @NotNull String orderType) {
        this.argument = argument;
        this.orderType = orderType;
    }

    @NotNull
    public Expression getArgument() {
        return argument;
    }

    @NotNull
    public String getOrderType() {
        return orderType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderByArgExpression)) return false;

        OrderByArgExpression that = (OrderByArgExpression) o;

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
        return "OrderByArgExpression{" +
                "argument=" + argument +
                ", orderType='" + orderType + '\'' +
                '}';
    }

    @NotNull
    @Override
    public ArithmeticType type() {
        return ArithmeticType.ORDER;
    }

    @Override
    public Expression findExpressionByType(ArithmeticType arithmeticType) {
        if (arithmeticType == type()) return this;
        else return argument.findExpressionByType(arithmeticType);
    }

    @NotNull
    @Override
    public Notification verify(Context context) {
        return argument.verify(context);
    }
}
