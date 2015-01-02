package com.viosng.confsql.semantic.model.expressions;

import com.viosng.confsql.semantic.model.InternalQueryElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 2:12
 */
public interface Expression extends InternalQueryElement {

    @NotNull
    public Type type();

    static enum Type {
        PLUS("+"),
        MINUS("-"),
        UNARY_MINUS("-"),
        MULTIPLICATION("*"),
        DIVISION("/"),
        POWER("^"),
        AND("and"),
        OR("or"),
        GREATER(">"),
        GREATER_OR_EQUAL(">="),
        LESS("<"),
        LESS_OR_EQUAL("<="),
        EQUAL("=="),
        NOT("not"),
        FUNCTION_CALL("function"),
        CONSTANT("constant"),
        ATTRIBUTE("attribute"),
        GROUP("group");

        private String name;

        public String getName() {
            return name;
        }

        Type(String name) {
            this.name = name;
        }
    }
    
    public boolean containsType(Type type);
}