package com.viosng.confsql.semantic.model.expressions;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 11:17
 */

public interface PredicateExpression extends Expression {

    public static boolean isInvalidBooleanConstant(Expression argument) {
        if (argument.type() == Expression.Type.CONSTANT){
            String lowerCaseValue = argument.getName().toLowerCase();
            return !(lowerCaseValue.equals("true") || lowerCaseValue.equals("false"));
        }
        return false;
    }
}
