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
import com.viosng.confsql.semantic.model.algebra.expressions.Expression;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.algebra.Query;
import com.viosng.confsql.semantic.model.algebra.QueryBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import static com.viosng.confsql.semantic.model.algebra.Query.QueryType;

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
        public String id = "";

        @XStreamAsAttribute
        public QueryType queryType;

        public XMLParameter[] parameters = new XMLParameter[0];

        public XMLExpressionConverter.XMLExpression[] schema = new XMLExpressionConverter.XMLExpression[0];
        
        public XMLModelElement[] arguments = new XMLModelElement[0];

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof XMLQuery)) return false;

            XMLQuery xmlQuery = (XMLQuery) o;

            return Arrays.equals(arguments, xmlQuery.arguments) 
                    && !(id != null ? !id.equals(xmlQuery.id) : xmlQuery.id != null) 
                    && Arrays.equals(parameters, xmlQuery.parameters) 
                    && Arrays.equals(schema, xmlQuery.schema) 
                    && queryType == xmlQuery.queryType;

        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (queryType != null ? queryType.hashCode() : 0);
            result = 31 * result + (parameters != null ? Arrays.hashCode(parameters) : 0);
            result = 31 * result + (schema != null ? Arrays.hashCode(schema) : 0);
            result = 31 * result + (arguments != null ? Arrays.hashCode(arguments) : 0);
            return result;
        }

    }

    private static EnumSet<QueryType> typesWithSchema = EnumSet.of(QueryType.FILTER, QueryType.AGGREGATION, QueryType.NEST, QueryType.GROUP_JOIN);
    
    @NotNull
    @Override
    public XMLQuery convertToXML(@NotNull Query query) {
        XMLQuery xmlQuery = new XMLQuery();
        xmlQuery.id = query.id();
        xmlQuery.queryType = query.type();
        xmlQuery.parameters = query.getParameters().stream().map(p -> new XMLParameter(p.getName(), p.getValue()))
                .toArray(XMLParameter[]::new);
        xmlQuery.arguments = Stream.concat(
                query.getSubQueries().stream().map(this::convertToXML),
                query.getArgumentExpressions().stream().map(XMLExpressionConverter.getInstance()::convertToXML))
                .toArray(XMLModelElement[]::new);
        if (typesWithSchema.contains(query.type())) {
            xmlQuery.schema = query.getRequiredSchemaAttributes().stream().map(XMLExpressionConverter.getInstance()::convertToXML)
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
                .id(xml.id)
                .queryType(xml.queryType)
                .argumentExpressions(convertExpressions(extractElementsWithType(xml.arguments, XMLExpressionConverter.XMLExpression.class)))
                .parameters(convertParameters(xml.parameters))
                .requiredSchemaAttributes(convertExpressions(Arrays.stream(xml.schema)))
                .subQueries(extractElementsWithType(xml.arguments, XMLQuery.class).map(this::convertFromXML).toArray(Query[]::new))
                .create();
    }

    @Override
    public void configure(@NotNull XStream xStream) {
        xStream.processAnnotations(new Class[]{XMLQuery.class, XMLParameter.class});
        XMLExpressionConverter.getInstance().configure(xStream);
    }
}
