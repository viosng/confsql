package com.viosng.confsql.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.binary.BinaryExpression;
import com.viosng.confsql.semantic.model.expressions.other.ValueExpression;
import com.viosng.confsql.semantic.model.expressions.unary.UnaryExpression;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 24.12.2014
 * Time: 18:20
 */
public interface XMLExpressionHelper {

    @XStreamAlias("expression")
    public static class XMLExpression {
        @XStreamAsAttribute
        public String type, objectReference, value;
        @XStreamImplicit
        public Object[] arguments;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof XMLExpression)) return false;

            XMLExpression that = (XMLExpression) o;

            return Arrays.equals(arguments, that.arguments) && 
                    !(objectReference != null ? !objectReference.equals(that.objectReference) : that.objectReference != null) && 
                    !(type != null ? !type.equals(that.type) : that.type != null) && 
                    !(value != null ? !value.equals(that.value) : that.value != null);

        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + (objectReference != null ? objectReference.hashCode() : 0);
            result = 31 * result + (value != null ? value.hashCode() : 0);
            result = 31 * result + (arguments != null ? Arrays.hashCode(arguments) : 0);
            return result;
        }

        @Override
        public String toString() {
            return "XMLExpression{" +
                    "type='" + type + '\'' +
                    ", objectReference='" + objectReference + '\'' +
                    ", value='" + value + '\'' +
                    ", arguments=" + Arrays.toString(arguments) +
                    '}';
        }
    }

    @SuppressWarnings("unchecked")
    public static XMLExpression convertToXMLExpression(Expression expression) {
        XMLExpression xmlExpression = new XMLExpression();
        xmlExpression.type = expression.type().toString();
        switch(expression.type()) {
            case ATTRIBUTE:
            case GROUP:
                ValueExpression.AttributeExpression attributeExpression = (ValueExpression.AttributeExpression)expression;
                xmlExpression.value = attributeExpression.getValue();
                xmlExpression.objectReference = attributeExpression.getObjectReference();
                break;
            case FUNCTION_CALL:
                ValueExpression.FunctionCallExpression functionCallExpression = (ValueExpression.FunctionCallExpression)expression;
                xmlExpression.arguments =
                        functionCallExpression.getArguments().stream().map(XMLExpressionHelper::convertToXMLExpression).toArray();
            case CONSTANT:
                xmlExpression.value = expression.getName();
                break;
            case NOT:
            case UNARY_MINUS:
                UnaryExpression unaryExpression = (UnaryExpression)expression;
                xmlExpression.arguments = new XMLExpression[]{
                        convertToXMLExpression(unaryExpression.getArg())
                };
                break;
            default:
                if (!(expression instanceof BinaryExpression)) throw new IllegalArgumentException("Unsupported expression type");
                BinaryExpression binaryExpression = (BinaryExpression)expression;
                xmlExpression.arguments = new XMLExpression[]{
                        convertToXMLExpression(binaryExpression.getLeftArg()),
                        convertToXMLExpression(binaryExpression.getRightArg())
                };
                break;
        }
        return xmlExpression;
    }
    
    public static void configure(XStream xStream) {
        xStream.processAnnotations(XMLExpression.class);
    }
}
