package com.viosng.confsql.semantic.model.sql.query.without.translation;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLParameter;
import com.viosng.confsql.semantic.model.sql.query.SQLTablePrimary;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 01.03.2015
 * Time: 20:09
 */
@Data
public class SQLJoinedTablePrimary implements SQLExpression {
    
    @NotNull
    private final String joinType;
    
    @NotNull
    private final List<SQLParameter> parameterList;
    
    @NotNull
    private final SQLTablePrimary tablePrimary;

    @Nullable
    private final SQLExpression onCondition;

}
