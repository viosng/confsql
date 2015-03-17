package com.viosng.confsql.semantic.model.algebra.queries;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.QueryContext;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
    private final List<Expression> requiredSchemaAttributes;
    
    @NotNull
    private final List<Query> subQueries;
    
    private QueryContext context;

    public DefaultQuery(@NotNull String id,
                        @NotNull List<Parameter> parameters,
                        @NotNull List<Expression> requiredSchemaAttributes,
                        @NotNull List<Query> subQueries) {
        this.id = id;
        this.parameters = parameters;
        this.requiredSchemaAttributes = requiredSchemaAttributes;
        this.subQueries = subQueries;
    }

    @NotNull
    @Override
    public String id() {
        return id;
    }

    @Override
    public void setId(@Nullable String id) {
        if (id != null) this.id = id;
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
    public List<Query> getSubQueries() {
        return subQueries;
    }

    @NotNull
    @Override
    public QueryContext getContext() {
        if (context == null) {
            context = createContext();
        }
        return context;
    }

    @NotNull
    protected abstract QueryContext createContext();

    @Override
    public String toString() {
        return "DefaultQuery{\n" +
                "id='" + id + '\'' +
                ",\n parameters=" + parameters +
                ",\n requiredSchemaAttributes=" + requiredSchemaAttributes +
                ",\n subQueries=" + subQueries +
                ",\n context=" + context +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultQuery)) return false;

        DefaultQuery that = (DefaultQuery) o;

        return this.queryType() == that.queryType()
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
        result = 31 * result + subQueries.hashCode();
        result = 31 * result + (context != null ? context.hashCode() : 0);
        return result;
    }
}
