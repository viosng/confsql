package com.viosng.confsql.semantic.model.sql;

import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.sql.expr.impl.*;
import com.viosng.confsql.semantic.model.sql.query.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.Test;

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

        assertEquals(new SQLParameterList(
                        Arrays.asList(
                                new SQLParameter("a", new SQLConstant("3")),
                                new SQLParameter("asdfs", new SQLConstant("true")),
                                new SQLParameter("asdfs sdfsdfgsfd sdfsf", new SQLConstant("true")))),
                visitor.visit(getParser("a=3,asdfs=true,\"asdfs sdfsdfgsfd sdfsf\"=true").paramList()));
    }

    @Test
    public void testExprList() throws Exception {
        assertEquals(new SQLExpressionList(Arrays.asList(
                        new SQLConstant("1"),
                        new SQLField("afsd.ds"),
                        new SQLConstant("\"sdfs\"")
                )),
                visitor.visit(getParser("1, afsd.ds, \"sdfs\"").exprList()));
    }

    @Test
    public void testFunctionCall() throws Exception {
        SQLFunctionCall functionCall = new SQLFunctionCall("f",
                Arrays.asList(
                        new SQLConstant("1"),
                        new SQLField("afsd.ds"),
                        new SQLConstant("\"sdfs\"")
                ),
                Arrays.asList(
                        new SQLParameter("a", new SQLConstant("3")),
                        new SQLParameter("asdfs", new SQLConstant("true")),
                        new SQLParameter("asdfs sdfsdfgsfd sdfsf", new SQLConstant("true"))));
        assertEquals(functionCall, 
                visitor.visit(getParser("f(1, afsd.ds, \"sdfs\"; a=3,asdfs=true,\"asdfs sdfsdfgsfd sdfsf\"=true)").expr()));

        functionCall = new SQLFunctionCall("args",
                Arrays.asList(new SQLConstant("1"), new SQLField("afsd.ds"), new SQLConstant("\"sdfs\"")), Collections.<SQLParameter>emptyList());
        
        assertEquals(functionCall, visitor.visit(getParser("args(1, afsd.ds, \"sdfs\")").expr()));

        functionCall = new SQLFunctionCall("params", Collections.<SQLExpression>emptyList(), Arrays.asList(
                                new SQLParameter("a", new SQLConstant("3")),
                                new SQLParameter("asdfs", new SQLConstant("true")),
                                new SQLParameter("asdfs sdfsdfgsfd sdfsf", new SQLConstant("true"))));

        assertEquals(functionCall, visitor.visit(getParser("params(a=3,asdfs=true,\"asdfs sdfsdfgsfd sdfsf\"=true)").expr()));

        functionCall = new SQLFunctionCall("empty",Collections.<SQLExpression>emptyList(), Collections.<SQLParameter>emptyList());

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
        SQLExpression cast = new SQLFunctionCall("cast", Arrays.asList(new SQLConstant("23"), new SQLConstant("Int")), 
                Collections.emptyList());
        assertEquals(cast, visitor.visit(getParser("cast 23 as Int").expr()));
        assertNotEquals(cast, visitor.visit(getParser("cast 23 as B").expr()));
        assertNotEquals(cast, visitor.visit(getParser("cast 24 as Int").expr()));
    }

    @Test
    public void testIs() throws Exception {
        SQLExpression cast = new SQLFunctionCall("is", Arrays.asList(new SQLConstant("23"), new SQLConstant("42")), 
                Collections.emptyList());
        assertEquals(cast, visitor.visit(getParser("23 is 42").expr()));
        assertNotEquals(cast, visitor.visit(getParser("23 is 43").expr()));
        assertNotEquals(cast, visitor.visit(getParser("24 is 42").expr()));
    }

    @Test
    public void testBinaryExpressions() throws Exception {
        SQLField c1 = new SQLField("c1");
        SQLField c2 = new SQLField("c2");
        List<Pair<ArithmeticType, String>>  testData = Arrays.asList(
                new Pair<>(ArithmeticType.PLUS, "c1 + c2"),
                new Pair<>(ArithmeticType.MINUS, "c1 - c2"),
                new Pair<>(ArithmeticType.MULTIPLY, "c1 * c2"),
                new Pair<>(ArithmeticType.DIVIDE, "c1 / c2"),
                new Pair<>(ArithmeticType.MODULAR, "c1 % c2"),
                new Pair<>(ArithmeticType.POWER, "c1 ** c2"),
                new Pair<>(ArithmeticType.AND, "c1 and c2"),
                new Pair<>(ArithmeticType.OR, "c1 or c2"),
                new Pair<>(ArithmeticType.BIT_AND, "c1 & c2"),
                new Pair<>(ArithmeticType.BIT_OR, "c1 | c2"),
                new Pair<>(ArithmeticType.BIT_XOR, "c1 ^ c2"),
                new Pair<>(ArithmeticType.CONCATENATION, "c1 || c2"),
                new Pair<>(ArithmeticType.EQUAL, "c1 = c2"),
                new Pair<>(ArithmeticType.NOT_EQUAL, "c1 != c2"),
                new Pair<>(ArithmeticType.NOT_EQUAL, "c1 <> c2"),
                new Pair<>(ArithmeticType.GT, "c1 > c2"),
                new Pair<>(ArithmeticType.LT, "c1 < c2"),
                new Pair<>(ArithmeticType.GE, "c1 >= c2"),
                new Pair<>(ArithmeticType.LE, "c1 <= c2")
        );
        for (Pair<ArithmeticType, String> pair : testData) {
            assertEquals(pair.b, new SQLBinaryExpression(pair.a, c1, c2), visitor.visit(getParser(pair.b).expr()));
            assertNotEquals(pair.b, new SQLBinaryExpression(pair.a, c2, c1), visitor.visit(getParser(pair.b).expr()));
        }
    }

    @Test
    public void testUnaryExpressions() throws Exception {
        SQLField c1 = new SQLField("c1");
        List<Pair<ArithmeticType, String>>  testData = Arrays.asList(
                new Pair<>(ArithmeticType.BIT_NEG, "~ c1"),
                new Pair<>(ArithmeticType.MINUS, "-c1"),
                new Pair<>(ArithmeticType.NOT, "not c1")
        );
        for (Pair<ArithmeticType, String> pair : testData) {
            assertEquals(pair.b, new SQLUnaryExpression(pair.a, c1), visitor.visit(getParser(pair.b).expr()));
        }
    }

    @Test
    public void testLimitClause() throws Exception {
        SQLExpression limit = new SQLConstant("3");
        assertEquals(limit, visitor.visit(getParser("limit 3").limitClause()));
    }

    @Test
    public void testOrderByClause() throws Exception {
        SQLOrderByClause orderByClause = new SQLOrderByClause(
                Arrays.asList(new SQLParameter("a", new SQLConstant("3")), new SQLParameter("b", new SQLConstant("4"))),
                Arrays.asList(new SQLField("a"), new SQLField("b"), new SQLField("c")),
                "DESC"
        );
        assertEquals(orderByClause, visitor.visit(getParser("order(a=3,b=4) by a, b, c desc").orderByClause()));

        orderByClause = new SQLOrderByClause(
                Collections.<SQLParameter>emptyList(),
                Arrays.asList(new SQLField("a"), new SQLField("b"), new SQLField("c")),
                "ASC"
        );
        assertEquals(orderByClause, visitor.visit(getParser("order by a, b, c").orderByClause()));
    }

    @Test
    public void testHavingClause() throws Exception {
        SQLExpression having = new SQLConstant("3");
        assertEquals(having, visitor.visit(getParser("having 3").havingClause()));
    }

    @Test
    public void testGroupByClause() throws Exception {
        SQLGroupByClause groupByClause = new SQLGroupByClause(
                Arrays.asList(new SQLParameter("a", new SQLConstant("3")), new SQLParameter("b", new SQLConstant("4"))),
                Arrays.asList(new SQLField("a"), new SQLField("b"), new SQLField("c")));
        assertEquals(groupByClause, visitor.visit(getParser("group(a=3,b=4) by a, b, c").groupByClause()));

        groupByClause = new SQLGroupByClause(
                Collections.<SQLParameter>emptyList(),
                Arrays.asList(new SQLField("a"), new SQLField("b"), new SQLField("c")));
        assertEquals(groupByClause, visitor.visit(getParser("group by a, b, c").groupByClause()));
    }

    @Test
    public void testWhereClause() throws Exception {
        SQLExpression where = new SQLConstant("3");
        assertEquals(where, visitor.visit(getParser("where 3").whereClause()));
    }

    @Test
    public void testTablePrimary() throws Exception {
        SQLTablePrimary tablePrimary = new SQLTablePrimary(new SQLField("source"), "alias", Arrays.asList("a", "b", "c"));
        assertEquals(tablePrimary, visitor.visit(getParser("source as alias (a, b, c)").tablePrimary()));

        tablePrimary = new SQLTablePrimary(new SQLField("source"), "source",
                Collections.<String>emptyList());
        assertEquals(tablePrimary, visitor.visit(getParser("source").tablePrimary()));
    }

    @Test
    public void testJoinedTablePrimary() throws Exception {
        SQLTablePrimary tablePrimary = new SQLTablePrimary(new SQLField("source"), "alias", Arrays.asList("a", "b", "c"));
        List<SQLParameter> parameterList = 
                Arrays.asList(new SQLParameter("a", new SQLConstant("3")), new SQLParameter("b", new SQLConstant("4")));
        SQLBinaryExpression binaryExpression = new SQLBinaryExpression(ArithmeticType.EQUAL,
                new SQLField("a"), new SQLField("b"));
        SQLJoinedTablePrimary joinedTablePrimary = new SQLJoinedTablePrimary("full", parameterList, tablePrimary, binaryExpression);

        assertEquals(joinedTablePrimary,
                visitor.visit(getParser("full join(a=3,b=4) source as alias (a, b, c) on a = b").joinedTablePrimary()));

        joinedTablePrimary = new SQLJoinedTablePrimary("full", parameterList, tablePrimary, null);

        assertEquals(joinedTablePrimary,
                visitor.visit(getParser("full join(a=3,b=4) source as alias (a, b, c)").joinedTablePrimary()));

        joinedTablePrimary = new SQLJoinedTablePrimary("inner", Collections.<SQLParameter>emptyList(), tablePrimary, null);

        assertEquals(joinedTablePrimary,
                visitor.visit(getParser("join source as alias (a, b, c)").joinedTablePrimary()));
    }

    @Test
    public void testTableReference() throws Exception {
        SQLTablePrimary tablePrimary = new SQLTablePrimary(new SQLField("source"), "source", Collections.<String>emptyList());
        List<SQLParameter> parameterList =
                Arrays.asList(new SQLParameter("a", new SQLConstant("3")), new SQLParameter("b", new SQLConstant("4")));
        SQLBinaryExpression binaryExpression = new SQLBinaryExpression(ArithmeticType.EQUAL,
                new SQLField("a"), new SQLField("b"));
        
        List<SQLJoinedTablePrimary> joinedTablePrimaryList = Arrays.asList(
                new SQLJoinedTablePrimary("full", parameterList, tablePrimary, binaryExpression),
                new SQLJoinedTablePrimary("left", parameterList, tablePrimary, null),
                new SQLJoinedTablePrimary("inner", Collections.<SQLParameter>emptyList(), tablePrimary, null)
        );
        SQLTableReference tableReference = new SQLTableReference(tablePrimary, joinedTablePrimaryList);

        assertEquals(tableReference, visitor.visit(getParser(
                "source full join(a=3,b=4) source on a = b left join(a=3,b=4) source inner join source ").tableReference()));
    }

    @Test
    public void testFromClause() throws Exception {
        SQLTablePrimary tablePrimary = new SQLTablePrimary(new SQLField("source"), "source", Collections.<String>emptyList());
        List<SQLParameter> parameterList =
                Arrays.asList(new SQLParameter("a", new SQLConstant("3")), new SQLParameter("b", new SQLConstant("4")));
        SQLBinaryExpression binaryExpression = new SQLBinaryExpression(ArithmeticType.EQUAL,
                new SQLField("a"), new SQLField("b"));

        List<SQLJoinedTablePrimary> joinedTablePrimaryList = Arrays.asList(
                new SQLJoinedTablePrimary("full", parameterList, tablePrimary, binaryExpression),
                new SQLJoinedTablePrimary("left", parameterList, tablePrimary, null),
                new SQLJoinedTablePrimary("inner", Collections.<SQLParameter>emptyList(), tablePrimary, null)
        );
        SQLTableReference tableReference = new SQLTableReference(tablePrimary, joinedTablePrimaryList);
        
        List<SQLTableReference> tableReferenceList = Arrays.asList(
                tableReference,
                new SQLTableReference(tablePrimary, Collections.<SQLJoinedTablePrimary>emptyList())
        );
        
        SQLFromClause fromClause = new SQLFromClause(parameterList, tableReferenceList);

        assertEquals(fromClause, visitor.visit(getParser(
                "from(a=3,b=4) source full join(a=3,b=4) source on a = b left join(a=3,b=4) source inner join source, source").fromClause()));
    }

    @Test
    public void testStat() throws Exception {
        
        SQLTablePrimary tablePrimary = new SQLTablePrimary(new SQLField("source"), "source", Collections.<String>emptyList());
        List<SQLParameter> parameterList =
                Arrays.asList(new SQLParameter("a", new SQLConstant("3")), new SQLParameter("b", new SQLConstant("4")));
        SQLBinaryExpression binaryExpression = new SQLBinaryExpression(ArithmeticType.EQUAL,
                new SQLField("a"), new SQLField("b"));

        List<SQLJoinedTablePrimary> joinedTablePrimaryList = Arrays.asList(
                new SQLJoinedTablePrimary("full", parameterList, tablePrimary, binaryExpression),
                new SQLJoinedTablePrimary("left", parameterList, tablePrimary, null),
                new SQLJoinedTablePrimary("inner", Collections.<SQLParameter>emptyList(), tablePrimary, null)
        );
        SQLTableReference tableReference = new SQLTableReference(tablePrimary, joinedTablePrimaryList);

        List<SQLTableReference> tableReferenceList = Arrays.asList(
                tableReference,
                new SQLTableReference(tablePrimary, Collections.<SQLJoinedTablePrimary>emptyList())
        );

        SQLFromClause fromClause = new SQLFromClause(parameterList, tableReferenceList);

        SQLExpression whereClause = new SQLConstant("3");

        SQLGroupByClause groupByClause = new SQLGroupByClause(
                Arrays.asList(new SQLParameter("a", new SQLConstant("3")), new SQLParameter("b", new SQLConstant("4"))),
                Arrays.asList(new SQLField("a"), new SQLField("b"), new SQLField("c")));

        SQLExpression havingClause = new SQLConstant("3");

        SQLOrderByClause orderByClause = new SQLOrderByClause(
                Arrays.asList(new SQLParameter("a", new SQLConstant("3")), new SQLParameter("b", new SQLConstant("4"))),
                Arrays.asList(new SQLField("a"), new SQLField("b"), new SQLField("c")),
                "DESC"
        );

        SQLExpression limitClause = new SQLConstant("3");
        
        SQLTableExpression tableExpression = new SQLTableExpression(fromClause, whereClause, groupByClause, havingClause, 
                orderByClause, limitClause);
        
        List<SQLSelectItem> selectItemList = Arrays.asList(
                new SQLSelectItem(new SQLAsteriskSelectItem("obj"), new SQLField("obj1")),
                new SQLSelectItem(new SQLAsteriskSelectItem(null), null),
                new SQLSelectItem(new SQLConstant("3"), null),
                new SQLSelectItem(new SQLField("a.a.a"), new SQLField("b"))
        );
        
        SQLQuery query = new SQLQuery(selectItemList, tableExpression);

        assertEquals(query, visitor.visit(getParser(
                        "select obj.* as obj1, *, 3, a.a.a as b " +
                        "from(a=3,b=4) source " +
                        "full join(a=3,b=4) source on a = b " +
                        "left join(a=3,b=4) source " +
                        "inner join source, source " + 
                        "where 3" +
                        "group(a=3,b=4) by a, b, c " + 
                        "having 3 " +
                        "order(a=3,b=4) by a, b, c desc " +
                        "limit 3").stat()));
    }
}
