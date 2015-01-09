package com.viosng.confsql.semantic.model.queries;

import com.google.common.collect.ImmutableMap;
import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.binary.BinaryPredicateExpressionFactory;
import com.viosng.confsql.semantic.model.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 09.01.2015
 * Time: 0:58
 */
public class QueryContextTest {

    private static final List<Parameter> PARAMETERS = Arrays.asList(
            new Parameter("constA", "valA"),
            new Parameter("constB", "valB"),
            new Parameter("constC", "valC")
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
        return Arrays.asList(attributes.stream().map(a -> ValueExpressionFactory.attribute(objectName, a)).toArray(Expression[]::new));
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
        List<Query> filters = Arrays.asList(testData.entrySet().stream()
                .map(e -> createFilter(e.getKey(), e.getValue())).toArray(Query[]::new));
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
}