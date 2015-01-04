package com.viosng.confsql.semantic.model.queries;

import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.other.ValueExpression;
import com.viosng.confsql.semantic.model.other.Notification;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 29.12.2014
 * Time: 2:41
 */
public class QueryFactory {

    private QueryFactory() {
    }

    protected static List<Expression> combineSchemaAttributes(Stream<List<Expression>> attributeStream) {
        return new ArrayList<>(attributeStream.map(HashSet<Expression>::new).
                <HashSet<Expression>>collect(HashSet<Expression>::new, HashSet<Expression>::addAll, HashSet<Expression>::addAll));
    }

    private static class PrimaryQuery extends DefaultQuery implements Query.Primary {
        private PrimaryQuery(@NotNull String id,
                             @NotNull List<Parameter> parameters,
                             @NotNull List<Expression> argumentExpressions) {
            super(id, parameters, Collections.emptyList(), Collections.emptyList(), argumentExpressions);
        }
    }

    public static Query.Primary primary(@NotNull String id,
                                        @NotNull List<Expression> argumentExpressions,
                                        @NotNull List<Parameter> parameters) {
        return new PrimaryQuery(id, parameters, argumentExpressions);
    }
    
    private static class FilterQuery extends DefaultQuery implements Query.Filter {
        private FilterQuery(@NotNull String id,
                            @NotNull List<Parameter> parameters,
                            @NotNull List<Expression> schemaAttributes,
                            @NotNull Query subQuery,
                            @NotNull List<Expression> argumentExpressions) {
            super(id, parameters, schemaAttributes, Arrays.asList(subQuery), argumentExpressions);
        }
        
    }

    public static Query.Filter filter(@NotNull String id,
                                      @NotNull Query base,
                                      @NotNull List<Expression> argumentExpressions,
                                      @NotNull List<Parameter> parameters,
                                      @NotNull List<Expression> schemaAttributes) {
        return new FilterQuery(id, parameters, schemaAttributes, base, argumentExpressions);
    }

    // todo add sub query list, not only two
    private static class FusionQuery extends DefaultQuery implements Query.Fusion {

        public FusionQuery(@NotNull String id,
                           @NotNull List<Parameter> parameters,
                           @NotNull List<Query> subQueries) {
            super(id, parameters, combineSchemaAttributes(subQueries.stream().map(Query::getSchemaAttributes)),
                    subQueries, Collections.emptyList());
        }
        //todo  verify scopes
    }

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
            super(id, parameters, combineSchemaAttributes(Arrays.asList(leftBase, rightBase).stream().map(Query::getSchemaAttributes)),
                    Arrays.asList(leftBase, rightBase), argumentExpressions);
        }
        //todo  verify scopes
        
    }

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
                                 @NotNull List<Expression> schemaAttributes,
                                 @NotNull Query base,
                                 @NotNull List<Expression> argumentExpressions) {
            super(id, parameters, schemaAttributes, Arrays.asList(base), argumentExpressions);
        }

        @NotNull
        @Override
        public Notification verify() {
            Notification notification = super.verify();
            if (!getSchemaAttributes().stream().anyMatch(s -> s.getExpression(Expression.Type.GROUP) != null)) {
                notification.error("Aggregation operation with id = \"" + id() + 
                        "\" has no group operation result reference in schema attributes");
            }
            return  notification;
        }
    }

    public static Query.Aggregation aggregation(@NotNull String id,
                                                @NotNull Query base,
                                                @NotNull List<Expression> argumentExpressions,
                                                @NotNull List<Parameter> parameters,
                                                @NotNull List<Expression> schemaAttributes) {
        return new AggregationQuery(id, parameters, schemaAttributes, base, argumentExpressions);
    }

    private static class NestQuery extends DefaultQuery implements Query.Nest {
        private NestQuery(@NotNull String id,
                          @NotNull List<Parameter> parameters,
                          @NotNull List<Expression> schemaAttributes,
                          @NotNull Query base) {
            super(id, parameters, schemaAttributes, Arrays.asList(base), Collections.emptyList());
        }

        @NotNull
        @Override
        public Notification verify() {
            Notification notification = super.verify();
            if (!getSchemaAttributes().stream().anyMatch(s -> s.type() == Expression.Type.GROUP)) {
                notification.error("Nest operation with id = \"" + id() +
                        "\" has no group operation result reference in schema attributes");
            }
            return  notification;
        }
    }

    public static Query.Nest nest(@NotNull String id,
                                  @NotNull Query base,
                                  @NotNull List<Parameter> parameters,
                                  @NotNull List<Expression> schemaAttributes) {
        return new NestQuery(id, parameters, schemaAttributes, base);
    }
    
    private static List<Expression> unNestSchemaGroup(@NotNull List<Expression> schemaAttributes, String groupId) {
        List<Expression> newSchemaAttributes = new ArrayList<>(schemaAttributes.size());
        for (Expression schemaAttribute : schemaAttributes) {
            if (schemaAttribute.id().equals(groupId) && schemaAttribute.type().equals(Expression.Type.GROUP) &&
                    schemaAttribute instanceof ValueExpression.GroupExpression) {
                ValueExpression.GroupExpression groupExpression = (ValueExpression.GroupExpression)schemaAttribute;
                for (Expression attribute : groupExpression.getGroupedAttributes()) {
                    newSchemaAttributes.add(attribute);
                }
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
                            @NotNull ValueExpression.AttributeExpression attribute) {
            super(id, parameters, unNestSchemaGroup(base.getSchemaAttributes(), attribute.id()), Arrays.asList(base), Arrays.asList(attribute));
        }
        //todo  verify scopes
    }

    public static Query.UnNest unNest(@NotNull String id,
                                      @NotNull Query base,
                                      @NotNull ValueExpression.AttributeExpression attribute,
                                      @NotNull List<Parameter> parameters) {
        return new UnNestQuery(id, parameters, base, attribute);
    }

    private static class GroupJoinQuery extends DefaultQuery implements Query.GroupJoin {

        public GroupJoinQuery(@NotNull String id,
                              @NotNull List<Parameter> parameters,
                              @NotNull List<Expression> schemaAttributes,
                              @NotNull Query leftBase,
                              @NotNull Query rightBase,
                              @NotNull List<Expression> argumentExpressions) {
            super(id, parameters, schemaAttributes, Arrays.asList(leftBase, rightBase), argumentExpressions);
        }

        @NotNull
        @Override
        public Notification verify() { //todo
            return super.verify();
        }
    }
    
    public static Query.GroupJoin groupJoin(@NotNull String id,
                                            @NotNull Query leftBase,
                                            @NotNull Query rightBase,
                                            @NotNull List<Expression> argumentExpressions,
                                            @NotNull List<Parameter> parameters,
                                            @NotNull List<Expression> schemaAttributes) {
        return new GroupJoinQuery(id, parameters, schemaAttributes, leftBase, rightBase, argumentExpressions);
    }
}
