package com.viosng.confsql.semantic.model.algebra.special.expr;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.03.2015
 * Time: 11:08
 */
public class ObjectExpression implements Expression {

    private ObjectExpression(){}

    private static class Holder {
        private static final ObjectExpression INSTANCE = new ObjectExpression();
    }

    public static ObjectExpression getInstance() {
        return Holder.INSTANCE;
    }

    @NotNull
    @Override
    public ArithmeticType type() {
        return ArithmeticType.OBJECT;
    }

    @NotNull
    @Override
    public Notification verify(Context context) {
        return new Notification();
    }

    @Override
    public String toString() {
        return "ObjectExpression{}";
    }
}
