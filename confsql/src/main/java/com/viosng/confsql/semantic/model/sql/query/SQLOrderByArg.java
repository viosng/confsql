package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.special.expr.OrderByArgExpression;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 08.03.2015
 * Time: 0:10
 */
@Data
public class SQLOrderByArg implements SQLExpression {

    @NotNull
    private final SQLExpression argument;

    @NotNull
    private final String orderType;

    @Override
    @NotNull
    public Expression convert() {
        return new OrderByArgExpression(argument.convert(), orderType);
    }

}
