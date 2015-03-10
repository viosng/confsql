package com.viosng.confsql.semantic.model.algebra.queries;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.ExpressionImpl;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpression;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 07.01.2015
 * Time: 0:50
 */

@RunWith(Theories.class)
public class QueryTest {
    public static Object[][] testData;

    private static final List<Parameter> PARAMETERS = Arrays.asList(
            new Parameter("constA", ValueExpressionFactory.constant("valA")),
            new Parameter("constB", ValueExpressionFactory.constant("valB")),
            new Parameter("constC", ValueExpressionFactory.constant("valC"))
    );
    

    @BeforeClass
    public static void init(){
        Query Q_MOCK = mock(Query.class);
        when(Q_MOCK.id()).thenReturn("query");
        ValueExpression.AttributeExpression attributeExpression = mock(ValueExpression.AttributeExpression.class);
        when(attributeExpression.id()).thenReturn("attribute");
        testData = new Object[][] {
                { QueryFactory.primary("pr", emptyList(), PARAMETERS), "pr", Query.QueryType.PRIMARY },
                { QueryFactory.filter("ft", Q_MOCK, emptyList(), PARAMETERS, emptyList()), "ft", Query.QueryType.FILTER },
                { QueryFactory.fusion("fs", PARAMETERS, emptyList()), "fs", Query.QueryType.FUSION },
                { QueryFactory.join("jn", PARAMETERS, Q_MOCK, Q_MOCK, emptyList()), "jn", Query.QueryType.JOIN },
                { QueryFactory.aggregation("ag", Q_MOCK, emptyList(), PARAMETERS, emptyList()), "ag", Query.QueryType.AGGREGATION },
                { QueryFactory.nest("ne", Q_MOCK, PARAMETERS, emptyList()), "ne", Query.QueryType.NEST },
                { QueryFactory.groupJoin("gj", Q_MOCK, Q_MOCK, emptyList(), PARAMETERS, emptyList()), "gj", Query.QueryType.GROUP_JOIN },
        };
    }

    @DataPoints
    public static Object[][] data() {
        return testData;
    }

    @Theory
    public void testInterface(final Object... testData) {
        Query q = (Query) testData[0];
        assertEquals(testData[1], q.id());
        assertEquals(testData[2], q.queryType());
        assertEquals(PARAMETERS, q.getParameters());
    }

    private Query.Primary createPrimary() {
        return QueryFactory.primary("primary", Arrays.asList(ValueExpressionFactory.constant("sourcePath")), PARAMETERS);
    }
    
    @Test
    public void testPrimary() throws Exception {
        Query.Primary primary = createPrimary();
        assertTrue(primary.verify().isOk());
    }

    private Query.Filter createFilter() {
        List<Expression> schemaAttributes = Arrays.asList(
                ValueExpressionFactory.attribute("primary", "fieldA"),
                ValueExpressionFactory.attribute("primary", "fieldB"),
                ValueExpressionFactory.attribute("primary", "fieldC")
        );
        return QueryFactory.filter("filter", createPrimary(), emptyList(), PARAMETERS, schemaAttributes);
    }

    @Test
    public void testFilter() throws Exception {
        Query.Filter filter = createFilter();
        assertTrue(filter.verify().isOk());
        List<Expression> argumentExpressions = Arrays.asList(
                new ExpressionImpl(ArithmeticType.LT, ValueExpressionFactory.attribute("filter", "fieldD"),
                        ValueExpressionFactory.constant("40"))
        );
        filter = QueryFactory.filter("filter", filter, argumentExpressions, PARAMETERS, emptyList());
        //assertFalse(filter.verify().isOk());
    }
}
