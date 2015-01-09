package com.viosng.confsql.semantic.model.queries;

import com.google.common.collect.Lists;
import com.viosng.confsql.semantic.model.ModelElement;
import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.DefaultContext;
import com.viosng.confsql.semantic.model.other.Notification;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 27.12.2014
 * Time: 14:31
 */
public abstract class DefaultQuery implements Query{
    @NotNull
    private final String id;
    
    @NotNull
    private final List<Parameter> parameters;
    
    @NotNull
    private final List<Expression> schemaAttributes, requiredSchemaAttributes, argumentExpressions;
    
    @NotNull
    private final List<Query> subQueries;
    
    protected Context context;

    private Notification notification;

    public DefaultQuery(@NotNull String id,
                        @NotNull List<Parameter> parameters,
                        @NotNull List<Expression> requiredSchemaAttributes,
                        @NotNull List<Query> subQueries, @NotNull List<Expression> argumentExpressions) {
        this.id = id;
        this.parameters = parameters;
        this.requiredSchemaAttributes = requiredSchemaAttributes;
        this.schemaAttributes = Arrays.asList(requiredSchemaAttributes.stream()
                .filter(a -> !a.id().equals(ModelElement.UNDEFINED_ID))
                .map(a -> ValueExpressionFactory.attribute(id, a.id()))
                .toArray(Expression[]::new));
        this.argumentExpressions = argumentExpressions;
        this.subQueries = subQueries;
    }

    @NotNull
    @Override
    public String id() {
        return id;
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
    public List<Expression> getSchemaAttributes() {
        return schemaAttributes;
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
                ",\n schemaAttributes=" + schemaAttributes +
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

        if (!argumentExpressions.equals(that.argumentExpressions)) return false;
        if (!id.equals(that.id)) return false;
        if (!parameters.equals(that.parameters)) return false;
        if (!requiredSchemaAttributes.equals(that.requiredSchemaAttributes)) return false;
        if (!subQueries.equals(that.subQueries)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + parameters.hashCode();
        result = 31 * result + requiredSchemaAttributes.hashCode();
        result = 31 * result + argumentExpressions.hashCode();
        result = 31 * result + subQueries.hashCode();
        return result;
    }
}
