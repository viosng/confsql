package com.viosng.confsql.semantic.model.queries;

import com.viosng.confsql.semantic.model.ModelElement;
import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.other.ValueExpression;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.Contract;
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
    private String id = ModelElement.UNDEFINED_ID;

    @Nullable
    private Query.Type type;

    @NotNull
    private List<Parameter> parameters = Collections.emptyList();

    @NotNull
    private List<Expression> schemaAttributes = Collections.emptyList(), argumentExpressions = Collections.emptyList();

    @NotNull
    private List<Query> subQueries = Collections.emptyList();

    @NotNull
    public QueryBuilder setId(@NotNull String id) {
        this.id = id;
        return this;
    }

    @NotNull
    public QueryBuilder setType(@NotNull Query.Type type) {
        this.type = type;
        return this;
    }

    @NotNull
    public QueryBuilder setParameters(@NotNull List<Parameter> parameters) {
        this.parameters = parameters;
        return this;
    }

    @NotNull
    public QueryBuilder setParameters(Parameter... parameters) {
        if (parameters != null) this.parameters = Arrays.asList(parameters);
        return this;
    }

    @NotNull
    public QueryBuilder setSchemaAttributes(@NotNull List<Expression> schemaAttributes) {
        this.schemaAttributes = schemaAttributes;
        return this;
    }

    @NotNull
    public QueryBuilder setSchemaAttributes(Expression... schemaAttributes) {
        if (schemaAttributes != null) this.schemaAttributes = Arrays.asList(schemaAttributes);
        return this;
    }

    @NotNull
    public QueryBuilder setArgumentExpressions(@NotNull List<Expression> argumentExpressions) {
        this.argumentExpressions = argumentExpressions;
        return this;
    }

    @NotNull
    public QueryBuilder setArgumentExpressions(Expression... argumentExpressions) {
        if (argumentExpressions != null) this.argumentExpressions = Arrays.asList(argumentExpressions);
        return this;
    }

    @NotNull
    public QueryBuilder setSubQueries(@NotNull List<Query> subQueries) {
        this.subQueries = subQueries;
        return this;
    }

    @NotNull
    public QueryBuilder setSubQueries(Query... subQueries) {
        if (subQueries != null) this.subQueries = Arrays.asList(subQueries);
        return this;
    }
    
    public Query create() {
        return create(this.type);
    }

    @Contract("null -> null")
    public Query create(Query.Type type) {
        if (type == null) return null;
        switch (type) {
            case PRIMARY:
                return QueryFactory.primary(id, argumentExpressions, parameters);
            case FILTER:
                checkSubQueriesNumber(1);
                return QueryFactory.filter(id, subQueries.get(0), argumentExpressions, parameters, schemaAttributes);
            case FUSION:
                if (subQueries.isEmpty()) {
                    throw new IllegalArgumentException("Empty sub queries list");
                }
                return QueryFactory.fusion(id, parameters, subQueries);
            case JOIN:
                checkSubQueriesNumber(2);
                return QueryFactory.join(id, parameters, subQueries.get(0), subQueries.get(1), argumentExpressions);
            case AGGREGATION:
                checkSubQueriesNumber(1);
                return QueryFactory.aggregation(id, subQueries.get(0), argumentExpressions, parameters, schemaAttributes);
            case NEST:
                checkSubQueriesNumber(1);
                return QueryFactory.nest(id, subQueries.get(0), parameters, schemaAttributes);
            case UNNEST:
                checkSubQueriesNumber(1);
                if (argumentExpressions.size() != 1 || 
                        argumentExpressions.get(0) == null || 
                        !(argumentExpressions.get(0) instanceof ValueExpression.AttributeExpression)) {
                    throw new IllegalArgumentException("Unnest argument expression list should contain only one not null " +
                            "element with AttributeExpression type");
                }
                return QueryFactory.unNest(id, subQueries.get(0), 
                        (ValueExpression.AttributeExpression)argumentExpressions.get(0), parameters);
            case GROUP_JOIN:
                checkSubQueriesNumber(2);
                return QueryFactory.groupJoin(id, subQueries.get(0), subQueries.get(1), argumentExpressions, parameters, 
                        schemaAttributes);
            default: throw new UnsupportedOperationException("Unknown query type");
        }
    }
    
    private void checkSubQueriesNumber(int size) {
        if (subQueries.size() != size) {
            throw new IllegalArgumentException("Wrong number of sub queries, should be " + size);
        }
        if (subQueries.stream().filter(s -> s == null).toArray().length > 0) {
            throw new IllegalArgumentException("Sub queries list contains null elements");
        }
    }
    
}