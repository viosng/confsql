package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 24.02.2015
 * Time: 1:30
 */
@Data
public class SQLParameterList implements SQLExpression {

    @NotNull
    private final List<SQLParameter> parameterList;

}
