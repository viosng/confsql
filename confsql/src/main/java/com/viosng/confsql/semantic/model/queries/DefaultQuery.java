package com.viosng.confsql.semantic.model.queries;

import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;

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
    private final List<Expression> schemaAttributes, argumentExpressions;
    
    @NotNull
    private final List<Query> subQueries;
    
    protected Context context;

    private Notification notification;

    public DefaultQuery(@NotNull String id,
                        @NotNull List<Parameter> parameters,
                        @NotNull List<Expression> schemaAttributes,
                        @NotNull List<Query> subQueries, @NotNull List<Expression> argumentExpressions) {
        this.id = id;
        this.parameters = parameters;
        this.schemaAttributes = schemaAttributes;
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
    protected abstract Context createContext();
    
    @NotNull
    @Override
    public Notification verify() {
        if (notification == null) {
            notification = subQueries.stream().map(Query::verify).collect(Notification::new,
                    Notification::accept, Notification::accept);
            notification.addNotification(schemaAttributes.stream().map(e -> e.verify(getContext())).collect(Notification::new,
                    Notification::accept, Notification::accept));
            notification.addNotification(argumentExpressions.stream().map(e -> e.verify(getContext())).collect(Notification::new,
                    Notification::accept, Notification::accept));
        }
        return notification;
    }

}
