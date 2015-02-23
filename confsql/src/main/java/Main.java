import com.thoughtworks.xstream.XStream;
import com.viosng.confsql.semantic.model.algebra.Query;
import com.viosng.confsql.semantic.model.algebra.QueryBuilder;
import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.binary.BinaryArithmeticExpressionFactory;
import com.viosng.confsql.semantic.model.expressions.binary.BinaryPredicateExpressionFactory;
import com.viosng.confsql.semantic.model.expressions.other.IfExpressionFactory;
import com.viosng.confsql.semantic.model.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.expressions.unary.UnaryPredicateExpressionFactory;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.sql.*;
import com.viosng.confsql.xml.XMLConverter;
import com.viosng.confsql.xml.XMLExpressionConverter;
import com.viosng.confsql.xml.XMLQueryConverter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 19.12.2014
 * Time: 15:41
 */
public class Main {
    
    public static void testXML() {
        XStream xstream = new XStream();
        XMLConverter<XMLExpressionConverter.XMLExpression, Expression> converter = XMLExpressionConverter.getInstance();
        converter.configure(xstream);

        Expression expression = BinaryArithmeticExpressionFactory.plus(ValueExpressionFactory.constant("a"),
                ValueExpressionFactory.functionCall("b",
                        Arrays.asList(IfExpressionFactory.create(UnaryPredicateExpressionFactory.not(ValueExpressionFactory.constant("nnn")),
                                        ValueExpressionFactory.constant("true"), ValueExpressionFactory.constant("false")),
                                ValueExpressionFactory.constant("d"))));

        XMLExpressionConverter.XMLExpression xmlExpression = XMLExpressionConverter.getInstance().convertToXML(expression);
        String xml = xstream.toXML(xmlExpression);
        System.out.println(xml);
        System.out.println(xmlExpression.equals(xstream.fromXML(xml)));
        System.out.println(xstream.fromXML(xml));
        System.out.println(xmlExpression);
        System.out.println(expression.equals(converter.convertFromXML(xmlExpression)));
        System.out.println(expression);
        System.out.println(converter.convertFromXML(xmlExpression));

        Query q = new QueryBuilder()
                .subQueries(new QueryBuilder().argumentExpressions(ValueExpressionFactory.constant("a", "b"))
                        .id("prim").create(Query.QueryType.PRIMARY))
                .argumentExpressions(BinaryPredicateExpressionFactory.greater(ValueExpressionFactory.attribute("prim", "c"),
                        ValueExpressionFactory.constant("30")))
                .parameters(new Parameter("precision", "10"), new Parameter("size", "100"))
                .id("filt")
                .queryType(Query.QueryType.FILTER)
                .create();
        System.out.println(q);
        XMLQueryConverter.getInstance().configure(xstream);
        XMLQueryConverter qConv = XMLQueryConverter.getInstance();
        XMLQueryConverter.XMLQuery xmlQ = qConv.convertToXML(q);
        System.out.println(xstream.toXML(xmlQ));
        System.out.println(q.equals(qConv.convertFromXML((XMLQueryConverter.XMLQuery)xstream.fromXML(xstream.toXML(xmlQ)))));
        
    }
    
    public static void main(String[] args) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream("TRUE AND UNKNOWN OR FALSE AND TRUE");
        ConfSQLLexer lexer = new ConfSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ConfSQLParser parser = new ConfSQLParser(tokens);
        ConfSQLVisitor<SQLExpression> visitor = new ConfSQLVisitorImpl();
        System.out.println(visitor.visit(parser.stat()));
    }
    
    
    
}
