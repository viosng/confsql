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
import lombok.Data;
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
    @Data
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
    }

    @XStreamAlias("query")
    @Data
    private static class XMLQuery implements XMLExpression {
        @XStreamAsAttribute
        public String id = "";

        @XStreamAsAttribute
        public Query.QueryType type;

        public List<XMLParameter> parameters;

        public List<XMLExpression> schema;

        public List<XMLQuery> arguments;
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
