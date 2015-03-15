package com.viosng.confsql.semantic.model.algebra.queries;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.ExpressionImpl;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

import static com.viosng.confsql.semantic.model.algebra.Expression.UNDEFINED_ID;
import static com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpression.AttributeExpression;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 29.12.2014
 * Time: 2:41
 */
public class QueryFactory {

    private QueryFactory() {
    }

    private static List<Expression> combineSchemaAttributes(Stream<List<AttributeExpression>> attributeStream) {
        return new ArrayList<>(attributeStream.map(HashSet<AttributeExpression>::new)
                .<HashSet<AttributeExpression>>collect(HashSet<AttributeExpression>::new,
                        HashSet<AttributeExpression>::addAll, HashSet<AttributeExpression>::addAll));
    }

    private static class FictiveQuery implements Query {

        private FictiveQuery(){}

        @NotNull
        public static FictiveQuery getInstance() {
            return Holder.INSTANCE;
        }

        private static class Holder {
            @NotNull
            private static final FictiveQuery INSTANCE = new FictiveQuery();
        }

        @NotNull
        @Override
        public QueryType queryType() {
            return QueryType.FICTIVE;
        }

        @NotNull
        @Override
        public List<Parameter> getParameters() {
            throw new UnsupportedOperationException();
        }

        @NotNull
        @Override
        public List<Expression> getRequiredSchemaAttributes() {
            throw new UnsupportedOperationException();
        }

        @NotNull
        @Override
        public List<Query> getSubQueries() {
            throw new UnsupportedOperationException();
        }

        @NotNull
        @Override
        public Context getContext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "FictiveQuery";
        }

        @Override
        public boolean equals(Object o) {
            return this == o;
        }

        @Override
        public int hashCode() {
            return FictiveQuery.class.hashCode();
        }
    }

    @NotNull
    public static Query fictive() {
        return FictiveQuery.getInstance();
    }

    private static class PrimaryQuery extends DefaultQuery implements Query.Primary {
        private PrimaryQuery(@NotNull String id,
                             @NotNull List<Parameter> parameters) {
            super(id, parameters, Collections.emptyList(), Collections.emptyList());
        }

        @NotNull
        @Override
        protected Context createContext() {
            return new Context(id());
        }
    }

    @NotNull
    public static Query.Primary primary(@NotNull String id,
                                        @NotNull List<Parameter> parameters) {
        return new PrimaryQuery(id, parameters);
    }

    private static Context updateContextFromSchema(Context context, String id, List<Expression> schema, Query subQuery) {
        Set<String> ids = new HashSet<>();
        for (Expression expression : schema) {
            if (expression.id().equals(UNDEFINED_ID)) {
                context.warning("There is an expression without id");
                continue;
            }

            if (ids.contains(expression.id())) {
                context.warning("There is duplicate id (" + expression.id() + ")");
            } else {
                ids.add(expression.id());
            }

            if (expression instanceof ExpressionImpl) {
                context.addObject(Arrays.asList(id, expression.id()));
                continue;
            }
            switch (expression.type()) {
                case QUERY:
                case CASE:
                case CONSTANT:
                    context.addObject(Arrays.asList(id, expression.id()));
                    break;
                case ATTRIBUTE: {
                    List<String> object = ((AttributeExpression) expression).getObject();
                    if (!object.get(0).equals(subQuery.id())) {
                        List<String> newObject = new ArrayList<>();
                        newObject.add(subQuery.id());
                        newObject.addAll(object);
                        object = newObject;
                    }
                    if (!subQuery.getContext().hasObject(object)) {
                        context.warning("There is no attribute (" + expression.toString() + ")");
                    }
                    context.mergeContext(subQuery.getContext(), object, expression.id());
                    break;
                }
                case FUNCTION_CALL:
                    ValueExpression.FunctionCallExpression functionCallExpression =
                            (ValueExpression.FunctionCallExpression) expression;
                    if (functionCallExpression.getValue().compareToIgnoreCase("nest") == 0) {
                        context = updateContextFromSchema(context, id, functionCallExpression.getArguments(), subQuery);
                    } else {
                        return new Context(id);
                    }
                    break;
            }
        }
        return context;
    }

    private static class FilterQuery extends DefaultQuery implements Query.Filter {
        private FilterQuery(@NotNull String id,
                            @NotNull List<Parameter> parameters,
                            @NotNull List<Expression> requiredSchemaAttributes,
                            @NotNull Query subQuery) {
            super(id, parameters, requiredSchemaAttributes, Arrays.asList(subQuery));
        }


        @NotNull
        @Override
        protected Context createContext() {
            return updateContextFromSchema(new Context(id()), id(), getRequiredSchemaAttributes(), getSubQueries().get(0));
        }
    }

    @NotNull
    public static Query.Filter filter(@NotNull String id,
                                      @NotNull Query base,
                                      @NotNull List<Parameter> parameters,
                                      @NotNull List<Expression> requiredSchemaAttributes) {
        return new FilterQuery(id, parameters, requiredSchemaAttributes, base);
    }

    private static class FusionQuery extends DefaultQuery implements Query.Fusion {

        public FusionQuery(@NotNull String id,
                           @NotNull List<Parameter> parameters,
                           @NotNull List<Query> subQueries) {
            super(id, parameters, Collections.<Expression>emptyList(), subQueries);
        }

        @NotNull
        @Override
        protected Context createContext() {
            return null;
        }
    }

    @NotNull
    public static Query.Fusion fusion(@NotNull String id,
                                      @NotNull List<Parameter> parameters,
                                      @NotNull List<Query> subQueries) {
        return new FusionQuery(id, parameters, subQueries);
    }

    private static class JoinQuery extends DefaultQuery implements Query.Join {

        public JoinQuery(@NotNull String id,
                         @NotNull List<Parameter> parameters,
                         @NotNull Query leftBase,
                         @NotNull Query rightBase) {
            super(id, parameters, Collections.<Expression>emptyList(), Arrays.asList(leftBase, rightBase));
        }

        @NotNull
        @Override
        protected Context createContext() {
            return null;
        }
    }

    @NotNull
    public static Query.Join join(@NotNull String id,
                                  @NotNull List<Parameter> parameters,
                                  @NotNull Query leftBase,
                                  @NotNull Query rightBase) {
        return new JoinQuery(id, parameters, leftBase, rightBase);
    }

    private static class AggregationQuery extends DefaultQuery implements Query.Aggregation {
        private AggregationQuery(@NotNull String id,
                                 @NotNull List<Parameter> parameters,
                                 @NotNull Query base) {
            super(id, parameters, Collections.<Expression>emptyList(), Arrays.asList(base));
        }

        @NotNull
        @Override
        protected Context createContext() {
            return null;
        }
    }



    @NotNull
    public static Query.Aggregation aggregation(@NotNull String id,
                                                @NotNull Query base,
                                                @NotNull List<Parameter> parameters) {
        return new AggregationQuery(id, parameters, base);
    }

    private static class UnNestQuery extends DefaultQuery implements Query.UnNest {
        private UnNestQuery(@NotNull String id,
                            @NotNull List<Parameter> parameters,
                            @NotNull Query base) {
            super(id, parameters, Collections.emptyList(), Arrays.asList(base));
        }

        @NotNull
        @Override
        protected Context createContext() {
            return null;
        }
    }

    @NotNull
    public static Query.UnNest unNest(@NotNull String id,
                                      @NotNull Query base,
                                      @NotNull List<Parameter> parameters) {
        return new UnNestQuery(id, parameters, base);
    }
}
