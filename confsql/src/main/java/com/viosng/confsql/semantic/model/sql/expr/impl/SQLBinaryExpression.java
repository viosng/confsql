package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.ExpressionImpl;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 28.02.2015
 * Time: 18:11
 */
@Data
public class SQLBinaryExpression implements SQLExpression {
    
    @NotNull
    private final ArithmeticType operator;
    
    @NotNull
    private final SQLExpression left, right;

    @NotNull
    @Override
    public Expression convert() {
        return new ExpressionImpl(operator, left.convert(), right.convert());
    }

}
