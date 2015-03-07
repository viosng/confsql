package com.viosng.confsql.semantic.model.sql;

import com.thoughtworks.xstream.XStream;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.ExpressionImpl;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebraold.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.xml.XMLConverter;
import com.viosng.confsql.xml.XMLExpressionConverter;
import com.viosng.confsql.xml.XMLQueryConverter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 07.03.2015
 * Time: 18:03
 */
public class SQLConvertionTest {
    private ConfSQLVisitor<SQLExpression> visitor = new ConfSQLVisitorImpl();
    private XStream xstream;

    @Before
    public void setUp() throws Exception {
        xstream = new XStream();
        XMLConverter<XMLExpressionConverter.XMLExpressionImpl, Expression> converter = XMLExpressionConverter.getInstance();
        converter.configure(xstream);
        XMLQueryConverter.getInstance().configure(xstream);
    }

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
    public void testFull() throws Exception {
        Query exp = (Query) visitor.visit(getParser("select a from b,c fuzzy join e on r=q").stat()).convert();
        System.out.println(xstream.toXML(XMLQueryConverter.getInstance().convertToXML(exp)));
    }
}
