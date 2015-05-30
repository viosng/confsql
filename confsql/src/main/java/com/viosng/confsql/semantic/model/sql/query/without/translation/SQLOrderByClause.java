package com.viosng.confsql.semantic.model.sql.query.without.translation;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLParameter;
import com.viosng.confsql.semantic.model.sql.query.SQLOrderByArg;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 01.03.2015
 * Time: 1:54
 */
@Data
public class SQLOrderByClause implements SQLExpression{
    @NotNull
    private final List<SQLParameter> paramList;
    
    @NotNull
    private final List<SQLOrderByArg> orderByArgs;

}
