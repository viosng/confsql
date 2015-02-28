package com.viosng.confsql.semantic.model.sql;

import com.viosng.confsql.semantic.model.sql.impl.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

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
        assertEquals(new SQLField("aBC.d.e"), visitor.visit(getParser("aBC.d.e").expr()));
        assertNotEquals(new SQLField("8a"), visitor.visit(getParser("8a").expr()));
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
        } catch (NullPointerException ignored) {
        }

        assertEquals(new SQLExpressionList(
                        Arrays.asList(
                                new SQLParameter("a", new SQLConstant("3")),
                                new SQLParameter("asdfs", new SQLConstant("true")),
                                new SQLParameter("asdfs sdfsdfgsfd sdfsf", new SQLConstant("true")))),
                visitor.visit(getParser("a=3,asdfs=true,\"asdfs sdfsdfgsfd sdfsf\"=true").param_list()));
    }

    @Test
    public void testExprList() throws Exception {
        assertEquals(new SQLExpressionList(Arrays.asList(
                        new SQLConstant("1"),
                        new SQLField("afsd.ds"),
                        new SQLConstant("\"sdfs\"")
                )),
                visitor.visit(getParser("1, afsd.ds, \"sdfs\"").expr_list()));
    }

    @Test
    public void testFunctionCall() throws Exception {
        SQLFunctionCall functionCall = new SQLFunctionCall("f", 
                new SQLExpressionsAndParamsList(
                    new SQLExpressionList(
                            Arrays.asList(
                                new SQLConstant("1"),
                                new SQLField("afsd.ds"),
                                new SQLConstant("\"sdfs\"")
                    )),
                    new SQLExpressionList(
                        Arrays.asList(
                                new SQLParameter("a", new SQLConstant("3")),
                                new SQLParameter("asdfs", new SQLConstant("true")),
                                new SQLParameter("asdfs sdfsdfgsfd sdfsf", new SQLConstant("true"))))));
        assertEquals(functionCall, 
                visitor.visit(getParser("f(1, afsd.ds, \"sdfs\"; a=3,asdfs=true,\"asdfs sdfsdfgsfd sdfsf\"=true)").expr()));

        functionCall = new SQLFunctionCall("args",
                new SQLExpressionsAndParamsList(
                        new SQLExpressionList(
                                Arrays.asList(
                                        new SQLConstant("1"),
                                        new SQLField("afsd.ds"),
                                        new SQLConstant("\"sdfs\"")
                                )),
                        new SQLExpressionList(Collections.<SQLExpression>emptyList())));
        
        assertEquals(functionCall, visitor.visit(getParser("args(1, afsd.ds, \"sdfs\")").expr()));

        functionCall = new SQLFunctionCall("params",
                new SQLExpressionsAndParamsList(
                        new SQLExpressionList(Collections.<SQLExpression>emptyList()),
                        new SQLExpressionList(
                                Arrays.asList(
                                        new SQLParameter("a", new SQLConstant("3")),
                                        new SQLParameter("asdfs", new SQLConstant("true")),
                                        new SQLParameter("asdfs sdfsdfgsfd sdfsf", new SQLConstant("true"))))));

        assertEquals(functionCall, visitor.visit(getParser("params(a=3,asdfs=true,\"asdfs sdfsdfgsfd sdfsf\"=true)").expr()));

        functionCall = new SQLFunctionCall("empty",
                new SQLExpressionsAndParamsList(
                        new SQLExpressionList(Collections.<SQLExpression>emptyList()),
                        new SQLExpressionList(Collections.<SQLExpression>emptyList())));

        assertEquals(functionCall, visitor.visit(getParser("empty()").expr()));
    }

    @Test
    public void testCase() throws Exception {
        SQLCase caseExpr = new SQLCase(new SQLField("c"),
                Arrays.asList(
                        new SQLCase.SQLWhenThenClause(new SQLField("ca"), new SQLField("cb")),
                        new SQLCase.SQLWhenThenClause(new SQLConstant("3"), new SQLField("sdfgsre")),
                        new SQLCase.SQLWhenThenClause(new SQLConstant("\"wewe\""), new SQLConstant("123.323"))
                ),
                new SQLField("a.b.c"));
        assertEquals(caseExpr,
                visitor.visit(getParser("case c when ca then cb when 3 then sdfgsre when \"wewe\" then 123.323 else a.b.c end").expr()));

        caseExpr = new SQLCase(null,
                Arrays.asList(
                        new SQLCase.SQLWhenThenClause(new SQLField("ca"), new SQLField("cb")),
                        new SQLCase.SQLWhenThenClause(new SQLConstant("3"), new SQLField("sdfgsre"))
                ),
                null);
        assertEquals(caseExpr, visitor.visit(getParser("case when ca then cb when 3 then sdfgsre end").expr()));
    }

    @Test
    public void testCast() throws Exception {
        SQLExpression cast = new SQLFunctionCall("cast", new SQLExpressionsAndParamsList(
                new SQLExpressionList(Arrays.asList(new SQLConstant("23"), new SQLConstant("Int"))),
                new SQLExpressionList(Collections.emptyList())));
        assertEquals(cast, visitor.visit(getParser("cast 23 as Int").expr()));
        assertNotEquals(cast, visitor.visit(getParser("cast 23 as B").expr()));
        assertNotEquals(cast, visitor.visit(getParser("cast 24 as Int").expr()));
    }

    @Test
    public void testIs() throws Exception {
        SQLExpression cast = new SQLFunctionCall("is", new SQLExpressionsAndParamsList(
                new SQLExpressionList(Arrays.asList(new SQLConstant("23"), new SQLConstant("42"))),
                new SQLExpressionList(Collections.emptyList())));
        assertEquals(cast, visitor.visit(getParser("23 is 42").expr()));
        assertNotEquals(cast, visitor.visit(getParser("23 is 43").expr()));
        assertNotEquals(cast, visitor.visit(getParser("24 is 42").expr()));
    }

    @Test
    public void testBinaryExpressions() throws Exception {
        SQLField c1 = new SQLField("c1");
        SQLField c2 = new SQLField("c2");
        List<Pair<SQLExpression.ArithmeticType, String>>  testData = Arrays.asList(
                new Pair<>(SQLExpression.ArithmeticType.PLUS, "c1 + c2"),
                new Pair<>(SQLExpression.ArithmeticType.MINUS, "c1 - c2"),
                new Pair<>(SQLExpression.ArithmeticType.MULTIPLY, "c1 * c2"),
                new Pair<>(SQLExpression.ArithmeticType.DIVIDE, "c1 / c2"),
                new Pair<>(SQLExpression.ArithmeticType.MODULAR, "c1 % c2"),
                new Pair<>(SQLExpression.ArithmeticType.POWER, "c1 ** c2"),
                new Pair<>(SQLExpression.ArithmeticType.AND, "c1 and c2"),
                new Pair<>(SQLExpression.ArithmeticType.OR, "c1 or c2"),
                new Pair<>(SQLExpression.ArithmeticType.BIT_AND, "c1 & c2"),
                new Pair<>(SQLExpression.ArithmeticType.BIT_OR, "c1 | c2"),
                new Pair<>(SQLExpression.ArithmeticType.BIT_XOR, "c1 ^ c2"),
                new Pair<>(SQLExpression.ArithmeticType.CONCATENATION, "c1 || c2"),
                new Pair<>(SQLExpression.ArithmeticType.EQUAL, "c1 = c2"),
                new Pair<>(SQLExpression.ArithmeticType.NOT_EQUAL, "c1 != c2"),
                new Pair<>(SQLExpression.ArithmeticType.NOT_EQUAL, "c1 <> c2"),
                new Pair<>(SQLExpression.ArithmeticType.GT, "c1 > c2"),
                new Pair<>(SQLExpression.ArithmeticType.LT, "c1 < c2"),
                new Pair<>(SQLExpression.ArithmeticType.GE, "c1 >= c2"),
                new Pair<>(SQLExpression.ArithmeticType.LE, "c1 <= c2")
        );
        for (Pair<SQLExpression.ArithmeticType, String> pair : testData) {
            assertEquals(pair.b, new SQLBinaryExpression(pair.a, c1, c2), visitor.visit(getParser(pair.b).expr()));
            assertNotEquals(pair.b, new SQLBinaryExpression(pair.a, c2, c1), visitor.visit(getParser(pair.b).expr()));
        }
    }

    @Test
    public void testUnaryExpressions() throws Exception {
        SQLField c1 = new SQLField("c1");
        List<Pair<SQLExpression.ArithmeticType, String>>  testData = Arrays.asList(
                new Pair<>(SQLExpression.ArithmeticType.BIT_NEG, "~ c1"),
                new Pair<>(SQLExpression.ArithmeticType.MINUS, "-c1"),
                new Pair<>(SQLExpression.ArithmeticType.NOT, "not c1")
        );
        for (Pair<SQLExpression.ArithmeticType, String> pair : testData) {
            assertEquals(pair.b, new SQLUnaryExpression(pair.a, c1), visitor.visit(getParser(pair.b).expr()));
        }
    }
}
