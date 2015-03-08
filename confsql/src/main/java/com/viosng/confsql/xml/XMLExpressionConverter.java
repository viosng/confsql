package com.viosng.confsql.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.ExpressionImpl;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.special.expr.OrderByArgExpression;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 08.03.2015
 * Time: 13:19
 */
public class XMLExpressionConverter {

    private XMLExpressionConverter() {
    }

    public interface XMLExpression {
    }

    @XStreamAlias("parameter")
    private static class XMLParameter implements XMLExpression {
        @XStreamAsAttribute
        public String name;

        public XMLExpression value;

        public XMLParameter(String name, XMLExpression value) {
            this.name = name;
            this.value = value;
        }
    }

    @XStreamAlias("expression")
    private static class XMLExpressionImpl implements XMLExpression {
        @XStreamAsAttribute
        public String id, objectReference, value, orderType;

        @XStreamAsAttribute
        public ArithmeticType type;

        @XStreamImplicit
        public List<XMLExpression> arguments;

        @XStreamImplicit
        public List<XMLParameter> parameters;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof XMLExpressionImpl)) return false;

            XMLExpressionImpl that = (XMLExpressionImpl) o;

            if (arguments != null ? !arguments.equals(that.arguments) : that.arguments != null) return false;
            if (id != null ? !id.equals(that.id) : that.id != null) return false;
            if (objectReference != null ? !objectReference.equals(that.objectReference) : that.objectReference != null)
                return false;
            if (orderType != null ? !orderType.equals(that.orderType) : that.orderType != null) return false;
            if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;
            if (type != that.type) return false;
            if (value != null ? !value.equals(that.value) : that.value != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (objectReference != null ? objectReference.hashCode() : 0);
            result = 31 * result + (value != null ? value.hashCode() : 0);
            result = 31 * result + (orderType != null ? orderType.hashCode() : 0);
            result = 31 * result + (type != null ? type.hashCode() : 0);
            result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
            result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "XMLExpressionImpl{" +
                    "id='" + id + '\'' +
                    ", objectReference='" + objectReference + '\'' +
                    ", value='" + value + '\'' +
                    ", orderType='" + orderType + '\'' +
                    ", type=" + type +
                    ", arguments=" + arguments +
                    ", parameters=" + parameters +
                    '}';
        }
    }

    private static XMLExpression convertExpressions(@NotNull Expression exp) {
        if (exp.type() == ArithmeticType.PARAMETER) {
            Parameter parameter = (Parameter) exp;
            return new XMLParameter(parameter.id(), convertToXML(parameter.getValue()));
        }
        XMLExpressionImpl xmlExpression = new XMLExpressionImpl();
        xmlExpression.id = exp.id();
        xmlExpression.type = exp.type();
        if (exp instanceof ExpressionImpl) {
            ExpressionImpl expression = (ExpressionImpl) exp;
            xmlExpression.arguments = expression.getArguments().stream().map(
                    XMLExpressionConverter::convertToXML).collect(Collectors.toList());
            return xmlExpression;
        }
        switch (exp.type()) {
            case CONSTANT:
                xmlExpression.value = ((ValueExpression.ConstantExpression)exp).getValue();
                break;
            case ATTRIBUTE:
                xmlExpression.objectReference = ((ValueExpression.AttributeExpression)exp).getObjectReference();
                xmlExpression.value = ((ValueExpression.AttributeExpression)exp).getValue();
                break;
            case FUNCTION_CALL:
                xmlExpression.arguments = exp.getArguments().stream().map(
                        XMLExpressionConverter::convertToXML).collect(Collectors.toList());
                xmlExpression.parameters = ((ValueExpression.FunctionCallExpression)exp).getParameters()
                        .stream().map(p -> (XMLParameter) convertToXML(p)).collect(Collectors.toList());
            case ORDER:
                xmlExpression.arguments = Arrays.asList(convertToXML(((OrderByArgExpression)exp).getArgument()));
                xmlExpression.orderType = ((OrderByArgExpression)exp).getOrderType();
                break;
            default: return null;
        }
        return xmlExpression;
    }

    @XStreamAlias("query")
    private static class XMLQuery implements XMLExpression {
        @XStreamAsAttribute
        public String id = "";

        @XStreamAsAttribute
        public Query.QueryType queryType;

        public List<XMLParameter> parameters;

        public List<XMLExpression> schema;

        public List<XMLQuery> arguments;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof XMLQuery)) return false;

            XMLQuery xmlQuery = (XMLQuery) o;

            if (arguments != null ? !arguments.equals(xmlQuery.arguments) : xmlQuery.arguments != null) return false;
            if (id != null ? !id.equals(xmlQuery.id) : xmlQuery.id != null) return false;
            if (parameters != null ? !parameters.equals(xmlQuery.parameters) : xmlQuery.parameters != null)
                return false;
            if (queryType != xmlQuery.queryType) return false;
            if (schema != null ? !schema.equals(xmlQuery.schema) : xmlQuery.schema != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (queryType != null ? queryType.hashCode() : 0);
            result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
            result = 31 * result + (schema != null ? schema.hashCode() : 0);
            result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
            return result;
        }

        @Override
        public String

        toString() {
            return "XMLQuery{" +
                    "id='" + id + '\'' +
                    ", queryType=" + queryType +
                    ", parameters=" + parameters +
                    ", schema=" + schema +
                    ", arguments=" + arguments +
                    '}';
        }
    }

    @NotNull
    private static XMLQuery convertQuery(@NotNull Query query) {
        XMLQuery xmlQuery = new XMLQuery();
        xmlQuery.id = query.id();
        xmlQuery.queryType = query.queryType();
        if (!query.getParameters().isEmpty()) {
            xmlQuery.parameters = query.getParameters().stream().map(p ->
                    (XMLParameter) convertToXML(p)).collect(Collectors.toList());
        }
        if (!query.getSubQueries().isEmpty()) {
            xmlQuery.arguments = query.getSubQueries().stream().map(
                    XMLExpressionConverter::convertQuery).collect(Collectors.toList());
        }
        if (!query.getRequiredSchemaAttributes().isEmpty()) {
            xmlQuery.schema = query.getRequiredSchemaAttributes().stream().map(XMLExpressionConverter::convertToXML)
                    .collect(Collectors.toList());
        }
        return xmlQuery;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static XMLExpression convertToXML(@NotNull Expression expression) {
        XMLExpression xmlExpression = convertExpressions(expression);
        if (xmlExpression != null) return xmlExpression;
        return convertQuery((Query)expression);
    }

    public static void configureXStream(@NotNull XStream xStream) {
        xStream.processAnnotations(new Class[]{XMLQuery.class, XMLParameter.class, XMLExpressionImpl.class});
    }
}
