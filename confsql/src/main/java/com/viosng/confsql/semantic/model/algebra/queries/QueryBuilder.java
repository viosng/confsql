package com.viosng.confsql.semantic.model.algebra.queries;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpression;
import com.viosng.confsql.semantic.model.algebra.special.expr.Parameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 05.01.2015
 * Time: 4:19
 */
public class QueryBuilder {
    @NotNull
    private String id = Expression.UNDEFINED_ID;

    @Nullable
    private Query.QueryType queryType;

    @NotNull
    private List<Parameter> parameters = Collections.emptyList();

    @NotNull
    private List<Expression> schemaAttributes = Collections.emptyList(), argumentExpressions = Collections.emptyList();

    @NotNull
    private List<Query> subQueries = Collections.emptyList();

    @NotNull
    public QueryBuilder id(@Nullable String id) {
        if (id != null) this.id = id;
        return this;
    }

    @NotNull
    public QueryBuilder queryType(@NotNull Query.QueryType queryType) {
        this.queryType = queryType;
        return this;
    }

    @NotNull
    public QueryBuilder parameters(@NotNull List<Parameter> parameters) {
        this.parameters = parameters;
        return this;
    }

    @NotNull
    public QueryBuilder parameters(Parameter... parameters) {
        if (parameters != null) this.parameters = Arrays.asList(parameters);
        return this;
    }

    @NotNull
    public QueryBuilder requiredSchemaAttributes(@NotNull List<Expression> schemaAttributes) {
        this.schemaAttributes = schemaAttributes;
        return this;
    }

    @NotNull
    public QueryBuilder requiredSchemaAttributes(Expression... schemaAttributes) {
        if (schemaAttributes != null) this.schemaAttributes = Arrays.asList(schemaAttributes);
        return this;
    }

    @NotNull
    public QueryBuilder argumentExpressions(@NotNull List<Expression> argumentExpressions) {
        this.argumentExpressions = argumentExpressions;
        return this;
    }

    @NotNull
    public QueryBuilder argumentExpressions(Expression... argumentExpressions) {
        if (argumentExpressions != null) this.argumentExpressions = Arrays.asList(argumentExpressions);
        return this;
    }

    @NotNull
    public QueryBuilder subQueries(@NotNull List<Query> subQueries) {
        this.subQueries = subQueries;
        return this;
    }

    @NotNull
    public QueryBuilder subQueries(Query... subQueries) {
        if (subQueries != null) this.subQueries = Arrays.asList(subQueries);
        return this;
    }
    
    public Query create() {
        return create(this.queryType);
    }

    public Query create(Query.QueryType queryType) {
        if (queryType == null) throw new IllegalArgumentException("QueryType reference is null");
        switch (queryType) {
            case PRIMARY:
                String queryId = id;
                if (queryId.equals(Expression.UNDEFINED_ID)) {
                    queryId = ((ValueExpression.ConstantExpression)parameters.stream().filter(p -> p.id().equals("sourceName"))
                            .findAny().get().getValue()).getValue();
                }
                return QueryFactory.primary(queryId, parameters);
            case FILTER:
                checkSubQueriesCount(1);
                return QueryFactory.filter(id, subQueries.get(0), parameters, schemaAttributes);
            case FUSION:
                if (subQueries.isEmpty()) {
                    throw new IllegalArgumentException("Empty sub queries list");
                }
                return QueryFactory.fusion(id, parameters, subQueries);
            case JOIN:
                checkSubQueriesCount(2);
                return QueryFactory.join(id, parameters, subQueries);
            case AGGREGATION:
                checkSubQueriesCount(1);
                return QueryFactory.aggregation(id, subQueries.get(0), parameters);
            case UNNEST:
                checkSubQueriesCount(1);
                return QueryFactory.unNest(id, subQueries.get(0), parameters);
            default: throw new UnsupportedOperationException("Unknown query type");
        }
    }
    
    private void checkSubQueriesCount(int size) {
        if (subQueries.size() < size) {
            throw new IllegalArgumentException("Wrong number of sub queries, should be " + size);
        }
        if (subQueries.stream().filter(s -> s == null).toArray().length > 0) {
            throw new IllegalArgumentException("Sub queries list contains null elements");
        }
    }
    
}
