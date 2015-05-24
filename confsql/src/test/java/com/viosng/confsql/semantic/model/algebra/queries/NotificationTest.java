package com.viosng.confsql.semantic.model.algebra.queries;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.sql.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 18.03.2015
 * Time: 1:13
 */
public class NotificationTest {
    private ConfSQLVisitor<SQLExpression> visitor = new ConfSQLVisitorImpl();

    private ConfSQLParser getParser(String input) {
        return new ConfSQLParser(new CommonTokenStream(new ConfSQLLexer(new ANTLRInputStream(input))));
    }

    @Test
    public void testNotifications() throws Exception {
        Expression exp = visitor.visit(getParser("select * from t").stat()).convert();
        assertTrue(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser("select a,b,c from t").stat()).convert();
        assertTrue(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser("select a, c from (select a,b,c from t) as t").stat()).convert();
        assertTrue(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser("select a, d from (select a,b,c from t) as t").stat()).convert();
        assertFalse(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser("select a, d from (select a,b,c() from t) as t").stat()).convert();
        assertTrue(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser("select a, d from (select a,b, nest(d,e,f) n from t) as t").stat()).convert();
        assertFalse(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser("select a, n.f from (select a,b, nest(d,e,f) n from t) as t").stat()).convert();
        assertTrue(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser(
                "select a from (select a,b, nest(b, c.e as w, a.e) n from (select a,b,c from t) as t) as t\n" +
                "where b > 3\n" +
                "order(q=(select a from (select a,q from t) as t where q < n.b.e)) by a").stat()).convert();
        assertTrue(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser(
                "select a, n.b.w.e from (select a,b, nest(b, b.e as w, a.e) n from (select a,b,c from t) as t) as t\n" +
                        "where b > 3\n" +
                        "order(q=(select a from (select a,q from t) as t where q < n.b.e)) by a").stat()).convert();
        assertTrue(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser(
                "select a, n.d.e from (select a,b, nest(b, b.e as w, a.e) n from (select a,b,c from t) as t) as t\n" +
                        "where b > 3\n" +
                        "order(q=(select a from (select a,q from t) as t where q < n.b.e)) by a").stat()).convert();
        assertFalse(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser(
                "select a from (select a,b, nest(b as i, c.e as w, a.e) n from (select a,b,c from t) as t) as t join t.n\n" +
                        " where b > 3\n" +
                        " order(q=(select a from (select a,q from t) as t where q < b.e)) by a").stat()).convert();
        assertTrue(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser(
                "select a from (select a,b, nest(b, c.e as w, a.e) n from (select a,b,c from t) as t) as t join t.n\n" +
                        " where b > 3\n" +
                        " order(q=(select a from (select a,q from t) as t where q < b.e)) by a").stat()).convert();
        assertFalse(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser(
                "select a, w from (select a,b, nest(b as i, c.e as w, a.e) n from (select a,b,c from t) as t) as t join t.n\n" +
                        " where b > 3\n" +
                        " order(q=(select a from (select a,q from t) as t where q < b.e)) by a").stat()).convert();
        assertTrue(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser(
                "select 3 as c from (fusion select a,b,c from t with select a,e,f from t end) as q").stat()).convert();
        assertTrue(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser(
                "select uu.a, uu.d from (fusion (select a, w from (select a,b, nest(b as i, c.e as w, a.e) n from (select a,b,c from t) as t) as t join t.n\n" +
                        " where b > 3\n" +
                        " order(q=(select a from (select a,q from t) as t where q < b.e)) by a) with select a,b,c from t end) as uu").stat()).convert();
        assertFalse(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser(
                "select uu.a, uu.r, c from (fusion (select d,f,r from tt) with select a,b,c from t end) as uu").stat()).convert();
        assertTrue(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser(
                "select uu.a, uu.tt from (fusion select e.a as tt from e join qq where e.b > 3\n" +
                        " with select a,b,c from t end) as uu").stat()).convert();
        assertTrue(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());

        exp = visitor.visit(getParser(
                "select uu.a, uu.tt from (fusion select e.a as tt from (select a,b,c from t) e join e.c where e.b > 3\n" +
                        "with select a,b,c from t end) as uu").stat()).convert();
        assertTrue(exp.verify(Context.EMPTY_CONTEXT).toString(), exp.verify(Context.EMPTY_CONTEXT).isOk());
    }
}
