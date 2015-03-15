package com.viosng.confsql.semantic.model.algebra.queries;

import com.viosng.confsql.semantic.model.sql.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 16.03.2015
 * Time: 1:51
 */
public class QueryContextCreationTest {

    private ConfSQLVisitor<SQLExpression> visitor = new ConfSQLVisitorImpl();

    private ConfSQLParser getParser(String input) {
        return new ConfSQLParser(new CommonTokenStream(new ConfSQLLexer(new ANTLRInputStream(input))));
    }

    @Test
    public void testFilter() throws Exception {
        Query filter = (Query) visitor.visit(getParser("select a.m, nest(n.d,b as e,c as w) as q from (select a, b, c, nest(d,e,f) n from t) as t1").query()).convert();
        //Query filter = (Query) visitor.visit(getParser("select a, b, c, nest(d,e,f) n from t").query()).convert();
        System.out.println(filter.getContext());
        System.out.println(1);
    }
}
