package com.viosng.confsql.semantic.model.sql;

import com.thoughtworks.xstream.XStream;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.xml.XMLConverter;
import com.viosng.confsql.xml.XMLExpressionConverter;
import com.viosng.confsql.xml.XMLQueryConverter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Before;
import org.junit.Test;

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
    public void testFull() throws Exception {
        Query exp = (Query) visitor.visit(getParser("select a from(p=c) b(a,d,e)").stat()).convert();
        System.out.println(xstream.toXML(XMLQueryConverter.getInstance().convertToXML(exp)));
    }
}
