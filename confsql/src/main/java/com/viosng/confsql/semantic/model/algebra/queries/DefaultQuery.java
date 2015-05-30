package com.viosng.confsql.semantic.model.algebra.queries;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.special.expr.Parameter;
import com.viosng.confsql.semantic.model.other.*;
import com.viosng.confsql.semantic.model.other.CacheableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 27.12.2014
 * Time: 14:31
 */
public abstract class DefaultQuery implements Query{
    @NotNull
    private volatile String id;
    
    @NotNull
    private final List<Parameter> parameters;
    
    @NotNull
    private final List<Expression> requiredSchemaAttributes;
    
    @NotNull
    private final List<Query> subQueries;

    @NotNull
    private final CacheableValue<QueryContext> context = new CacheableValue<>();

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
        return context.orElseGet(this::createContext);
    }

    @NotNull
    protected abstract QueryContext createContext();

    @NotNull
    @Override
    public Notification verify(Context context) {
        List<Context> subQueryContexts = subQueries.stream().map(Query::getContext).collect(Collectors.toList());
        Context subQueryContext = new SuperContext(subQueryContexts);
        Context superContext = new SuperContext(Arrays.asList(context, subQueryContext)); //todo add new constructor
        return Stream.concat(
                Stream.concat(
                        Stream.concat(
                                requiredSchemaAttributes.stream().map(q -> q.verify(subQueryContext)),
                                parameters.stream().map(q -> q.verify(superContext))),
                        subQueries.stream().map(q -> q.verify(context))),
                subQueryContexts.stream().map(q -> (Notification) q))
                .collect(new NotificationCollector());
    }

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
        result = 31 * result + context.hashCode();
        return result;
    }
}
