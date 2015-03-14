package com.viosng.confsql.semantic.model.algebra.verifier;

import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.queries.QueryBuilder;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.other.Verifier;
import com.viosng.confsql.semantic.model.sql.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

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
    public void testPrimary() throws Exception {
        Verifier v = primary().verify(new Verifier());
    }

    @Test
    public void testFilter() throws Exception {
        Query filter = (Query)visitor.visit(getParser("select * from (select a, b, c from table) as t1 join (select d, e, f from table) as t2").query()).convert();
        System.out.println(filter.verify(new Verifier()));
    }
}
