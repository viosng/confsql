package com.viosng.confsql.semantic.model.sql;

import com.google.common.collect.Lists;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.ExpressionImpl;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.queries.QueryBuilder;
import com.viosng.confsql.semantic.model.algebra.queries.QueryFactory;
import com.viosng.confsql.semantic.model.algebra.special.expr.CaseExpression;
import com.viosng.confsql.semantic.model.algebra.special.expr.OrderByArgExpression;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 07.03.2015
 * Time: 18:03
 */
public class SQLConvertionTest {
    private ConfSQLVisitor<SQLExpression> visitor = new ConfSQLVisitorImpl();

    private ConfSQLParser getParser(String input) {
        return new ConfSQLParser(new CommonTokenStream(new ConfSQLLexer(new ANTLRInputStream(input))));
    }

    @Test
    public void testConstant() throws Exception {
        assertEquals(ValueExpressionFactory.constant("3e-4"),
                visitor.visit(getParser("3e-4").expr()).convert());
        assertEquals(ValueExpressionFactory.constant("123"),
                visitor.visit(getParser("123").expr()).convert());
        assertEquals(ValueExpressionFactory.constant("\"sdfsd\""),
                visitor.visit(getParser("\"sdfsd\"").expr()).convert());
    }

    @Test
    public void testBinaryExpression() throws Exception {
        String c1 = "123", c2 = ".3e-4";
        assertEquals(new ExpressionImpl(ArithmeticType.PLUS, ValueExpressionFactory.constant(c1), ValueExpressionFactory.constant(c2)),
                visitor.visit(getParser(c1 + " + " + c2).expr()).convert());
        assertEquals(new ExpressionImpl(ArithmeticType.POWER, ValueExpressionFactory.constant(c1), ValueExpressionFactory.constant(c2)),
                visitor.visit(getParser(c1 + " ** " + c2).expr()).convert());
        assertEquals(new ExpressionImpl(ArithmeticType.LT, ValueExpressionFactory.constant(c1), ValueExpressionFactory.constant(c2)),
                visitor.visit(getParser(c1 + " < " + c2).expr()).convert());
        assertEquals(new ExpressionImpl(ArithmeticType.NOT_EQUAL, ValueExpressionFactory.constant(c1), ValueExpressionFactory.constant(c2)),
                visitor.visit(getParser(c1 + " != " + c2).expr()).convert());
    }

    @Test
    public void testUnaryExpression() throws Exception {
        String c2 = "123";
        assertEquals(new ExpressionImpl(ArithmeticType.MINUS, ValueExpressionFactory.constant(c2)),
                visitor.visit(getParser("-" + c2).expr()).convert());
        assertEquals(new ExpressionImpl(ArithmeticType.NOT, ValueExpressionFactory.constant(c2)),
                visitor.visit(getParser("not " + c2).expr()).convert());
        assertEquals(new ExpressionImpl(ArithmeticType.BIT_NEG, ValueExpressionFactory.constant(c2)),
                visitor.visit(getParser("~ " + c2).expr()).convert());
    }

    @Test
    public void testField() throws Exception {
        assertEquals(ValueExpressionFactory.attribute("a", "b"), visitor.visit(getParser("a.b").expr()).convert());
        assertEquals(ValueExpressionFactory.attribute("", "b"), visitor.visit(getParser("b").expr()).convert());
        assertEquals(ValueExpressionFactory.attribute("aa.v.v", "b"), visitor.visit(getParser("aa.v.v.b").expr()).convert());
    }

    @Test
    public void testParameter() throws Exception {
        assertEquals(new Parameter("a", ValueExpressionFactory.constant("1")), visitor.visit(getParser("a=1").param()).convert());
        assertEquals(new Parameter("a", ValueExpressionFactory.attribute("a", "b")), visitor.visit(getParser("a=a.b").param()).convert());
    }

    @Test
    public void testFunctionCall() throws Exception {
        List<Parameter> parameters = Arrays.asList(new Parameter("a", ValueExpressionFactory.constant("1")),
                new Parameter("b", ValueExpressionFactory.constant("2")));
        List<Expression> arguments = Arrays.asList(ValueExpressionFactory.constant("1"),
                ValueExpressionFactory.constant("2"), ValueExpressionFactory.constant("3"));

        assertEquals(ValueExpressionFactory.functionCall("func", arguments, parameters),
                visitor.visit(getParser("func(1,2,3;a=1,b=2)").expr()).convert());

        assertEquals(ValueExpressionFactory.functionCall("func1", Collections.<Expression>emptyList(), parameters),
                visitor.visit(getParser("func1(a=1,b=2)").expr()).convert());

        assertEquals(ValueExpressionFactory.functionCall("func2", arguments, Collections.<Parameter>emptyList()),
                visitor.visit(getParser("func2(1,2,3)").expr()).convert());
    }

    @Test
    public void testCaseExpression() throws Exception {
        List<Parameter> parameters = Lists.newArrayList(
                new Parameter("whenExpression0", ValueExpressionFactory.constant("1")),
                new Parameter("thenExpression0", ValueExpressionFactory.constant("2")),
                new Parameter("whenExpression1", ValueExpressionFactory.constant("3")),
                new Parameter("thenExpression1", ValueExpressionFactory.constant("4")),
                new Parameter("whenExpression2", ValueExpressionFactory.constant("5")),
                new Parameter("thenExpression2", ValueExpressionFactory.constant("6")));

        assertEquals(new CaseExpression(null, parameters),
                visitor.visit(getParser("case when 1 then 2 when 3 then 4 when 5 then 6 end").expr()).convert());

        parameters.add(new Parameter("elseExpression", ValueExpressionFactory.constant("7")));

        assertEquals(new CaseExpression(null, parameters),
                visitor.visit(getParser("case when 1 then 2 when 3 then 4 when 5 then 6 else 7 end").expr()).convert());

        assertNotEquals(new CaseExpression(ValueExpressionFactory.constant("-1"), parameters),
                visitor.visit(getParser("case 0 when 1 then 2 when 3 then 4 when 5 then 6 else 7 end").expr()).convert());

        assertEquals(new CaseExpression(ValueExpressionFactory.constant("0"), parameters),
                visitor.visit(getParser("case 0 when 1 then 2 when 3 then 4 when 5 then 6 else 7 end").expr()).convert());
    }

    @Test
    public void testSelectItem() throws Exception {
        assertEquals(ValueExpressionFactory.constant("3", "c"),
                visitor.visit(getParser("3 as c").selectItem()).convert());

        ExpressionImpl expression = new ExpressionImpl(ArithmeticType.PLUS, ValueExpressionFactory.constant("3"),
                ValueExpressionFactory.constant("4"));
        expression.setId("alias");
        assertEquals(expression, visitor.visit(getParser("3 + 4 as alias").selectItem()).convert());

        assertEquals(
                ValueExpressionFactory.attribute("", "source", "alias"),
                visitor.visit(getParser("source as alias").selectItem()).convert());
    }

    @Test
    public void testTablePrimary() throws Exception {
        Query primary = new QueryBuilder()
                .queryType(Query.QueryType.PRIMARY)
                .parameters(new Parameter("sourceName", ValueExpressionFactory.constant("source")))
                .id("alias")
                .create();
        assertEquals(primary, visitor.visit(getParser("source as alias").tablePrimary()).convert());

        Query filter = new QueryBuilder()
                .queryType(Query.QueryType.FILTER)
                .subQueries(primary)
                .requiredSchemaAttributes(ValueExpressionFactory.attribute("", "a"))
                .id("alias")
                .create();
        assertEquals(filter, visitor.visit(getParser("(select a from source as alias) as alias").tablePrimary()).convert());
    }

    @Test
    public void testTableReference() throws Exception {
        Query primary = new QueryBuilder()
                .queryType(Query.QueryType.PRIMARY)
                .parameters(new Parameter("sourceName", ValueExpressionFactory.constant("source")))
                .id("source")
                .create();
        assertEquals(primary, visitor.visit(getParser("source").tableReference()).convert());

        Query query = new QueryBuilder()
                .queryType(Query.QueryType.UNNEST)
                .parameters(
                        new Parameter("unNestObject", ValueExpressionFactory.attribute("source", "a")),
                        new Parameter("a", ValueExpressionFactory.attribute("d", "e")))
                .subQueries(primary, primary)
                .create();
        assertEquals(query, visitor.visit(getParser("source join(a=d.e) source.a").tableReference()).convert());

        query = new QueryBuilder()
                .queryType(Query.QueryType.JOIN)
                .parameters(
                        new Parameter("joinType", ValueExpressionFactory.constant("right")),
                        new Parameter("onCondition", new ExpressionImpl(ArithmeticType.LT,
                                ValueExpressionFactory.attribute("", "a"), ValueExpressionFactory.attribute("", "b"))),
                        new Parameter("a", ValueExpressionFactory.attribute("d", "e")))
                .subQueries(query, primary)
                .id("source")
                .create();
        assertEquals(query, visitor.visit(getParser(
                "source join(a=d.e) source.a right join(a=d.e) source on a<b").tableReference()).convert());

        query = new QueryBuilder()
                .queryType(Query.QueryType.JOIN)
                .parameters(new Parameter("joinType", ValueExpressionFactory.constant("inner")))
                .subQueries(query, primary)
                .id("source")
                .create();
        assertEquals(query, visitor.visit(getParser(
                "source join(a=d.e) source.a right join(a=d.e) source on a<b join source").tableReference()).convert());
    }

    @Test
    public void testFrom() throws Exception {
        Query primary = new QueryBuilder()
                .queryType(Query.QueryType.PRIMARY)
                .parameters(new Parameter("sourceName", ValueExpressionFactory.constant("source")))
                .id("source")
                .create();

        Query query = new QueryBuilder()
                .queryType(Query.QueryType.JOIN)
                .parameters(
                        new Parameter("joinType", ValueExpressionFactory.constant("fuzzy")),
                        new Parameter("onCondition", new ExpressionImpl(ArithmeticType.EQUAL,
                                ValueExpressionFactory.attribute("", "a"), ValueExpressionFactory.attribute("", "b"))),
                        new Parameter("a", ValueExpressionFactory.attribute("d", "e")))
                .subQueries(primary, primary)
                .id("source")
                .create();

        Query query1 = new QueryBuilder()
                .queryType(Query.QueryType.JOIN)
                .parameters(
                        new Parameter("joinType", ValueExpressionFactory.constant("right")),
                        new Parameter("onCondition", new ExpressionImpl(ArithmeticType.LT,
                                ValueExpressionFactory.attribute("", "a"), ValueExpressionFactory.attribute("", "b"))),
                        new Parameter("a", ValueExpressionFactory.attribute("d", "e")))
                .subQueries(query, primary)
                .create();

        QueryBuilder from = new QueryBuilder()
                .queryType(Query.QueryType.JOIN)
                .subQueries(primary, query, query1);

        assertEquals(from.create(), visitor.visit(getParser(
                "from source, source fuzzy join(a=d.e) source on a=b, " +
                        "source fuzzy join(a=d.e) source on a=b right join(a=d.e) source on a<b").fromClause()).convert());

        from.parameters(new Parameter("a", ValueExpressionFactory.constant("1")), new Parameter("b", ValueExpressionFactory.constant("2")));

        assertEquals(from.create(), visitor.visit(getParser(
                "from(a=1,b=2) source, source fuzzy join(a=d.e) source on a=b, " +
                        "source fuzzy join(a=d.e) source on a=b right join(a=d.e) source on a<b").fromClause()).convert());
    }

    @Test
    public void testQuery() throws Exception {
        QueryBuilder queryBuilder = new QueryBuilder()
                .queryType(Query.QueryType.FILTER)
                .subQueries(QueryFactory.fictive())
                .requiredSchemaAttributes(ValueExpressionFactory.attribute("", "a"), ValueExpressionFactory.constant("3"));
        assertEquals(queryBuilder.create(), visitor.visit(getParser("select a, 3").query()).convert());

        Query primary = new QueryBuilder()
                .queryType(Query.QueryType.PRIMARY)
                .parameters(new Parameter("sourceName", ValueExpressionFactory.constant("source")))
                .id("source")
                .create();
        queryBuilder.subQueries(primary);
        assertEquals(queryBuilder.create(), visitor.visit(getParser("select a, 3 from source").query()).convert());

        Query where = new QueryBuilder()
                .queryType(Query.QueryType.FILTER)
                .parameters(new Parameter("filterExpression", new ExpressionImpl(ArithmeticType.EQUAL,
                        ValueExpressionFactory.constant("3"), ValueExpressionFactory.constant("4"))))
                .subQueries(primary)
                .create();
        queryBuilder.subQueries(where);
        assertEquals(queryBuilder.create(), visitor.visit(getParser("select a, 3 from source where 3=4").query()).convert());

        Query groupBy = new QueryBuilder()
                .queryType(Query.QueryType.AGGREGATION)
                .subQueries(where)
                .parameters(
                        new Parameter("groupByArg0", ValueExpressionFactory.attribute("", "a")),
                        new Parameter("groupByArg1", ValueExpressionFactory.attribute("", "b")),
                        new Parameter("groupByArg2", ValueExpressionFactory.attribute("", "c")),
                        new Parameter("algorithm", ValueExpressionFactory.constant("\"NearestNeighbours\"")))
                .create();
        queryBuilder.subQueries(groupBy);
        assertEquals(queryBuilder.create(), visitor.visit(getParser(
                "select a, 3 from source where 3=4 group(\"algorithm\"=\"NearestNeighbours\") by a, b, c").query()).convert());

        Query having = new QueryBuilder()
                .queryType(Query.QueryType.FILTER)
                .parameters(new Parameter("filterExpression", new ExpressionImpl(ArithmeticType.EQUAL,
                        ValueExpressionFactory.constant("3"), ValueExpressionFactory.constant("4"))))
                .subQueries(groupBy)
                .create();
        queryBuilder.subQueries(having);

        Query select = queryBuilder.create();
        assertEquals(select, visitor.visit(getParser(
                "select a, 3 from source where 3=4 group(\"algorithm\"=\"NearestNeighbours\") by a, b, c having 3=4").query()).convert());

        Query orderBy = new QueryBuilder()
                .queryType(Query.QueryType.FILTER)
                .subQueries(select)
                .parameters(
                        new Parameter("orderByArg0", new OrderByArgExpression(ValueExpressionFactory.attribute("", "a"), "asc")),
                        new Parameter("orderByArg1", new OrderByArgExpression(ValueExpressionFactory.attribute("", "b"), "desc")),
                        new Parameter("c", ValueExpressionFactory.constant("\"d\"")),
                        new Parameter("type", ValueExpressionFactory.constant("order")))
                .create();
        assertEquals(orderBy, visitor.visit(getParser(
                "select a, 3 from source where 3=4 group(\"algorithm\"=\"NearestNeighbours\") by a, b, c " +
                        "having 3=4 order(\"c\"=\"d\") by a, b desc").query()).convert());

        Query limit = new QueryBuilder()
                .queryType(Query.QueryType.FILTER)
                .parameters(new Parameter("type", ValueExpressionFactory.constant("limit")),
                        new Parameter("limitValue", ValueExpressionFactory.constant("10")))
                .subQueries(orderBy)
                .create();
        assertEquals(limit, visitor.visit(getParser(
                "select a, 3 from source where 3=4 group(\"algorithm\"=\"NearestNeighbours\") by a, b, c " +
                        "having 3=4 order(\"c\"=\"d\") by a, b desc limit 10").query()).convert());

        queryBuilder.subQueries(primary);
        queryBuilder.requiredSchemaAttributes(Collections.<Expression>emptyList());
        assertEquals(queryBuilder.create(), visitor.visit(getParser("select * from source").query()).convert());
    }
}
