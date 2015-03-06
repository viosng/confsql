package com.viosng.confsql.semantic.model.sql;

import com.viosng.confsql.semantic.model.algebra.Expression;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.02.2015
 * Time: 13:04
 */
public interface SQLExpression {
    
    default Expression convert() {
        throw new UnsupportedOperationException();
    } 
}
