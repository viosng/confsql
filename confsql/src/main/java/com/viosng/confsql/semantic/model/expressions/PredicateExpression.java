package com.viosng.confsql.semantic.model.expressions;

import com.viosng.confsql.semantic.model.expressions.other.ValueExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 11:17
 */

public interface PredicateExpression extends Expression {
    public static boolean isInvalidBooleanConstant(@NotNull Expression argument) {
        if (argument.type() == Expression.Type.CONSTANT){
            String lowerCaseValue = ((ValueExpression.ConstantExpression) argument).getValue().toLowerCase();
            return !(lowerCaseValue.equals("true") || lowerCaseValue.equals("false"));
        }
        return false;
    }
}
