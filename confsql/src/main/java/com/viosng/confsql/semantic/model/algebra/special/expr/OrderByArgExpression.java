package com.viosng.confsql.semantic.model.algebra.special.expr;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 08.03.2015
 * Time: 0:06
 */

@Data
public class OrderByArgExpression implements Expression {

    @NotNull
    private final Expression argument;

    @NotNull
    private final String orderType;

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

    @NotNull
    @Override
    public Notification verify(Context context) {
        return argument.verify(context);
    }
}
