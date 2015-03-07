package com.viosng.confsql.semantic.model.algebra.queries;

import com.google.common.collect.Lists;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebraold.expressions.other.ValueExpression;
import com.viosng.confsql.semantic.model.algebraold.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.other.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 27.12.2014
 * Time: 14:31
 */
public abstract class DefaultQuery implements Query{
    @NotNull
    private String id;
    
    @NotNull
    private final List<Parameter> parameters;
    
    @NotNull
    private final List<Expression> requiredSchemaAttributes, argumentExpressions;
    
    @NotNull
    protected List<ValueExpression.AttributeExpression> queryObjectAttributes;
    
    @NotNull
    private final List<Query> subQueries;
    
    private Context context;

    private Notification notification;

    public DefaultQuery(@NotNull String id,
                        @NotNull List<Parameter> parameters,
                        @NotNull List<Expression> requiredSchemaAttributes,
                        @NotNull List<Query> subQueries, 
                        @NotNull List<Expression> argumentExpressions) {
        this.id = id;
        this.parameters = parameters;
        this.requiredSchemaAttributes = requiredSchemaAttributes;
        this.queryObjectAttributes = requiredSchemaAttributes.stream()
                .filter(a -> !a.id().equals(Expression.UNDEFINED_ID))
                .map(a -> a.type() == ArithmeticType.GROUP
                        ? ValueExpressionFactory.group(id, a.id(), ((ValueExpression.GroupExpression) a).getGroupedAttributes())
                        : ValueExpressionFactory.attribute(id, a.id()))
                .collect(Collectors.toList());
        this.argumentExpressions = argumentExpressions;
        this.subQueries = subQueries;
    }

    @NotNull
    @Override
    public String id() {
        return id;
    }

    @Override
    public void setId(@Nullable String id) {
        this.id = id;
    }

    @NotNull
    @Override
    public List<Parameter> getParameters() {
        return parameters;
    }

    @NotNull
    @Override
    public List<Expression> getRequiredSchemaAttributes() {
        return requiredSchemaAttributes;
    }

    @NotNull
    @Override
    public List<ValueExpression.AttributeExpression> getQueryObjectAttributes() {
        return queryObjectAttributes;
    }

    @NotNull
    @Override
    public List<Query> getSubQueries() {
        return subQueries;
    }

    @NotNull
    @Override
    public List<Expression> getArgumentExpressions() {
        return argumentExpressions;
    }


    @NotNull
    @Override
    public Context getContext() {
        if (context == null) {
            context = createContext();
        }
        return context;
    }

    @NotNull
    protected DefaultContext createContext() {
        return new DefaultContext(subQueries);
    }
    
    @NotNull
    @Override
    public Notification verify() {
        if (notification == null) {
            notification = Lists.newArrayList(
                    subQueries.stream().map(Query::verify)
                            .collect(Notification::new, Notification::accept, Notification::accept), 
                    requiredSchemaAttributes.stream().map(e -> e.verify(getContext()))
                            .collect(Notification::new, Notification::accept, Notification::accept), 
                    argumentExpressions.stream().map(e -> e.verify(getContext()))
                            .collect(Notification::new, Notification::accept, Notification::accept))
                    .stream().collect(Notification::new, Notification::accept, Notification::accept);
        }
        return notification;
    }

    @Override
    public String toString() {
        return "DefaultQuery{\n" +
                "id='" + id + '\'' +
                ",\n parameters=" + parameters +
                ",\n requiredSchemaAttributes=" + requiredSchemaAttributes +
                ",\n queryObjectAttributes=" + queryObjectAttributes +
                ",\n argumentExpressions=" + argumentExpressions +
                ",\n subQueries=" + subQueries +
                ",\n context=" + context +
                ",\n notification=" + notification +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultQuery)) return false;

        DefaultQuery that = (DefaultQuery) o;

        return this.queryType() == that.queryType()
                && argumentExpressions.equals(that.argumentExpressions) 
                && id.equals(that.id) 
                && parameters.equals(that.parameters)
                && requiredSchemaAttributes.equals(that.requiredSchemaAttributes) 
                && subQueries.equals(that.subQueries);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + parameters.hashCode();
        result = 31 * result + requiredSchemaAttributes.hashCode();
        result = 31 * result + argumentExpressions.hashCode();
        result = 31 * result + subQueries.hashCode();
        result = 31 * result + (context != null ? context.hashCode() : 0);
        result = 31 * result + (notification != null ? notification.hashCode() : 0);
        return result;
    }
}
