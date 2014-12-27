package com.viosng.confsql.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.viosng.confsql.semantic.model.expressions.ArithmeticExpression;
import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.PredicateExpression;
import com.viosng.confsql.semantic.model.expressions.binary.BinaryArithmeticExpressionFactory;
import com.viosng.confsql.semantic.model.expressions.binary.BinaryExpression;
import com.viosng.confsql.semantic.model.expressions.binary.BinaryPredicateExpressionFactory;
import com.viosng.confsql.semantic.model.expressions.other.ValueExpression;
import com.viosng.confsql.semantic.model.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.expressions.unary.UnaryArithmeticExpressionFactory;
import com.viosng.confsql.semantic.model.expressions.unary.UnaryExpression;
import com.viosng.confsql.semantic.model.expressions.unary.UnaryPredicateExpressionFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;

import static com.viosng.confsql.semantic.model.expressions.Expression.Type;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 24.12.2014
 * Time: 18:20
 */
public class XMLExpressionConverter implements XMLConverter<XMLExpressionConverter.XMLExpression, Expression> {

    private XMLExpressionConverter(){}
    
    private static class XMLExpressionConverterHolder {
        private static final XMLExpressionConverter INSTANCE = new XMLExpressionConverter();
    }
    
    public static  XMLExpressionConverter getInstance() { return XMLExpressionConverterHolder.INSTANCE; }

    @XStreamAlias("expression")
    public static class XMLExpression implements XMLConverter.XMLModelElement {
        @XStreamAsAttribute
        public String objectReference, value;

        @XStreamAsAttribute
        public Expression.Type type;
        
        @XStreamImplicit
        public XMLExpression[] arguments;

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

    @NotNull
    @SuppressWarnings("unchecked")
    public XMLExpression convertToXMLExpression(@NotNull Expression expression) {
        XMLExpression xmlExpression = new XMLExpression();
        xmlExpression.type = expression.type();
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
                        functionCallExpression.getArguments().stream().map(this::convertToXMLExpression).toArray(XMLExpression[]::new);
            case CONSTANT:
                xmlExpression.value = ((ValueExpression.ConstantExpression)expression).getValue();
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

    @NotNull
    @Override
    public Expression convertFromXMLExpression(@NotNull XMLExpression xmlExpression) {
        Expression expression = processArithmeticExpressionArguments(xmlExpression);
        if (expression != null) return expression;
        expression = processPredicateExpressionArguments(xmlExpression);
        if (expression != null) return expression;
        switch (xmlExpression.type) {
            case FUNCTION_CALL: return ValueExpressionFactory.functionCall(
                    xmlExpression.value, Arrays.asList(xmlExpression.arguments).stream()
                            .map(e -> convertWithCheck(e, Expression.class)).toArray(Expression[]::new));

            case CONSTANT: return ValueExpressionFactory.constant(xmlExpression.value);

            case ATTRIBUTE: return ValueExpressionFactory.attribute(xmlExpression.objectReference, xmlExpression.value);

            case GROUP: return ValueExpressionFactory.group(xmlExpression.objectReference, xmlExpression.value);
            
            default: throw new IllegalArgumentException("Unsupported expression type:" + xmlExpression.type);
        }
    }
    
    private static EnumSet<Expression.Type> allowedArithmeticTypes = EnumSet.of(Type.PLUS, Type.MINUS, 
            Type.MULTIPLICATION, Type.DIVISION, Type.POWER, Type.UNARY_MINUS, Type.GREATER, Type.GREATER_OR_EQUAL, 
            Type.LESS, Type.LESS_OR_EQUAL, Type.EQUAL);
    
    private Expression processArithmeticExpressionArguments(@NotNull XMLExpression xmlExpression) {
        if (!allowedArithmeticTypes.contains(xmlExpression.type)) return null;
        ArithmeticExpression[] exps = Arrays.asList(xmlExpression.arguments).stream()
                .map(e -> convertWithCheck(e, ArithmeticExpression.class)).toArray(ArithmeticExpression[]::new);
        switch (xmlExpression.type) {
            case PLUS: return BinaryArithmeticExpressionFactory.plus(exps[0], exps[1]);

            case MINUS: return BinaryArithmeticExpressionFactory.minus(exps[0], exps[1]);

            case MULTIPLICATION: return BinaryArithmeticExpressionFactory.multiplication(exps[0], exps[1]);

            case DIVISION: return BinaryArithmeticExpressionFactory.division(exps[0], exps[1]);

            case POWER: return BinaryArithmeticExpressionFactory.power(exps[0], exps[1]);

            case UNARY_MINUS: return UnaryArithmeticExpressionFactory.minus(exps[0]);

            case GREATER: return BinaryPredicateExpressionFactory.greater(exps[0], exps[1]);

            case GREATER_OR_EQUAL: return BinaryPredicateExpressionFactory.greaterOrEqual(exps[0], exps[1]);

            case LESS: return BinaryPredicateExpressionFactory.less(exps[0], exps[1]);

            case LESS_OR_EQUAL: return BinaryPredicateExpressionFactory.lessOrEqual(exps[0], exps[1]);

            case EQUAL: return BinaryPredicateExpressionFactory.equal(exps[0], exps[1]);
            
            default: return null;
        }
    }

    private static EnumSet<Expression.Type> allowedPredicateTypes = EnumSet.of(Type.AND, Type.OR, Type.NOT);

    private Expression processPredicateExpressionArguments(@NotNull XMLExpression xmlExpression) {
        if (!allowedPredicateTypes.contains(xmlExpression.type)) return null;
        PredicateExpression[] exps = Arrays.asList(xmlExpression.arguments).stream()
                .map(e -> convertWithCheck(e, ArithmeticExpression.class)).toArray(PredicateExpression[]::new);
        switch (xmlExpression.type) {
            case AND: return BinaryPredicateExpressionFactory.and(exps[0], exps[1]);

            case OR: return BinaryPredicateExpressionFactory.or(exps[0], exps[1]);

            case NOT: return UnaryPredicateExpressionFactory.not(exps[0]);

            default: return null;
        }
    }
    
    private <T extends Expression> T convertWithCheck (@NotNull XMLExpression xmlExpression, Class<T> tClass) {
        try {
            return tClass.cast(convertFromXMLExpression(xmlExpression));
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Wrong expression argument class type, need : " + tClass.toString());
        }
    }

    public void configure( @NotNull XStream xStream) {
        xStream.processAnnotations(XMLExpression.class);
    }
}
