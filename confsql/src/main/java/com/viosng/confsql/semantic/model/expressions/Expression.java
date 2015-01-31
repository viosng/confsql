package com.viosng.confsql.semantic.model.expressions;

import com.viosng.confsql.semantic.model.ModelElement;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 2:12
 */
public interface Expression extends ModelElement {

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
        GROUP("group"),
        IF("if");

        private final String name;

        public String getName() {
            return name;
        }

        Type(String name) {
            this.name = name;
        }
    }

    /**
     * @param type the expression type to find.
     * @return the instance with specific {@code type} expression sub tree or {@code null} if it won't be found.
     */
    public Expression findExpressionByType(Type type);

    @NotNull
    public Notification verify(@NotNull Context context);
}