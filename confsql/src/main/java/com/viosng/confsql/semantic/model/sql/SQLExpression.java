package com.viosng.confsql.semantic.model.sql;

import com.viosng.confsql.semantic.model.algebra.Expression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.02.2015
 * Time: 13:04
 */
public interface SQLExpression {

    @NotNull
    default Expression convert() {
        throw new UnsupportedOperationException();
    } 
}
