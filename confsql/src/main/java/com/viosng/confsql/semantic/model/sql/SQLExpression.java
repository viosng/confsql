package com.viosng.confsql.semantic.model.sql;

import com.viosng.confsql.semantic.model.ModelElement;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.02.2015
 * Time: 13:04
 */
public interface SQLExpression {
    
    default ModelElement convert() {
        return new ModelElement() {};
    } 
}
