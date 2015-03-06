package com.viosng.confsql.semantic.model.algebra.queries;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebraold.expressions.other.ValueExpression;
import com.viosng.confsql.semantic.model.algebraold.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.viosng.confsql.semantic.model.algebraold.expressions.other.ValueExpression.AttributeExpression;

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

    @NotNull
    public static Query fictive() {
        return new Query() {
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
            public List<AttributeExpression> getQueryObjectAttributes() {
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
            public List<Expression> getArgumentExpressions() {
                throw new UnsupportedOperationException();
            }

            @NotNull
            @Override
            public Context getContext() {
                throw new UnsupportedOperationException();
            }

            @NotNull
            @Override
            public Notification verify() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static class PrimaryQuery extends DefaultQuery implements Query.Primary {
        private PrimaryQuery(@NotNull String id,
                             @NotNull List<Parameter> parameters,
                             @NotNull List<Expression> argumentExpressions) {
            super(id, parameters, Collections.emptyList(), Collections.emptyList(), argumentExpressions);
        }
    }

    @NotNull
    public static Query.Primary primary(@NotNull String id,
                                        @NotNull List<Expression> argumentExpressions,
                                        @NotNull List<Parameter> parameters) {
        return new PrimaryQuery(id, parameters, argumentExpressions);
    }
    
    private static class FilterQuery extends DefaultQuery implements Query.Filter {
        private FilterQuery(@NotNull String id,
                            @NotNull List<Parameter> parameters,
                            @NotNull List<Expression> requiredSchemaAttributes,
                            @NotNull Query subQuery,
                            @NotNull List<Expression> argumentExpressions) {
            super(id, parameters, requiredSchemaAttributes, Arrays.asList(subQuery), argumentExpressions);
        }
        
    }

    @NotNull
    public static Query.Filter filter(@NotNull String id,
                                      @NotNull Query base,
                                      @NotNull List<Expression> argumentExpressions,
                                      @NotNull List<Parameter> parameters,
                                      @NotNull List<Expression> requiredSchemaAttributes) {
        return new FilterQuery(id, parameters, requiredSchemaAttributes, base, argumentExpressions);
    }

    private static class FusionQuery extends DefaultQuery implements Query.Fusion {

        public FusionQuery(@NotNull String id,
                           @NotNull List<Parameter> parameters,
                           @NotNull List<Query> subQueries) {
            super(id, parameters, combineSchemaAttributes(subQueries.stream().map(Query::getQueryObjectAttributes)),
                    subQueries, Collections.emptyList());
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
                         @NotNull Query rightBase,
                         @NotNull List<Expression> argumentExpressions) {
            super(id, parameters, combineSchemaAttributes(Arrays.asList(leftBase, rightBase).stream().map(Query::getQueryObjectAttributes)),
                    Arrays.asList(leftBase, rightBase), argumentExpressions);
        }
    }

    @NotNull
    public static Query.Join join(@NotNull String id,
                                  @NotNull List<Parameter> parameters,
                                  @NotNull Query leftBase,
                                  @NotNull Query rightBase,
                                  @NotNull List<Expression> argumentExpressions) {
        return new JoinQuery(id, parameters, leftBase, rightBase, argumentExpressions);
    }

    private static class AggregationQuery extends DefaultQuery implements Query.Aggregation {
        private AggregationQuery(@NotNull String id,
                                 @NotNull List<Parameter> parameters,
                                 @NotNull List<Expression> requiredSchemaAttributes,
                                 @NotNull Query base,
                                 @NotNull List<Expression> argumentExpressions) {
            super(id, parameters, requiredSchemaAttributes, Arrays.asList(base), argumentExpressions);
        }

        @NotNull
        @Override
        public Notification verify() {
            Notification notification = super.verify();
            if (!getRequiredSchemaAttributes().stream().anyMatch(s -> s.findExpressionByType(ArithmeticType.GROUP) != null)) {
                notification.error("Aggregation operation with id = \"" + id() + 
                        "\" has no group operation result reference in schema attributes");
            }
            return  notification;
        }
    }

    @NotNull
    public static Query.Aggregation aggregation(@NotNull String id,
                                                @NotNull Query base,
                                                @NotNull List<Expression> argumentExpressions,
                                                @NotNull List<Parameter> parameters,
                                                @NotNull List<Expression> requiredSchemaAttributes) {
        return new AggregationQuery(id, parameters, requiredSchemaAttributes, base, argumentExpressions);
    }

    private static class NestQuery extends DefaultQuery implements Query.Nest {
        private NestQuery(@NotNull String id,
                          @NotNull List<Parameter> parameters,
                          @NotNull List<Expression> requiredSchemaAttributes,
                          @NotNull Query base) {
            super(id, parameters, requiredSchemaAttributes, Arrays.asList(base), Collections.emptyList());
        }

        @NotNull
        @Override
        public Notification verify() {
            Notification notification = super.verify();
            if (!getRequiredSchemaAttributes().stream().anyMatch(s -> s.type() == ArithmeticType.GROUP)) {
                notification.error("Nest operation with id = \"" + id() +
                        "\" has no group operation result reference in schema attributes");
            }
            return  notification;
        }
    }

    @NotNull
    public static Query.Nest nest(@NotNull String id,
                                  @NotNull Query base,
                                  @NotNull List<Parameter> parameters,
                                  @NotNull List<Expression> requiredSchemaAttributes) {
        return new NestQuery(id, parameters, requiredSchemaAttributes, base);
    }

    @NotNull
    private static List<AttributeExpression> unNestSchemaGroup(@NotNull String queryId,
                                                               @NotNull String groupId, 
                                                               @NotNull List<AttributeExpression> requiredSchemaAttributes) {
        List<AttributeExpression> newSchemaAttributes = new ArrayList<>(requiredSchemaAttributes.size());
        for (AttributeExpression schemaAttribute : requiredSchemaAttributes) {
            if (schemaAttribute.id().equals(groupId) && schemaAttribute.type().equals(ArithmeticType.GROUP) &&
                    schemaAttribute instanceof ValueExpression.GroupExpression) {
                ValueExpression.GroupExpression groupExpression = (ValueExpression.GroupExpression)schemaAttribute;
                newSchemaAttributes.addAll(groupExpression.getGroupedAttributes().stream()
                        .filter(a -> !a.id().equals(Expression.UNDEFINED_ID))
                        .map(a -> ValueExpressionFactory.attribute(queryId, a.id()))
                        .collect(Collectors.toList()));
            } else {
                newSchemaAttributes.add(schemaAttribute);
            }
        }
        return newSchemaAttributes;
    }

    private static class UnNestQuery extends DefaultQuery implements Query.UnNest {
        private UnNestQuery(@NotNull String id,
                            @NotNull List<Parameter> parameters,
                            @NotNull Query base,
                            @NotNull AttributeExpression attribute) {
            super(id, parameters, Collections.emptyList(), Arrays.asList(base), Arrays.asList(attribute));
            this.queryObjectAttributes = unNestSchemaGroup(id, attribute.id(), base.getQueryObjectAttributes());
        }
    }

    @NotNull
    public static Query.UnNest unNest(@NotNull String id,
                                      @NotNull Query base,
                                      @NotNull AttributeExpression attribute,
                                      @NotNull List<Parameter> parameters) {
        return new UnNestQuery(id, parameters, base, attribute);
    }

    private static class GroupJoinQuery extends DefaultQuery implements Query.GroupJoin {

        public GroupJoinQuery(@NotNull String id,
                              @NotNull List<Parameter> parameters,
                              @NotNull List<Expression> requiredSchemaAttributes,
                              @NotNull Query leftBase,
                              @NotNull Query rightBase,
                              @NotNull List<Expression> argumentExpressions) {
            super(id, parameters, requiredSchemaAttributes, Arrays.asList(leftBase, rightBase), argumentExpressions);
        }

        @NotNull
        @Override
        public Notification verify() {
            Notification notification = super.verify();
            if (!getRequiredSchemaAttributes().stream().anyMatch(s -> s.findExpressionByType(ArithmeticType.GROUP) != null)) {
                notification.error("GroupJoin operation with id = \"" + id() +
                        "\" has no group operation result reference in schema attributes");
            }
            return  notification;
        }
    }

    @NotNull
    public static Query.GroupJoin groupJoin(@NotNull String id,
                                            @NotNull Query leftBase,
                                            @NotNull Query rightBase,
                                            @NotNull List<Expression> argumentExpressions,
                                            @NotNull List<Parameter> parameters,
                                            @NotNull List<Expression> requiredSchemaAttributes) {
        return new GroupJoinQuery(id, parameters, requiredSchemaAttributes, leftBase, rightBase, argumentExpressions);
    }
}
