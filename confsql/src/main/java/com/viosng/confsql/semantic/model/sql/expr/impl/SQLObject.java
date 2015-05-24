package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.special.expr.ObjectExpression;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.03.2015
 * Time: 11:12
 */
public class SQLObject implements SQLExpression {

    @NotNull
    @Override
    public Expression convert() {
        return ObjectExpression.getInstance();
    }
}
