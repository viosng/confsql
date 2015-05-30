package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLField;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 02.03.2015
 * Time: 3:16
 */
@Data
public class SQLSelectItem implements SQLExpression {
    @NotNull
    private final SQLExpression item;
    
    @Nullable
    private final SQLField as;

    @Override
    @NotNull
    public Expression convert() {
        Expression exp = item.convert();
        if (as != null) exp.setId(as.getName());
        return exp;
    }

}
