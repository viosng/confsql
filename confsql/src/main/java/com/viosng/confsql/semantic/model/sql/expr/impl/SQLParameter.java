package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.special.expr.Parameter;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 28.02.2015
 * Time: 3:15
 */
@Data
public class SQLParameter implements SQLExpression {
    @NotNull
    private final String name;
    
    @NotNull
    private final SQLExpression value;

    @NotNull
    @Override
    public Expression convert() {
        return new Parameter(name, value.convert());
    }

}
