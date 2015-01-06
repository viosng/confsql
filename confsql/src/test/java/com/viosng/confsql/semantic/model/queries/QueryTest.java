package com.viosng.confsql.semantic.model.queries;

import com.viosng.confsql.semantic.model.expressions.other.ValueExpression;
import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 07.01.2015
 * Time: 0:50
 */

@RunWith(Theories.class)
public class QueryTest {
    public static Object[][] testData;
    
    private static Query Q_MOCK = mock(Query.class);

    @BeforeClass
    public static void generateData(){
        testData = new Object[][] {
                { QueryFactory.primary("pr", emptyList(), emptyList()), "pr", Query.QueryType.PRIMARY },
                { QueryFactory.filter("ft", Q_MOCK, emptyList(), emptyList(), emptyList()), "ft", Query.QueryType.FILTER },
                { QueryFactory.fusion("fs", emptyList(), emptyList()), "fs", Query.QueryType.FUSION },
                { QueryFactory.join("jn", emptyList(), Q_MOCK, Q_MOCK, emptyList()), "jn", Query.QueryType.JOIN },
                { QueryFactory.aggregation("ag", Q_MOCK, emptyList(), emptyList(), emptyList()), "ag", Query.QueryType.AGGREGATION },
                { QueryFactory.nest("ne", Q_MOCK, emptyList(), emptyList()), "ne", Query.QueryType.NEST },
                { QueryFactory.unNest("un", Q_MOCK, mock(ValueExpression.AttributeExpression.class), emptyList()), "un", Query.QueryType.UNNEST},
                { QueryFactory.groupJoin("gj", Q_MOCK, Q_MOCK, emptyList(), emptyList(), emptyList()), "gj", Query.QueryType.GROUP_JOIN },
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
        assertEquals(testData[2], q.type());
    }
    
    
}
