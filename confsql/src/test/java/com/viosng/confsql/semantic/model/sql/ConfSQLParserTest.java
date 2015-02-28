package com.viosng.confsql.semantic.model.sql;

import com.viosng.confsql.semantic.model.sql.impl.SQLConstant;
import com.viosng.confsql.semantic.model.sql.impl.SQLExpressionList;
import com.viosng.confsql.semantic.model.sql.impl.SQLField;
import com.viosng.confsql.semantic.model.sql.impl.SQLParameter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.02.2015
 * Time: 18:10
 */

@RunWith(Theories.class)
public class ConfSQLParserTest {
    private ConfSQLVisitor<SQLExpression> visitor = new ConfSQLVisitorImpl();

    private ConfSQLParser getParser(String input) {
        return new ConfSQLParser(new CommonTokenStream(new ConfSQLLexer(new ANTLRInputStream(input))));
    }

    @Test
    public void testField() throws Exception {
        assertEquals(new SQLField("abc"), visitor.visit(getParser("abc").expr()));
        assertEquals(new SQLField("a"), visitor.visit(getParser("a").expr()));
        assertEquals(new SQLField("abc.d.e"), visitor.visit(getParser("abc.d.e").expr()));
    }

    @Test
    public void testConstant() throws Exception {
        assertEquals(new SQLConstant("\"abc\""), visitor.visit(getParser("\"abc\"").expr()));
        assertEquals(new SQLConstant("\"abcsfd sdfsdgs\""), visitor.visit(getParser("\"abcsfd sdfsdgs\"").expr()));

        assertEquals(new SQLConstant("123"), visitor.visit(getParser("123").expr()));
        assertEquals(new SQLConstant("0"), visitor.visit(getParser("0").expr()));
        assertEquals(new SQLConstant("123234234"), visitor.visit(getParser("123234234").expr()));

        assertEquals(new SQLConstant("TRUE"), visitor.visit(getParser("TRUE").expr()));
        assertEquals(new SQLConstant("false"), visitor.visit(getParser("false").expr()));
        assertEquals(new SQLConstant("NULL"), visitor.visit(getParser("NULL").expr()));

        assertEquals(new SQLConstant("1.5"), visitor.visit(getParser("1.5").expr()));
        assertEquals(new SQLConstant("123."), visitor.visit(getParser("123.").expr()));
        assertEquals(new SQLConstant(".23e124321"), visitor.visit(getParser(".23e124321").expr()));
    }

    @Test
    public void testParams() throws Exception {
        assertEquals(new SQLParameter("a", new SQLConstant("3")), visitor.visit(getParser("a=3").param()));
        assertEquals(new SQLParameter("asdfs", new SQLConstant("true")), visitor.visit(getParser("asdfs=true").param()));
        assertEquals(new SQLParameter("asdfs sdfsdfgsfd sdfsf", new SQLConstant("true")),
                visitor.visit(getParser("\"asdfs sdfsdfgsfd sdfsf\"=true").param()));
        try {
            visitor.visit(getParser("a.f=3").param());
            fail();
        } catch (NullPointerException ignored){}

        assertEquals(new SQLExpressionList(
                        Arrays.asList(
                                new SQLParameter("a", new SQLConstant("3")),
                                new SQLParameter("asdfs", new SQLConstant("true")),
                                new SQLParameter("asdfs sdfsdfgsfd sdfsf", new SQLConstant("true")))),
                visitor.visit(getParser("a=3,asdfs=true,\"asdfs sdfsdfgsfd sdfsf\"=true").param_list()));
    }

}
