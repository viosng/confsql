import com.thoughtworks.xstream.XStream;
import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.binary.BinaryArithmeticExpressionFactory;
import com.viosng.confsql.semantic.model.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.xml.XMLConverter;
import com.viosng.confsql.xml.XMLExpressionConverter;

import javax.xml.bind.JAXBException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 19.12.2014
 * Time: 15:41
 */
public class Main {
    
    public static void main(String[] args) throws JAXBException {
        XStream xstream = new XStream();
        XMLConverter<XMLExpressionConverter.XMLExpression, Expression> converter = XMLExpressionConverter.getInstance();
        converter.configure(xstream);
        
        Expression expression = BinaryArithmeticExpressionFactory.plus(ValueExpressionFactory.constant("a"), 
                ValueExpressionFactory.functionCall("b", 
                        Arrays.asList(ValueExpressionFactory.constant("c"), ValueExpressionFactory.constant("d"))));
        
        XMLExpressionConverter.XMLExpression xmlExpression = XMLExpressionConverter.getInstance().convertToXMLExpression(expression);
        String xml = xstream.toXML(xmlExpression);
        System.out.println(xml);
        System.out.println(xmlExpression.equals(xstream.fromXML(xml)));
        System.out.println(xstream.fromXML(xml));
        System.out.println(xmlExpression);
        System.out.println(expression.equals(converter.convertFromXMLExpression(xmlExpression)));
        System.out.println(expression);
        System.out.println(converter.convertFromXMLExpression(xmlExpression));
        
    }
    
}
