package com.viosng.confsql.semantic.model.expressions;

import com.viosng.confsql.semantic.model.ModelElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 2:12
 */
public interface Expression extends ModelElement{
    
    @NotNull
    public Type type();
    
    static enum Type {
        PLUS,
        MINUS,
        UNARY_MINUS,
        MULTIPLICATION,
        DIVISION,
        POWER,
        AND,
        OR,
        GREATER,
        GREATER_OR_EQUAL,
        LESS,
        LESS_OR_EQUAL,
        EQUAL,
        NOT,
        FUNCTION_CALL,
        CONSTANT,
        ATTRIBUTE,
        GROUP
    }
}
