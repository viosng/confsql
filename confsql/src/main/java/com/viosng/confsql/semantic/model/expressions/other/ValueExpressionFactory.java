package com.viosng.confsql.semantic.model.expressions.other;

import com.google.common.base.Joiner;
import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 24.12.2014
 * Time: 19:15
 */
public class ValueExpressionFactory {
    
    private ValueExpressionFactory(){}

    private static abstract class AbstractValueExpression implements ValueExpression {
        @NotNull
        protected final String value;

        protected AbstractValueExpression(@NotNull String value) {
            this.value = value;
        }

        @NotNull
        @Override
        public String getValue() {
            return value;
        }

        @NotNull
        @Override
        public Notification verify(@NotNull Context context) {
            return new Notification();
        }

        @Override
        public String toString() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AbstractValueExpression)) return false;
            AbstractValueExpression that = (AbstractValueExpression) o;
            return value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }

    private static abstract class AbstractAttributeExpression extends AbstractValueExpression implements ValueExpression.AttributeExpression{

        @NotNull
        private final String objectReference;

        protected AbstractAttributeExpression(@NotNull String objectReference, @NotNull String value) {
            super(value);
            this.objectReference = objectReference;
        }

        @NotNull
        @Override
        public String getObjectReference() {
            return objectReference;
        }

        @NotNull
        @Override
        public Notification verify(@NotNull Context context) {
            Notification notification = new Notification();
            if (!context.hasReference(objectReference)) {
                notification.error("Context doesn't know about \"" + objectReference + "\" reference");
            }
            else if (!context.hasAttribute(objectReference, value)) {
                notification.error("Object reference \"" + objectReference + "\" hasn't attribute \"" + value + "\"");
            }
            return notification;
        }

        @Override
        public String toString() {
            return objectReference + "." + value;
        }
    }
    
    private static class ConstantExpression extends AbstractValueExpression implements ValueExpression.ConstantExpression {
        private ConstantExpression(@NotNull String value) {
            super(value);
        }
    }

    private static class FunctionCallExpression extends AbstractValueExpression implements ValueExpression.FunctionCallExpression {
        @NotNull
        private final List<Expression> arguments;

        public FunctionCallExpression(@NotNull String value, @NotNull List<Expression> arguments) {
            super(value);
            this.arguments = arguments;
        }

        @NotNull
        @Override
        public List<Expression> getArguments() {
            return arguments;
        }

        @Override
        public String toString() {
            return value + "(" + Joiner.on(", ").join(arguments) + ")";
        }
    }

    private static class AttributeExpression extends AbstractAttributeExpression implements ValueExpression.AttributeExpression{
        private AttributeExpression(@NotNull String objectReference, @NotNull String value) {
            super(objectReference, value);
        }
    }

    private static class GroupExpression extends AbstractAttributeExpression implements ValueExpression.GroupExpression{
        private GroupExpression(@NotNull String objectReference, @NotNull String value) {
            super(objectReference, value);
        }
    }
    
    public static ValueExpression.ConstantExpression constant(@NotNull String value) {
        return new ConstantExpression(value);
    }

    public static ValueExpression.FunctionCallExpression functionCall(@NotNull String value, Expression... expressions) {
        return new FunctionCallExpression(value, Arrays.asList(expressions));
    }
    
    public static ValueExpression.AttributeExpression attribute(@NotNull String objectReference, @NotNull String value) {
        return new AttributeExpression(objectReference, value);
    }

    public static ValueExpression.GroupExpression group(@NotNull String objectReference, @NotNull String value) {
        return new GroupExpression(objectReference, value);
    }
}
