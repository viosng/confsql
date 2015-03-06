package com.viosng.confsql.semantic.model.algebraold;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.queries.QueryFactory;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebraold.expressions.binary.BinaryArithmeticExpressionFactory;
import com.viosng.confsql.semantic.model.algebraold.expressions.binary.BinaryPredicateExpressionFactory;
import com.viosng.confsql.semantic.model.algebraold.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 09.01.2015
 * Time: 0:58
 */
public class QueryContextTest {

    private static final List<Parameter> PARAMETERS = Arrays.asList(
            new Parameter("constA", ValueExpressionFactory.constant("valA")),
            new Parameter("constB", ValueExpressionFactory.constant("valB")),
            new Parameter("constC", ValueExpressionFactory.constant("valC"))
    );

    private Query.Primary createPrimary() {
        return QueryFactory.primary("primary", Arrays.asList(ValueExpressionFactory.constant("sourcePath")), PARAMETERS);
    }

    @Test
    public void testPrimary() throws Exception {
        Query.Primary primary = createPrimary();
        assertTrue(primary.verify().isOk());
    }
    
    private static List<Expression> createSchemaAttributes(String objectName, List<String> attributes) {
        return attributes.stream().map(a -> ValueExpressionFactory.attribute(objectName, a)).collect(Collectors.toList());
    }

    private Query.Filter createFilter(String objectName, List<String> attributes) {
        List<Expression> schemaAttributes = createSchemaAttributes("primary", attributes);
        return QueryFactory.filter(objectName, createPrimary(), emptyList(), PARAMETERS, schemaAttributes);
    }

    @Test
    public void testFilter() throws Exception {
        Query.Filter filter = createFilter("filter", Arrays.asList("fieldA", "fieldB", "fieldC"));
        assertTrue(filter.verify().isOk());
        List<Expression> argumentExpressions = Arrays.asList(
                BinaryPredicateExpressionFactory.less(ValueExpressionFactory.attribute("filter", "fieldD"),
                        ValueExpressionFactory.constant("40"))
        );
        filter = QueryFactory.filter("filter", filter, argumentExpressions, PARAMETERS, emptyList());
        assertFalse(filter.verify().isOk());
    }

    @Test
    public void testFusion() throws Exception {
        Map<String, List<String>> testData = ImmutableMap.of(
                "filter1", Arrays.asList("fieldA", "fieldB", "fieldC"),
                "filter2", Arrays.asList("fieldD", "fieldE"),
                "filter3", Arrays.asList("fieldF", "fieldG", "fieldH")
        );
        List<Query> filters = testData.entrySet().stream()
                .map(e -> createFilter(e.getKey(), e.getValue())).collect(Collectors.toList());
        Query.Fusion fusion = QueryFactory.fusion("fusion", PARAMETERS, filters);
        Context context = fusion.getContext();
        testData.entrySet().stream().forEach(e -> {
            assertTrue(context.hasReference(e.getKey()));
            e.getValue().stream().forEach(a -> assertTrue(context.hasAttribute(e.getKey(), a)));
        });
        
        Map<String, List<String>> wrongTestData = ImmutableMap.of(
                "filter3", Arrays.asList("fieldA", "fieldB", "fieldC"),
                "filter1", Arrays.asList("fieldD", "fieldE"),
                "filter2", Arrays.asList("fieldF", "fieldG", "fieldH"),
                "filter4", Arrays.asList("fieldQ")
        );
        wrongTestData.entrySet().stream()
                .forEach(e -> e.getValue().stream()
                        .forEach(a -> assertFalse(String.format("Object \"%s\" has attribute \"%s\"", e.getKey(), a),
                                context.hasAttribute(e.getKey(), a))));
    }

    @Test
    public void testJoin() throws Exception {
        Query.Filter filter1 = createFilter("filter1", Arrays.asList("fieldA", "fieldB", "fieldC"));
        Query.Filter filter2 = createFilter("filter2", Arrays.asList("fieldD", "fieldE"));
        List<Expression> argumentExpressions = new ArrayList<>(Arrays.asList(
            BinaryPredicateExpressionFactory.less(ValueExpressionFactory.attribute("filter2","fieldD"),
                    ValueExpressionFactory.constant("40")),
                    ValueExpressionFactory.attribute("filter1","fieldA"),
                    ValueExpressionFactory.attribute("filter1","fieldB"),
                    ValueExpressionFactory.attribute("filter1","fieldC"),
                    ValueExpressionFactory.attribute("filter2","fieldE")
        ));
        
        Query.Join join = QueryFactory.join("join", PARAMETERS, filter1, filter2, argumentExpressions);
        assertTrue(filter1.verify().toString(), filter1.verify().isOk());
        assertTrue(filter2.verify().toString(), filter2.verify().isOk());
        assertTrue(join.verify().toString(), join.verify().isOk());
        
        argumentExpressions.add(ValueExpressionFactory.attribute("filter2", "fieldF"));
        assertTrue(join.verify().toString(), join.verify().isOk());
        
        join = QueryFactory.join("join", PARAMETERS, filter1, filter2, argumentExpressions);
        assertFalse(join.verify().toString(), join.verify().isOk());

        filter2 = createFilter("filter2", Arrays.asList("fieldD", "fieldE", "fieldF"));
        List<Expression> requiredSchemaAttributes = new ArrayList<>(filter2.getRequiredSchemaAttributes());
        requiredSchemaAttributes.add(BinaryPredicateExpressionFactory.less(ValueExpressionFactory.attribute("primary","fieldD"),
                ValueExpressionFactory.constant("40")));
        join = QueryFactory.join("join", PARAMETERS, filter1, QueryFactory.filter("filter2", filter2.getArg(), 
                filter2.getArgumentExpressions(), filter2.getParameters(), requiredSchemaAttributes), argumentExpressions);
        argumentExpressions.add(ValueExpressionFactory.attribute("filter2", "expr"));
        assertFalse(join.verify().toString(), join.verify().isOk());

        requiredSchemaAttributes.add(BinaryPredicateExpressionFactory.less(ValueExpressionFactory.attribute("primary", "fieldD"),
                ValueExpressionFactory.constant("40"), "expr"));
        join = QueryFactory.join("join", PARAMETERS, filter1, QueryFactory.filter("filter2", filter2.getArg(),
                filter2.getArgumentExpressions(), filter2.getParameters(), requiredSchemaAttributes), argumentExpressions);
        assertTrue(join.verify().toString(), join.verify().isOk());
    }

    @Test
    public void testAggregation() throws Exception {
        Query subQuery = mock(Query.class);
        when(subQuery.id()).thenReturn("subQuery");
        when(subQuery.verify()).thenReturn(new Notification());
        List<String> attributes = Arrays.asList("a", "b", "c", "age");
        List<Expression> requiredSchemaAttributes = new ArrayList<>(createSchemaAttributes("subQuery", attributes));
        when(subQuery.getQueryObjectAttributes()).thenReturn(attributes.stream()
                .map(a -> ValueExpressionFactory.attribute("subQuery", a)).collect(Collectors.toList()));
        Query.Aggregation aggregation = QueryFactory.aggregation("aggregation", subQuery, emptyList(), emptyList(), 
                requiredSchemaAttributes);
        assertFalse(aggregation.verify().isOk());
        
        requiredSchemaAttributes.add(ValueExpressionFactory.functionCall("sum", Arrays.asList(ValueExpressionFactory.constant("1"))));
        aggregation = QueryFactory.aggregation("aggregation", subQuery, emptyList(), emptyList(), requiredSchemaAttributes);
        assertFalse(aggregation.verify().toString(), aggregation.verify().isOk());

        requiredSchemaAttributes.add(ValueExpressionFactory.functionCall("sum",
                Arrays.asList(ValueExpressionFactory.group("subQuery", "ages",
                        Arrays.asList(ValueExpressionFactory.attribute("subQuery", "age"))))));
        aggregation = QueryFactory.aggregation("aggregation", subQuery, emptyList(), emptyList(), requiredSchemaAttributes);
        assertTrue(aggregation.verify().toString(), aggregation.verify().isOk());
    }
    
    @Test
    public void testNest() throws Exception {
        Query subQuery = mock(Query.class);
        when(subQuery.id()).thenReturn("subQuery");
        when(subQuery.verify()).thenReturn(new Notification());
        List<String> attributes = Arrays.asList("a", "b", "c", "age");
        List<Expression> requiredSchemaAttributes = new ArrayList<>(createSchemaAttributes("subQuery", attributes));
        when(subQuery.getQueryObjectAttributes()).thenReturn(attributes.stream()
                .map(a -> ValueExpressionFactory.attribute("subQuery", a)).collect(Collectors.toList()));
        
        Query.Nest nest = QueryFactory.nest("nest", subQuery, emptyList(), requiredSchemaAttributes);
        assertFalse(nest.verify().toString(), nest.verify().isOk());

        requiredSchemaAttributes.add(ValueExpressionFactory.group("subQuery", "ages",
                Arrays.asList(ValueExpressionFactory.attribute("subQuery", "age"))));
        nest = QueryFactory.nest("nest", subQuery, emptyList(), requiredSchemaAttributes);
        assertTrue(nest.verify().toString(), nest.verify().isOk());
    }

    @Test
    public void testUnNest() throws Exception {
        Query subQuery = mock(Query.class);
        when(subQuery.id()).thenReturn("subQuery");
        when(subQuery.verify()).thenReturn(new Notification());
        Map<String, String> attributeMap = ImmutableMap.of(
                "age1", "subQuery",
                "age", "subQuery", 
                "age2", "subQuery"
        );
        when(subQuery.getRequiredSchemaAttributes()).thenReturn(Arrays.asList(
                        ValueExpressionFactory.group("subQuery", "ages", attributeMap.entrySet().stream()
                                .map(a -> ValueExpressionFactory.attribute(a.getValue(), a.getKey()))
                                .collect(Collectors.toList()))));
        
        Query.UnNest unNest = QueryFactory.unNest("unnest", subQuery, ValueExpressionFactory.attribute("subQuery", "ages"), 
                PARAMETERS);
        Context context = unNest.getContext();
        attributeMap.entrySet().stream().forEach(a -> assertTrue(String.format("Object \"%s\" hasn't attribute \"%s\"",
                a.getValue(), a.getKey()), context.hasAttribute(a.getValue(), a.getKey())));
        assertFalse(context.hasReference("subQuery1"));
        assertFalse(context.hasAttribute("subQuery1", "age3"));
    }

    @Test
    public void testGroupJoin() throws Exception {
        Map<String, List<String>> testData = ImmutableMap.of(
                "f1", Arrays.asList("a", "b"),
                "f2", Arrays.asList("c", "d")
        );
        List<Query> queries = testData.entrySet().stream().map(e -> createFilter(e.getKey(), e.getValue())).collect(Collectors.toList());
        List<Expression> arguments = Arrays.asList(
                BinaryArithmeticExpressionFactory.plus(ValueExpressionFactory.attribute("f1", "a"), 
                        ValueExpressionFactory.attribute("f2", "c")),
                BinaryArithmeticExpressionFactory.plus(ValueExpressionFactory.attribute("f1", "b"), 
                        ValueExpressionFactory.attribute("f2", "d"))
        );
        Query.GroupJoin groupJoin = QueryFactory.groupJoin("groupJoin", queries.get(0), queries.get(1), arguments, 
                emptyList(), emptyList());
        assertFalse(groupJoin.verify().toString(), groupJoin.verify().isOk());
        
        List<Expression> requiredSchema = Lists.newArrayList(
                ValueExpressionFactory.functionCall("sum",
                        Arrays.asList(ValueExpressionFactory.group("groupJoin", "ages",
                                Arrays.asList(ValueExpressionFactory.attribute("f1", "a")))))
        );
        groupJoin = QueryFactory.groupJoin("groupJoin", queries.get(0), queries.get(1), arguments, emptyList(), requiredSchema);
        assertTrue(groupJoin.verify().toString(), groupJoin.verify().isOk());
        Context context = groupJoin.getContext();
        testData.entrySet().stream().forEach(e -> e.getValue().stream().forEach(a -> assertTrue(context.hasAttribute(e.getKey(), a))));
    }
}