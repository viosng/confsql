package com.viosng.confsql.semantic.model.algebra.queries;

import com.google.common.collect.Lists;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.sql.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        Query filter = (Query) visitor.visit(getParser("select * from t").query()).convert();
        Context context = new Context("t");
        assertEquals(context, filter.getContext());

        filter = (Query) visitor.visit(getParser("select a, b, c, nest(d,e,f) n from t").query()).convert();
        context = new Context(Expression.UNDEFINED_ID);
        context.addObject(Lists.newArrayList("", "a"));
        context.addObject(Lists.newArrayList("", "b"));
        context.addObject(Lists.newArrayList("", "c"));
        context.addObject(Lists.newArrayList("", "n", "d"));
        context.addObject(Lists.newArrayList("", "n", "e"));
        context.addObject(Lists.newArrayList("", "n", "f"));
        assertEquals(context, filter.getContext());

        filter = (Query) visitor.visit(getParser(
                "select a.m, nest(n.d,b as e,c as w) as q from (select a, b, c, nest(d,e,f) n from t) as t1").query()).convert();
        context = new Context(Expression.UNDEFINED_ID);
        context.addObject(Lists.newArrayList("", "m"));
        context.addObject(Lists.newArrayList("", "q", "d"));
        context.addObject(Lists.newArrayList("", "q", "e"));
        context.addObject(Lists.newArrayList("", "q", "w"));
        assertEquals(context, filter.getContext());

        filter = (Query) visitor.visit(getParser(
                "select a.m, nest(n as r,b as e,c as w) as q from (select a, b, c, nest(d,e,f) n from t) as t1").query()).convert();
        context = new Context(Expression.UNDEFINED_ID);
        context.addObject(Lists.newArrayList("", "m"));
        context.addObject(Lists.newArrayList("", "q", "r", "d"));
        context.addObject(Lists.newArrayList("", "q", "r", "e"));
        context.addObject(Lists.newArrayList("", "q", "r", "f"));
        context.addObject(Lists.newArrayList("", "q", "e"));
        context.addObject(Lists.newArrayList("", "q", "w"));
        assertEquals(context, filter.getContext());

        filter = (Query) visitor.visit(getParser(
                "select * from (select a, b, c, f(3) d from t) as t1").query()).convert();
        context = new Context("t1");
        assertEquals(context, filter.getContext());

        filter = (Query) visitor.visit(getParser(
                "select d,e,f, a+b as q, (select 1) as query, \"sdfs\" r from (select a, b, c, f(3) d from t) as t1").query()).convert();
        context = new Context("");
        context.addObject(Lists.newArrayList("", "d"));
        context.addObject(Lists.newArrayList("", "e"));
        context.addObject(Lists.newArrayList("", "f"));
        context.addObject(Lists.newArrayList("", "q"));
        context.addObject(Lists.newArrayList("", "r"));
        context.addObject(Lists.newArrayList("", "query"));
        assertEquals(context, filter.getContext());

        filter = (Query) visitor.visit(getParser("select a, b from (select c, d from t) as t").query()).convert();
        assertFalse(filter.getContext().isOk());

        filter = (Query) visitor.visit(getParser("select a, b from (select c, d, f() from t) as t").query()).convert();
        assertTrue(filter.getContext().toString(), filter.getContext().isOk());

        filter = (Query) visitor.visit(getParser("select a, b, 3+2 a from t").query()).convert();
        assertFalse(filter.getContext().toString(), filter.getContext().isOk());

        filter = (Query) visitor.visit(getParser("select a, b, 3+2 from t").query()).convert();
        assertFalse(filter.getContext().toString(), filter.getContext().isOk());
    }

    @Test
    public void testFusion() throws Exception {
        Query fusion = (Query) visitor.visit(getParser("fusion (select a, b, c, nest(e, r, t) n from t) with (select c, d, e from t) end").query()).convert();
        Context context = new Context("");
        context.addObject(Lists.newArrayList("", "a"));
        context.addObject(Lists.newArrayList("", "b"));
        context.addObject(Lists.newArrayList("", "c"));
        context.addObject(Lists.newArrayList("", "d"));
        context.addObject(Lists.newArrayList("", "e"));
        context.addObject(Lists.newArrayList("", "n", "e"));
        context.addObject(Lists.newArrayList("", "n", "r"));
        context.addObject(Lists.newArrayList("", "n", "t"));
        assertEquals(context, fusion.getContext());
    }
}
