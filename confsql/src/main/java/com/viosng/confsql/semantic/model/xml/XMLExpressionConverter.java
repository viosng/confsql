package com.viosng.confsql.semantic.model.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.viosng.confsql.semantic.model.ExpressionConverter;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.ExpressionImpl;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.special.expr.CaseExpression;
import com.viosng.confsql.semantic.model.algebra.special.expr.OrderByArgExpression;
import com.viosng.confsql.semantic.model.algebra.special.expr.Parameter;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.sql.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 08.03.2015
 * Time: 13:19
 */
public class XMLExpressionConverter implements ExpressionConverter<XMLExpressionConverter.XMLExpression>{

    public interface XMLExpression {
    }
    
    private XMLExpressionConverter() {}

    private static class Holder {
        private final static XMLExpressionConverter INSTANCE = new XMLExpressionConverter();
    }

    public static XMLExpressionConverter getInstance() {
        return Holder.INSTANCE;
    }

    @XStreamAlias("parameter")
    private static class XMLParameter implements XMLExpression {
        @XStreamAsAttribute
        public String name;

        @XStreamImplicit
        public XMLExpression[] value;

        public XMLParameter(String name, XMLExpression value) {
            this.name = name;
            this.value = new XMLExpression[]{value};
        }
    }

    @XStreamAlias("expression")
    private static class XMLExpressionImpl implements XMLExpression {
        @XStreamAsAttribute
        public String id;

        @XStreamAsAttribute
        public ArithmeticType type;

        @XStreamAsAttribute
        public String objectReference, value, orderType;

        @XStreamImplicit
        public XMLExpression[] argument;

        public List<XMLExpression> arguments;

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

    @XStreamAlias("query")
    private static class XMLQuery implements XMLExpression {
        @XStreamAsAttribute
        public String id = "";

        @XStreamAsAttribute
        public Query.QueryType type;

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
            if (type != xmlQuery.type) return false;
            if (schema != null ? !schema.equals(xmlQuery.schema) : xmlQuery.schema != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (type != null ? type.hashCode() : 0);
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
                    ", type=" + type +
                    ", parameters=" + parameters +
                    ", schema=" + schema +
                    ", arguments=" + arguments +
                    '}';
        }
    }

    @NotNull
    private XMLQuery convertQuery(@NotNull Query query) {
        XMLQuery xmlQuery = new XMLQuery();
        xmlQuery.id = query.id().equals(Expression.UNDEFINED_ID) ? null : query.id();
        xmlQuery.type = query.queryType();
        if (!query.getParameters().isEmpty()) {
            xmlQuery.parameters = query.getParameters().stream().map(p ->
                    (XMLParameter) convert(p)).collect(Collectors.toList());
        }
        if (!query.getSubQueries().isEmpty() && query.getSubQueries().get(0).queryType() != Query.QueryType.FICTIVE) {
            xmlQuery.arguments = query.getSubQueries().stream().map(
                    this::convertQuery).collect(Collectors.toList());
        }
        if (!query.getRequiredSchemaAttributes().isEmpty()) {
            xmlQuery.schema = query.getRequiredSchemaAttributes().stream().map(this::convert)
                    .collect(Collectors.toList());
        }
        return xmlQuery;
    }

    private XMLExpression convertExpressions(@NotNull Expression exp) {
        if (exp.type() == ArithmeticType.PARAMETER) {
            Parameter parameter = (Parameter) exp;
            return new XMLParameter(parameter.id(), convert(parameter.getValue()));
        }
        XMLExpressionImpl xmlExpression = new XMLExpressionImpl();
        xmlExpression.id = exp.id().equals(Expression.UNDEFINED_ID) ? null : exp.id();
        xmlExpression.type = exp.type();
        if (exp instanceof ExpressionImpl) {
            ExpressionImpl expression = (ExpressionImpl) exp;
            xmlExpression.arguments = expression.getArguments().stream().map(
                    this::convert).collect(Collectors.toList());
            return xmlExpression;
        }
        switch (exp.type()) {
            case CONSTANT:
                xmlExpression.value = ((ValueExpression.ConstantExpression)exp).getValue();
                break;
            case ATTRIBUTE:
                xmlExpression.value = exp.toString();
                break;
            case OBJECT:
                break;
            case FUNCTION_CALL:
                xmlExpression.value = ((ValueExpression.FunctionCallExpression)exp).getValue();
                xmlExpression.arguments = exp.getArguments().stream().map(
                        this::convert).collect(Collectors.toList());
                if (xmlExpression.arguments.isEmpty()) xmlExpression.arguments = null;

                xmlExpression.parameters = ((ValueExpression.FunctionCallExpression)exp).getParameters()
                        .stream().map(p -> (XMLParameter) convert(p)).collect(Collectors.toList());
                if (xmlExpression.parameters.isEmpty()) xmlExpression.parameters = null;
                break;
            case ORDER:
                xmlExpression.arguments = Collections.singletonList((((OrderByArgExpression) exp).getArgument())).stream().map(
                        this::convert).collect(Collectors.toList());
                xmlExpression.orderType = ((OrderByArgExpression)exp).getOrderType();
                break;
            case CASE:
                xmlExpression.arguments = exp.getArguments().stream().map(
                        this::convert).collect(Collectors.toList());
                if (xmlExpression.arguments.isEmpty()) xmlExpression.arguments = null;

                xmlExpression.parameters = ((CaseExpression)exp).getParameters()
                        .stream().map(p -> (XMLParameter) convert(p)).collect(Collectors.toList());
                break;
            default: return null;
        }
        return xmlExpression;
    }

    public void configureXStream(@NotNull XStream xStream) {
        xStream.processAnnotations(new Class[]{XMLQuery.class, XMLParameter.class, XMLExpressionImpl.class});
    }

    @NotNull
    @Override
    public XMLExpression convert(@NotNull Expression expression) {
        XMLExpression xmlExpression = convertExpressions(expression);
        if (xmlExpression != null) return xmlExpression;
        return convertQuery((Query)expression);
    }

    private final static ConfSQLVisitor<SQLExpression> visitor = new ConfSQLVisitorImpl();

    @NotNull
    @Override
    public XMLExpression convert(@NotNull String query) {
        return convert(
                visitor.visit(
                        new ConfSQLParser(
                                new CommonTokenStream(
                                        new ConfSQLLexer(
                                                new ANTLRInputStream(query)))).stat()).convert());
    }
}
