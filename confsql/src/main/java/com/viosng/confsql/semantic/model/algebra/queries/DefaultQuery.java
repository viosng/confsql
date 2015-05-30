package com.viosng.confsql.semantic.model.algebra.queries;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.special.expr.Parameter;
import com.viosng.confsql.semantic.model.other.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
                                requiredSchemaAttributes.parallelStream().map(q -> q.verify(subQueryContext)),
                                parameters.parallelStream().map(q -> q.verify(superContext))),
                        subQueries.parallelStream().map(q -> q.verify(context))),
                subQueryContexts.parallelStream().map(q -> (Notification) q))
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
        if (o == null || getClass() != o.getClass()) return false;
        DefaultQuery that = (DefaultQuery) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(parameters, that.parameters) &&
                Objects.equals(requiredSchemaAttributes, that.requiredSchemaAttributes) &&
                Objects.equals(subQueries, that.subQueries) &&
                Objects.equals(context.get(), that.context.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parameters, requiredSchemaAttributes, subQueries, context);
    }
}
