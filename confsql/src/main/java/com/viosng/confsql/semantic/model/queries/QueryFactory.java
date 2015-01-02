package com.viosng.confsql.semantic.model.queries;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.other.ValueExpression;
import com.viosng.confsql.semantic.model.other.Notification;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 29.12.2014
 * Time: 2:41
 */
public class QueryFactory {

    private QueryFactory() {
    }

    protected static List<Expression> combineSchemaAttributes(List<Expression> left, List<Expression> right) {
        return Lists.newArrayList(Sets.union(Sets.newHashSet(left), Sets.newHashSet(right)));
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

    private static class FusionQuery extends DefaultQuery implements Query.Fusion {

        public FusionQuery(@NotNull String id,
                           @NotNull List<Parameter> parameters,
                           @NotNull Query leftBase,
                           @NotNull Query rightBase) {
            super(id, parameters, combineSchemaAttributes(leftBase.getSchemaAttributes(), rightBase.getSchemaAttributes()),
                    Arrays.asList(leftBase, rightBase), Collections.emptyList());
        }
    }

    public static Query.Fusion fusion(@NotNull String id,
                                      @NotNull List<Parameter> parameters,
                                      @NotNull Query leftBase,
                                      @NotNull Query rightBase) {
        return new FusionQuery(id, parameters, leftBase, rightBase);
    }

    private static class JoinQuery extends DefaultQuery implements Query.Join {

        public JoinQuery(@NotNull String id,
                         @NotNull List<Parameter> parameters,
                         @NotNull Query leftBase,
                         @NotNull Query rightBase,
                         @NotNull List<Expression> argumentExpressions) {
            super(id, parameters, combineSchemaAttributes(leftBase.getSchemaAttributes(), rightBase.getSchemaAttributes()),
                    Arrays.asList(leftBase, rightBase), argumentExpressions);
        }
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
            if (!getSchemaAttributes().stream().anyMatch(s -> s.containsType(Expression.Type.GROUP))) {
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

    private static class UnNestQuery extends DefaultQuery implements Query.UnNest {
        private UnNestQuery(@NotNull String id,
                            @NotNull List<Parameter> parameters,
                            @NotNull Query base,
                            @NotNull ValueExpression.AttributeExpression attribute) {
            super(id, parameters, Collections.emptyList(), Arrays.asList(base), Arrays.asList(attribute));
        }
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
