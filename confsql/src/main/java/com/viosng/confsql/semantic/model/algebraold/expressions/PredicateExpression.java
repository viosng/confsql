package com.viosng.confsql.semantic.model.algebraold.expressions;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebraold.expressions.other.ValueExpression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 11:17
 */

public interface PredicateExpression extends Expression {
    public static boolean isInvalidBooleanConstant(@NotNull Expression argument) {
        if (argument.type() == ArithmeticType.CONSTANT){
            String lowerCaseValue = ((ValueExpression.ConstantExpression) argument).getValue().toLowerCase();
            return !(lowerCaseValue.equals("true") || lowerCaseValue.equals("false"));
        }
        return false;
    }
}
