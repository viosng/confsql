package com.viosng.confsql.semantic.model.algebra.verifier;

import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.queries.QueryBuilder;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpression;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.other.Verifier;
import com.viosng.confsql.semantic.model.sql.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 14.03.2015
 * Time: 13:20
 */
public class VerifierTest {

    private static Query primary() {
        return new QueryBuilder().queryType(Query.QueryType.PRIMARY)
                .parameters(new Parameter("sourceName", ValueExpressionFactory.constant("table")))
                .create();
    }

    private ConfSQLVisitor<SQLExpression> visitor = new ConfSQLVisitorImpl();

    private ConfSQLParser getParser(String input) {
        return new ConfSQLParser(new CommonTokenStream(new ConfSQLLexer(new ANTLRInputStream(input))));
    }

    @Test
    public void testConstant() throws Exception {
        ValueExpression.ConstantExpression
        constantExpression = ValueExpressionFactory.constant("123");
        assertTrue(constantExpression.verify(new Verifier()).isOk());
    }

    @Test
    public void testPrimary() throws Exception {
        Verifier v = primary().verify(new Verifier());
    }

    @Test
    public void testFilter() throws Exception {
        Query subQuery = mock(Query.class);
        doReturn("subQuery").when(subQuery).id();
        Verifier subQueryVerifier = new Verifier();
        subQueryVerifier.attribute("subQuery", "b");
        subQueryVerifier.attribute("subQuery", "a");
        subQueryVerifier.attribute("subQuery", "c");
        doReturn(subQueryVerifier).when(subQuery).verify(any(Verifier.class));
        Query filter = new QueryBuilder().queryType(Query.QueryType.FILTER).subQueries(subQuery).create();
        System.out.println(filter.verify(new Verifier()));

    }

    @Test
    public void testJoin() throws Exception {
        Query filter = (Query)visitor.visit(getParser("select * from (select a, b, c from table) as t1 join " +
                "(select d, e, f from table) as t2 join (select h,k, l from table) as t3").query()).convert();
        System.out.println(filter.verify(new Verifier()));
    }
}
