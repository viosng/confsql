package com.viosng.confsql.semantic.model.algebra.queries;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.*;
import org.antlr.v4.runtime.misc.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
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

        @NotNull
        @Override
        public Verifier verify(@NotNull Verifier verifier) {
            return new Verifier();
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
            super(id, parameters, Collections.emptyList(), Collections.emptyList(), Collections.<Expression>emptyList());
        }

        @NotNull
        @Override
        public Verifier verify(@NotNull Verifier verifier) {
            return new Verifier().mergeWarnings(getParameters().stream().map(
                    e -> e.verify(verifier)).collect(Verifier::new, Verifier::accept, Verifier::accept));
        }
    }

    @NotNull
    public static Query.Primary primary(@NotNull String id,
                                        @NotNull List<Parameter> parameters) {
        return new PrimaryQuery(id, parameters);
    }

    private static Set<Pair<String, String>> transformAttributes(String newId, Set<Pair<String, String>> attributes) {
        return attributes.stream().map(
                pair -> new Pair<>(newId, (pair.a.equals(UNDEFINED_ID) ? "" : pair.a + ".") + pair.b)).collect(Collectors.toSet());
    }

    private static class FilterQuery extends DefaultQuery implements Query.Filter {
        private FilterQuery(@NotNull String id,
                            @NotNull List<Parameter> parameters,
                            @NotNull List<Expression> requiredSchemaAttributes,
                            @NotNull Query subQuery) {
            super(id, parameters, requiredSchemaAttributes, Arrays.asList(subQuery), Collections.<Expression>emptyList());
        }

        @NotNull
        @Override
        public Verifier verify(@NotNull Verifier verifier) {
            Verifier subQueryVerifier = getSubQueries().get(0).verify(verifier);

            // we don't need to add sub query context to output one, but we need to verify schema with sub query context
            Verifier schemaVerifier = getRequiredSchemaAttributes().stream().map(
                    e -> e.verify(subQueryVerifier)).collect(Verifier::new, Verifier::accept, Verifier::accept);
            Verifier filterVerifier = new Verifier()
                    .mergeWarnings(subQueryVerifier)
                    .mergeWarnings(getParameters().stream().map(
                            e -> e.verify(verifier)).collect(Verifier::new, Verifier::accept, Verifier::accept));
            transformAttributes(id(), schemaVerifier.getAllAttributes()).stream().forEach(pair -> filterVerifier.attribute(pair.a, pair.b));
            return filterVerifier;
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
            super(id, parameters, Collections.<Expression>emptyList(), subQueries, Collections.emptyList());
        }

        @NotNull
        @Override
        public Verifier verify(@NotNull Verifier verifier) {
            Verifier subQueriesVerifier = getSubQueries().stream().map(
                    e -> e.verify(verifier)).collect(Verifier::new, Verifier::accept, Verifier::accept);
            Verifier fusionVerifier = new Verifier().mergeWarnings(subQueriesVerifier);
            transformAttributes(id(), subQueriesVerifier.getAllAttributes()).forEach(pair -> fusionVerifier.attribute(pair.a, pair.b));
            return fusionVerifier;
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

        @NotNull
        @Override
        public Verifier verify(@NotNull Verifier verifier) {
            return getSubQueries().stream().map(q -> q.verify(verifier)).collect(Verifier::new, Verifier::accept, Verifier::accept);
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
                                 @NotNull Query base) {
            super(id, parameters, Collections.<Expression>emptyList(), Arrays.asList(base), Collections.<Expression>emptyList());
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

        @NotNull
        @Override
        public Verifier verify(@NotNull Verifier verifier) {
            Query base = getSubQueries().get(0);
            Verifier baseVerifier = base.verify(verifier);
            return baseVerifier.mergeWarnings(getParameters().stream().map(
                    p -> p.verify(baseVerifier)).collect(Verifier::new, Verifier::accept, Verifier::accept));
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
            super(id, parameters, Collections.emptyList(), Arrays.asList(base), Collections.<Expression>emptyList());
        }

        @NotNull
        @Override
        public Verifier verify(@NotNull Verifier verifier) {
            Query base = getSubQueries().get(0);
            Verifier baseVerifier = base.verify(verifier);
            Verifier unNestVerifier = new Verifier().mergeWarnings(baseVerifier);
            String unNestObject = getParameters().stream().filter(
                    p -> p.id().equals("unNestObject")).findAny().get().getValue().id();
            baseVerifier.getAttributes(unNestObject).stream().forEach(s -> unNestVerifier.attribute(base.id(), s));
            return unNestVerifier;
        }
    }

    @NotNull
    public static Query.UnNest unNest(@NotNull String id,
                                      @NotNull Query base,
                                      @NotNull List<Parameter> parameters) {
        return new UnNestQuery(id, parameters, base);
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

        @NotNull
        @Override
        public Verifier verify(@NotNull Verifier verifier) {
            throw new UnsupportedOperationException();
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
