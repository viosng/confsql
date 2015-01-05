package com.viosng.confsql.xml;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 03.01.2015
 * Time: 13:18
 */

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.queries.Query;
import com.viosng.confsql.semantic.model.queries.QueryBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import static com.viosng.confsql.semantic.model.queries.Query.Type;

public class XMLQueryConverter implements XMLConverter<XMLQueryConverter.XMLQuery, Query>{

    private XMLQueryConverter(){}

    private static class XMLQueryConverterHolder {
        private static final XMLQueryConverter INSTANCE = new XMLQueryConverter();
    }

    public static XMLQueryConverter getInstance() { return XMLQueryConverterHolder.INSTANCE; }

    @XStreamAlias("parameter")
    public static class XMLParameter implements XMLConverter.XMLModelElement {
        @XStreamAsAttribute
        public String name, value;

        public XMLParameter(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    @XStreamAlias("query")
    public static class XMLQuery implements XMLConverter.XMLModelElement {
        @XStreamAsAttribute
        public String id;

        @XStreamAsAttribute
        public Type type;

        @XStreamImplicit
        public XMLParameter[] parameters;

        @XStreamImplicit
        public XMLExpressionConverter.XMLExpression[] schema;

        @XStreamImplicit
        public XMLModelElement[] arguments;
    }

    private static EnumSet<Type> TYPES_WITH_SCHEMA = EnumSet.of(Type.FILTER, Type.AGGREGATION, Type.NEST, Type.GROUP_JOIN);
    
    @NotNull
    @Override
    public XMLQuery convertToXML(@NotNull Query query) {
        XMLQuery xmlQuery = new XMLQuery();
        xmlQuery.id = query.id();
        xmlQuery.type = query.type();
        xmlQuery.parameters = query.getParameters().stream().map(p -> new XMLParameter(p.getName(), p.getValue()))
                .toArray(XMLParameter[]::new);
        xmlQuery.arguments = Stream.concat(
                query.getSubQueries().stream().map(this::convertToXML),
                query.getArgumentExpressions().stream().map(XMLExpressionConverter.getInstance()::convertToXML))
                .toArray(XMLModelElement[]::new);
        if (TYPES_WITH_SCHEMA.contains(query.type())) {
            xmlQuery.schema = query.getSchemaAttributes().stream().map(XMLExpressionConverter.getInstance()::convertToXML)
                    .toArray(XMLExpressionConverter.XMLExpression[]::new);
        }
        return xmlQuery;
    }

    private static List<Parameter> convertParameters(XMLParameter[] xmlParameters) {
        return Arrays.asList(Arrays.asList(xmlParameters).stream()
                .map(x -> new Parameter(x.name, x.value)).toArray(Parameter[]::new));
    }

    private static List<Expression> convertExpressions(Stream<XMLExpressionConverter.XMLExpression> xmlExpressions) {
        return Arrays.asList(xmlExpressions.map(XMLExpressionConverter.getInstance()::convertFromXML).toArray(Expression[]::new));
    }
    
    private static <T extends XMLModelElement> Stream<T> extractElementsWithType(XMLModelElement[] elements, Class<T> tClass) {
        return Arrays.stream(elements).filter(tClass::isInstance).map(tClass::cast);
    }
    
    @NotNull
    @Override
    public Query convertFromXML(@NotNull XMLQuery xml) {
        return new QueryBuilder()
                .setId(xml.id)
                .setType(xml.type)
                .setArgumentExpressions(convertExpressions(extractElementsWithType(xml.arguments, XMLExpressionConverter.XMLExpression.class)))
                .setParameters(convertParameters(xml.parameters))
                .setSchemaAttributes(convertExpressions(Arrays.stream(xml.schema)))
                .setSubQueries(extractElementsWithType(xml.arguments, XMLQuery.class).map(this::convertFromXML).toArray(Query[]::new))
                .create();
    }

    @Override
    public void configure(@NotNull XStream xStream) {
        xStream.processAnnotations(new Class[]{XMLQuery.class, XMLParameter.class});
        XMLExpressionConverter.getInstance().configure(xStream);
    }
}
