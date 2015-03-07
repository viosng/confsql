package com.viosng.confsql.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebraold.expressions.ArithmeticExpression;
import com.viosng.confsql.semantic.model.algebraold.expressions.PredicateExpression;
import com.viosng.confsql.semantic.model.algebraold.expressions.binary.BinaryArithmeticExpressionFactory;
import com.viosng.confsql.semantic.model.algebraold.expressions.binary.BinaryExpression;
import com.viosng.confsql.semantic.model.algebraold.expressions.binary.BinaryPredicateExpressionFactory;
import com.viosng.confsql.semantic.model.algebra.special.expr.IfExpression;
import com.viosng.confsql.semantic.model.algebra.special.expr.IfExpressionFactory;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpression;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.algebraold.expressions.unary.UnaryArithmeticExpressionFactory;
import com.viosng.confsql.semantic.model.algebraold.expressions.unary.UnaryExpression;
import com.viosng.confsql.semantic.model.algebraold.expressions.unary.UnaryPredicateExpressionFactory;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;



/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 24.12.2014
 * Time: 18:20
 */
public class XMLExpressionConverter implements XMLConverter<XMLExpressionConverter.XMLExpressionImpl, Expression> {

    private XMLExpressionConverter(){}
    
    private static class XMLExpressionConverterHolder {
        private static final XMLExpressionConverter INSTANCE = new XMLExpressionConverter();
    }
    
    public static  XMLExpressionConverter getInstance() { return XMLExpressionConverterHolder.INSTANCE; }

    @XStreamAlias("expression")
    public static class XMLExpressionImpl implements XMLExpression {
        @XStreamAsAttribute
        public String id, objectReference, value;

        @XStreamAsAttribute
        public ArithmeticType type;
        
        @XStreamImplicit
        public XMLExpressionImpl[] arguments;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof XMLExpressionImpl)) return false;

            XMLExpressionImpl that = (XMLExpressionImpl) o;

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
    public XMLExpressionImpl convertToXML(@NotNull Expression modelElement) {
        XMLExpressionImpl xmlExpressionImpl = new XMLExpressionImpl();
        xmlExpressionImpl.id = modelElement.id();
        xmlExpressionImpl.type = modelElement.type();
        switch(modelElement.type()) {
            case ATTRIBUTE:
            case GROUP:
                ValueExpression.AttributeExpression attributeExpression = (ValueExpression.AttributeExpression) modelElement;
                xmlExpressionImpl.value = attributeExpression.getValue();
                xmlExpressionImpl.objectReference = attributeExpression.getObjectReference();
                break;
            case FUNCTION_CALL:
                ValueExpression.FunctionCallExpression functionCallExpression = (ValueExpression.FunctionCallExpression) modelElement;
                xmlExpressionImpl.arguments =
                        functionCallExpression.getArguments().stream().map(this::convertToXML).toArray(XMLExpressionImpl[]::new);
            case CONSTANT:
                xmlExpressionImpl.value = ((ValueExpression) modelElement).getValue();
                break;
            case NOT:
            case MINUS:
                if (modelElement instanceof UnaryExpression) {
                    UnaryExpression unaryExpression = (UnaryExpression) modelElement;
                    xmlExpressionImpl.arguments = new XMLExpressionImpl[]{
                            convertToXML(unaryExpression.getArg())
                    };
                    break;
                }
            case IF:
                IfExpression ifExpression = (IfExpression) modelElement;
                xmlExpressionImpl.arguments = new XMLExpressionImpl[] {
                        convertToXML(ifExpression.getPredicate()),
                        convertToXML(ifExpression.getExpressionOnTrue()),
                        convertToXML(ifExpression.getExpressionOnFalse())
                };
                break;
            default:
                if (!(modelElement instanceof BinaryExpression)) throw new IllegalArgumentException("Unsupported expression type - " + 
                        modelElement.type());
                BinaryExpression binaryExpression = (BinaryExpression) modelElement;
                xmlExpressionImpl.arguments = new XMLExpressionImpl[]{
                        convertToXML(binaryExpression.getLeftArg()),
                        convertToXML(binaryExpression.getRightArg())
                };
                break;
        }
        return xmlExpressionImpl;
    }

    @NotNull
    @Override
    public Expression convertFromXML(@NotNull XMLExpressionImpl xmlElement) {
        Expression expression = processArithmeticExpressionArguments(xmlElement);
        if (expression != null) return expression;
        expression = processPredicateExpressionArguments(xmlElement);
        if (expression != null) return expression;
        switch (xmlElement.type) {
            case FUNCTION_CALL: return ValueExpressionFactory.functionCall(
                    xmlElement.value, Arrays.asList(Arrays.stream(xmlElement.arguments)
                            .map(e -> convertWithCheck(e, Expression.class)).toArray(Expression[]::new)),
                    Collections.<Parameter>emptyList(), xmlElement.id);

            case CONSTANT: return ValueExpressionFactory.constant(xmlElement.value, xmlElement.id);

            case ATTRIBUTE: return ValueExpressionFactory.attribute(xmlElement.objectReference, xmlElement.value, xmlElement.id);

            case GROUP: return ValueExpressionFactory.group(xmlElement.objectReference, xmlElement.value, Collections.emptyList(), 
                    xmlElement.id);
            
            case IF: return IfExpressionFactory.create(convertWithCheck(xmlElement.arguments[0], PredicateExpression.class),
                    convertWithCheck(xmlElement.arguments[1], Expression.class),
                    convertWithCheck(xmlElement.arguments[2], Expression.class));
            
            default: throw new IllegalArgumentException("Unsupported expression type:" + xmlElement.type);
        }
    }
    
    private static EnumSet<ArithmeticType> allowedArithmeticArithmeticTypes = EnumSet.of(ArithmeticType.PLUS, ArithmeticType.MINUS, 
            ArithmeticType.MULTIPLY, ArithmeticType.DIVIDE, ArithmeticType.POWER, ArithmeticType.GT, ArithmeticType.GE, 
            ArithmeticType.LT, ArithmeticType.LE, ArithmeticType.EQUAL);
    
    private Expression processArithmeticExpressionArguments(@NotNull XMLExpressionImpl xmlExpressionImpl) {
        if (!allowedArithmeticArithmeticTypes.contains(xmlExpressionImpl.type)) return null;
        ArithmeticExpression[] exps = Arrays.stream(xmlExpressionImpl.arguments)
                .map(e -> convertWithCheck(e, ArithmeticExpression.class)).toArray(ArithmeticExpression[]::new);
        switch (xmlExpressionImpl.type) {
            case PLUS: return BinaryArithmeticExpressionFactory.plus(exps[0], exps[1], xmlExpressionImpl.id);

            case MINUS: return exps.length == 2 
                    ? BinaryArithmeticExpressionFactory.minus(exps[0], exps[1], xmlExpressionImpl.id)
                    : UnaryArithmeticExpressionFactory.minus(exps[0], xmlExpressionImpl.id);

            case MULTIPLY: return BinaryArithmeticExpressionFactory.multiplication(exps[0], exps[1], xmlExpressionImpl.id);

            case DIVIDE: return BinaryArithmeticExpressionFactory.division(exps[0], exps[1], xmlExpressionImpl.id);

            case POWER: return BinaryArithmeticExpressionFactory.power(exps[0], exps[1], xmlExpressionImpl.id);

            case GT: return BinaryPredicateExpressionFactory.greater(exps[0], exps[1], xmlExpressionImpl.id);

            case GE: return BinaryPredicateExpressionFactory.greaterOrEqual(exps[0], exps[1], xmlExpressionImpl.id);

            case LT: return BinaryPredicateExpressionFactory.less(exps[0], exps[1], xmlExpressionImpl.id);

            case LE: return BinaryPredicateExpressionFactory.lessOrEqual(exps[0], exps[1], xmlExpressionImpl.id);

            case EQUAL: return BinaryPredicateExpressionFactory.equal(exps[0], exps[1], xmlExpressionImpl.id);
            
            default: return null;
        }
    }

    private static EnumSet<ArithmeticType> allowedPredicateArithmeticTypes = EnumSet.of(ArithmeticType.AND, ArithmeticType.OR, ArithmeticType.NOT);

    private Expression processPredicateExpressionArguments(@NotNull XMLExpressionImpl xmlExpressionImpl) {
        if (!allowedPredicateArithmeticTypes.contains(xmlExpressionImpl.type)) return null;
        PredicateExpression[] exps = Arrays.asList(xmlExpressionImpl.arguments).stream()
                .map(e -> convertWithCheck(e, ArithmeticExpression.class)).toArray(PredicateExpression[]::new);
        switch (xmlExpressionImpl.type) {
            case AND: return BinaryPredicateExpressionFactory.and(exps[0], exps[1], xmlExpressionImpl.id);

            case OR: return BinaryPredicateExpressionFactory.or(exps[0], exps[1], xmlExpressionImpl.id);

            case NOT: return UnaryPredicateExpressionFactory.not(exps[0], xmlExpressionImpl.id);

            default: return null;
        }
    }
    
    private <T extends Expression> T convertWithCheck (@NotNull XMLExpressionImpl xmlExpressionImpl, Class<T> tClass) {
        try {
            return tClass.cast(convertFromXML(xmlExpressionImpl));
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Wrong expression argument class type, need : " + tClass.toString());
        }
    }

    @Override
    public void configure( @NotNull XStream xStream) {
        xStream.processAnnotations(XMLExpressionImpl.class);
    }
}
