package com.viosng.confsql.semantic.model.xml;

import com.google.common.base.Joiner;
import com.thoughtworks.xstream.XStream;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.sql.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        /*String query = "fusion (select * from a join a.nested) with select 1 with (select a, f(1,2;u=e,q=3) from b " +
                "inner join(a=e) c on q.w > \"sdfsd\"" +
                "left join d " +
                "fuzzy join(alg=\"alg1\") (select a) as r join r.a group(a=p) by f,g,h order by f desc limit 10) end";*/
        //String query = "select a, nest(b)";
        String query = Joiner.on("").join(Files.readAllLines(Paths.get("src/test/java/com/viosng/confsql/semantic/model/xml/query.sql"), StandardCharsets.UTF_8));
        System.out.println(query);
        Expression exp = visitor.visit(getParser(query).stat()).convert();
        XMLExpressionConverter.XMLExpression xmlQuery = XMLExpressionConverter.convertToXML(exp);

        FileWriter out = new FileWriter("xmlOutput.xml");
        String xmlQueryString = xStream.toXML(xmlQuery);
        out.write(xmlQueryString);
        out.close();
        System.out.println(query);
        System.out.println(xmlQueryString);
    }
}
