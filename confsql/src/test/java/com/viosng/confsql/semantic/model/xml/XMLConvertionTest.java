package com.viosng.confsql.semantic.model.xml;

import com.thoughtworks.xstream.XStream;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.sql.*;
import com.viosng.confsql.xml.XMLExpressionConverter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 08.03.2015
 * Time: 16:18
 */
public class XMLConvertionTest {

    private final static XStream xStream  = new XStream();

    @BeforeClass
    public static void beforeClass(){
        XMLExpressionConverter.configureXStream(xStream);
    }

    private ConfSQLVisitor<SQLExpression> visitor = new ConfSQLVisitorImpl();

    private ConfSQLParser getParser(String input) {
        return new ConfSQLParser(new CommonTokenStream(new ConfSQLLexer(new ANTLRInputStream(input))));
    }

    @Test
    public void testFull() throws Exception {
        Expression exp = visitor.visit(getParser("select a from b").stat()).convert();
        XMLExpressionConverter.XMLExpression xmlQuery = XMLExpressionConverter.convertToXML(exp);
        System.out.println(xStream.toXML(xmlQuery));

    }
}
