package com.viosng.confsql.semantic.model.algebra.expressions.other;

import com.google.common.base.Joiner;
import com.viosng.confsql.semantic.model.algebra.expressions.Expression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
        protected final String id, value;

        protected AbstractValueExpression(@NotNull String id, @NotNull String value) {
            this.id = id;
            this.value = value;
        }

        protected AbstractValueExpression(@NotNull String value) {
            this(Expression.UNDEFINED_ID, value);
        }

        @NotNull
        @Override
        public String id() {
            return id;
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

        protected AbstractAttributeExpression(@NotNull String id, @NotNull String objectReference, @NotNull String value) {
            super(id, value);
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
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AbstractAttributeExpression)) return false;
            if (!super.equals(o)) return false;
            AbstractAttributeExpression that = (AbstractAttributeExpression) o;
            return objectReference.equals(that.objectReference);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + objectReference.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return objectReference + "." + value;
        }
    }
    
    private static class ConstantExpression extends AbstractValueExpression implements ValueExpression.ConstantExpression {
        private ConstantExpression(@NotNull String id, @NotNull String value) {
            super(id, value);
        }

        private ConstantExpression(@NotNull String value) {
            super(value);
        }
    }

    private static class FunctionCallExpression extends AbstractValueExpression implements ValueExpression.FunctionCallExpression {
        @NotNull
        private final List<Expression> arguments;
        
        private Notification notification;

        private FunctionCallExpression(@NotNull String id, @NotNull String value, @NotNull List<Expression> arguments) {
            super(id, value);
            this.arguments = new ArrayList<>(arguments);
        }

        private FunctionCallExpression(@NotNull String value, @NotNull List<Expression> arguments) {
            super(value);
            this.arguments = new ArrayList<>(arguments);
        }

        @NotNull
        @Override
        public List<Expression> getArguments() {
            return arguments;
        }

        @NotNull
        @Override
        public Notification verify(@NotNull Context context) {
            if (notification == null) {
                notification = arguments.stream().map(e -> e.verify(context)).collect(Notification::new, 
                        Notification::accept, Notification::accept);
            }
            return notification;
        }

        @Override
        public Expression findExpressionByType(ArithmeticType arithmeticType) {
            if (arithmeticType == this.type()) return this;
            else return arguments.stream().map(a -> a.findExpressionByType(arithmeticType)).filter(a -> a != null).findFirst().orElse(null);
        }

        @Override
        public String toString() {
            return value + "(" + Joiner.on(", ").join(arguments) + ")";
        }
    }

    private static class AttributeExpression extends AbstractAttributeExpression implements ValueExpression.AttributeExpression{
        private AttributeExpression(@NotNull String id, @NotNull String objectReference, @NotNull String value) {
            super(id, objectReference, value);
        }
    }

    private static class GroupExpression extends AbstractAttributeExpression implements ValueExpression.GroupExpression{
        @NotNull
        private final List<Expression> groupedAttributes;

        private Notification notification;

        public GroupExpression(@NotNull String id, 
                               @NotNull String objectReference, 
                               @NotNull String value, 
                               @NotNull List<Expression> groupedAttributes) {
            super(id, objectReference, value);
            this.groupedAttributes = groupedAttributes;
        }

        @NotNull
        @Override
        public List<Expression> getGroupedAttributes() {
            return groupedAttributes;
        }

        @NotNull
        @Override
        public Notification verify(@NotNull Context context) {
            if (notification == null) {
                notification = groupedAttributes.stream().map(e -> e.verify(context)).collect(Notification::new,
                        Notification::accept, Notification::accept);
            }
            return notification;
        }
    }
    
    public static ValueExpression.ConstantExpression constant(@NotNull String value) {
        return new ConstantExpression(value);
    }

    public static ValueExpression.FunctionCallExpression functionCall(@NotNull String value, 
                                                                      @NotNull List<Expression> expressions) {
        return new FunctionCallExpression(value, expressions);
    }
    
    public static ValueExpression.AttributeExpression attribute(@NotNull String objectReference, @NotNull String value) {
        return new AttributeExpression(value, objectReference, value);
    }

    public static ValueExpression.GroupExpression group(@NotNull String objectReference, 
                                                        @NotNull String value, 
                                                        @NotNull List<Expression> groupedAttributes) {
        return new GroupExpression(value, objectReference, value, groupedAttributes);
    }

    public static ValueExpression.ConstantExpression constant(@NotNull String value, @NotNull String id) {
        return new ConstantExpression(id, value);
    }

    public static ValueExpression.FunctionCallExpression functionCall(@NotNull String value, 
                                                                      @NotNull List<Expression> expressions, 
                                                                      @NotNull String id) {
        return new FunctionCallExpression(id, value, expressions);
    }

    public static ValueExpression.AttributeExpression attribute(@NotNull String objectReference, 
                                                                @NotNull String value, 
                                                                @NotNull String id) {
        return new AttributeExpression(id, objectReference, value);
    }

    public static ValueExpression.GroupExpression group(@NotNull String objectReference, 
                                                        @NotNull String value,
                                                        @NotNull List<Expression> groupedAttributes,
                                                        @NotNull String id) {
        return new GroupExpression(id, objectReference, value, groupedAttributes);
    }
}
