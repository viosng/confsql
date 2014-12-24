import com.thoughtworks.xstream.XStream;
import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.binary.BinaryArithmeticExpressionFactory;
import com.viosng.confsql.semantic.model.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.xml.XMLExpressionHelper;

import javax.xml.bind.JAXBException;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 19.12.2014
 * Time: 15:41
 */
public class Main {
    
    public static void main(String[] args) throws JAXBException {
        XStream xstream = new XStream();
        xstream.processAnnotations(XMLExpressionHelper.XMLExpression.class);
        Expression expression = BinaryArithmeticExpressionFactory.plus(ValueExpressionFactory.constant("a"), 
                ValueExpressionFactory.functionCall("b", ValueExpressionFactory.constant("c"), ValueExpressionFactory.constant("d")));
        XMLExpressionHelper.XMLExpression xmlExpression = XMLExpressionHelper.convertToXMLExpression(expression);
        String xml = xstream.toXML(xmlExpression);
        System.out.println(xml);
        System.out.println(xmlExpression.equals(xstream.fromXML(xml)));
        System.out.println(xstream.fromXML(xml));
        System.out.println(xmlExpression);
    }
    
}
