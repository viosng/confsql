package com.viosng.confsql.semantic.model.algebra.special.expr;

import com.google.common.base.Joiner;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
        private String id;

        @NotNull
        protected final String value;

        protected AbstractValueExpression(@NotNull String id, @NotNull String value) {
            this.id = id;
            this.value = value;
        }

        protected AbstractValueExpression(@NotNull String value) {
            this(UNDEFINED_ID, value);
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

        @Override
        public void setId(@Nullable String id) {
            if (id != null) {
                this.id = id;
            }
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

    private static class AttributeExpression extends AbstractValueExpression implements ValueExpression.AttributeExpression{

        @NotNull
        private final List<String> object;

        protected AttributeExpression(@NotNull String id, @NotNull List<String> object) {
            super(id, "");
            this.object = object;
        }

        @NotNull
        @Override
        public List<String> getObject() {
            return object;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AttributeExpression)) return false;
            if (!super.equals(o)) return false;

            AttributeExpression that = (AttributeExpression) o;

            return object.equals(that.getObject());
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + object.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return Joiner.on('.').join(object);
        }

        @NotNull
        @Override
        public Notification verify(Context context) {
            return context.hasObject(object)
                    ? new Notification()
                    : new Notification().addWarning("There is no attribute (" + this.toString() + ") in a context");
        }
    }
    
    private static class ConstantExpression extends AbstractValueExpression implements ValueExpression.ConstantExpression {
        private ConstantExpression(@NotNull String id, @NotNull String value) {
            super(id, value);
        }

        private ConstantExpression(@NotNull String value) {
            super(value);
        }

        @NotNull
        @Override
        public Notification verify(Context context) {
            return new Notification();
        }
    }

    private static class FunctionCallExpressionImpl extends AbstractValueExpression implements ValueExpression.FunctionCallExpression {
        @NotNull
        private final List<Expression> arguments;

        @NotNull
        private final List<Parameter> parameters;

        private FunctionCallExpressionImpl(@NotNull String id, @NotNull String value, @NotNull List<Expression> arguments,
                                           @NotNull List<Parameter> parameters) {
            super(id, value);
            this.arguments = new ArrayList<>(arguments);
            this.parameters = parameters;
        }

        @NotNull
        @Override
        public List<Expression> getArguments() {
            return arguments;
        }

        @NotNull
        @Override
        public Notification verify(Context context) {
            return Stream.concat(arguments.stream(), parameters.stream())
                    .filter(a -> a != null)
                    .map(a -> a.verify(context))
                    .collect(Notification::new, Notification::accept, Notification::accept);
        }

        @NotNull
        public List<Parameter> getParameters() {
            return parameters;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof FunctionCallExpressionImpl)) return false;
            if (!super.equals(o)) return false;

            FunctionCallExpressionImpl that = (FunctionCallExpressionImpl) o;

            return arguments.equals(that.arguments) && parameters.equals(that.parameters);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + arguments.hashCode();
            result = 31 * result + parameters.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return value + "(" + Joiner.on(", ").join(arguments) + "; " + Joiner.on(", ").join(parameters) +  ")";
        }
    }
    
    public static ValueExpression.ConstantExpression constant(@NotNull String value) {
        return new ConstantExpression(!value.isEmpty() && value.charAt(0) == '\"' ? value.substring(1, value.length() - 1) : value);
    }

    public static ValueExpression.FunctionCallExpression functionCall(@NotNull String value,
                                                                      @NotNull List<Expression> expressions,
                                                                      @NotNull List<Parameter> parameters) {
        return new FunctionCallExpressionImpl(value, value, expressions, parameters);
    }
    
    public static ValueExpression.AttributeExpression attribute(@NotNull List<String> object) {
        return new AttributeExpression(object.get(object.size() - 1), object);
    }

    public static ValueExpression.ConstantExpression constant(@NotNull String value, @NotNull String id) {
        return new ConstantExpression(id, value);
    }

    public static ValueExpression.AttributeExpression attribute(List<String> object,
                                                                @NotNull String id) {
        return new AttributeExpression(id, object);
    }
}
