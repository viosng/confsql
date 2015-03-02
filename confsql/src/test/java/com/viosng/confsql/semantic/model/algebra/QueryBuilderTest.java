package com.viosng.confsql.semantic.model.algebra;

import com.viosng.confsql.semantic.model.algebra.expressions.Expression;
import com.viosng.confsql.semantic.model.algebra.expressions.other.ValueExpression;
import com.viosng.confsql.semantic.model.algebra.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 09.01.2015
 * Time: 23:50
 */
public class QueryBuilderTest {
    
    private QueryBuilder queryBuilder;
    private Query subQuery1, subQuery2;
    private final String ID = "query";
    private List<Parameter> parameters = Arrays.asList(new Parameter("a", "b"), new Parameter("c", "d"));
    
    private static List<Expression> schemaAttributes(String object, List<String> attributes) {
        return attributes.stream().map(a -> ValueExpressionFactory.attribute(object, a))
                .collect(Collectors.toList());
        
    }
    
    @Before
    public void setUp() throws Exception {
        queryBuilder = new QueryBuilder();
        queryBuilder.parameters(parameters);
        queryBuilder.id(ID);
        Query primary = QueryFactory.primary("primary", emptyList(), emptyList());
        subQuery1 = QueryFactory.filter("f1", primary, emptyList(), emptyList(),
                schemaAttributes("primary", Arrays.asList("a", "b", "c")));
        subQuery2 = QueryFactory.filter("f2", primary, emptyList(), emptyList(), 
                schemaAttributes("primary", Arrays.asList("f", "d", "e")));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testMissedQueryType() throws Exception {
        queryBuilder.create();
    }

    private void assertCreationException() {
        try {
            queryBuilder.create();
            fail();
        } catch (IllegalArgumentException ignored){}
    }

    @Test
    public void testPrimaryCreation() throws Exception {
        queryBuilder.queryType(Query.QueryType.PRIMARY);
        assertEquals(QueryFactory.primary(ID, emptyList(), parameters), queryBuilder.create());
    }

    @Test
    public void testFilterCreation() throws Exception {
        queryBuilder.queryType(Query.QueryType.FILTER);
        assertCreationException();
        queryBuilder.subQueries(subQuery1, subQuery2);
        assertCreationException();
        queryBuilder.subQueries(subQuery1);
        List<Expression> schemaAttributes = schemaAttributes("f1", Arrays.asList("a", "b"));
        queryBuilder.requiredSchemaAttributes(schemaAttributes);
        assertEquals(QueryFactory.filter(ID, subQuery1, emptyList(), parameters, schemaAttributes), queryBuilder.create());
    }

    @Test
    public void testFusionCreation() throws Exception {
        queryBuilder.queryType(Query.QueryType.FUSION);
        assertCreationException();
        queryBuilder.subQueries(subQuery1, subQuery2);
        assertEquals(QueryFactory.fusion(ID, parameters, Arrays.asList(subQuery1, subQuery2)), queryBuilder.create());
    }

    @Test
    public void testJoinCreation() throws Exception {
        queryBuilder.queryType(Query.QueryType.JOIN);
        assertCreationException();
        queryBuilder.subQueries(subQuery1);
        assertCreationException();
        queryBuilder.subQueries(subQuery1, subQuery2);
        assertEquals(QueryFactory.join(ID, parameters, subQuery1, subQuery2, emptyList()), queryBuilder.create());
    }

    @Test
    public void testAggregationCreation() throws Exception {
        queryBuilder.queryType(Query.QueryType.AGGREGATION);
        assertCreationException();
        queryBuilder.subQueries(subQuery1, subQuery2);
        assertCreationException();
        queryBuilder.subQueries(subQuery1);
        List<Expression> schemaAttributes = schemaAttributes("f1", Arrays.asList("a", "b"));
        queryBuilder.requiredSchemaAttributes(schemaAttributes);
        assertEquals(QueryFactory.aggregation(ID, subQuery1, emptyList(), parameters, schemaAttributes), queryBuilder.create());
    }

    @Test
    public void testNestCreation() throws Exception {
        queryBuilder.queryType(Query.QueryType.NEST);
        assertCreationException();
        queryBuilder.subQueries(subQuery1, subQuery2);
        assertCreationException();
        queryBuilder.subQueries(subQuery1);
        List<Expression> schemaAttributes = schemaAttributes("f1", Arrays.asList("a", "b"));
        queryBuilder.requiredSchemaAttributes(schemaAttributes);
        assertEquals(QueryFactory.nest(ID, subQuery1, parameters, schemaAttributes), queryBuilder.create());
    }

    @Test
    public void testUnNestCreation() throws Exception {
        queryBuilder.queryType(Query.QueryType.UNNEST);
        assertCreationException();
        queryBuilder.subQueries(subQuery1, subQuery2);
        assertCreationException();
        queryBuilder.subQueries(subQuery1);
        assertCreationException();
        queryBuilder.argumentExpressions(ValueExpressionFactory.constant("a"), ValueExpressionFactory.constant("b"));
        assertCreationException();
        queryBuilder.argumentExpressions(ValueExpressionFactory.constant("a"));
        assertCreationException();
        queryBuilder.argumentExpressions(ValueExpressionFactory.attribute("a", "b"), ValueExpressionFactory.attribute("c", "d"));
        assertCreationException();
        ValueExpression.AttributeExpression attribute = ValueExpressionFactory.attribute("a", "b");
        queryBuilder.argumentExpressions(attribute);
        assertEquals(QueryFactory.unNest(ID, subQuery1, attribute, parameters), queryBuilder.create());
    }

    @Test
    public void testGroupJoinCreation() throws Exception {
        queryBuilder.queryType(Query.QueryType.GROUP_JOIN);
        assertCreationException();
        queryBuilder.subQueries(subQuery1);
        assertCreationException();
        queryBuilder.subQueries(subQuery1, subQuery2);
        List<Expression> schemaAttributes = schemaAttributes("f1", Arrays.asList("a", "b"));
        queryBuilder.requiredSchemaAttributes(schemaAttributes);
        assertEquals(QueryFactory.groupJoin(ID, subQuery1, subQuery2, emptyList(), parameters, schemaAttributes), queryBuilder.create());
    }
}
