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
import com.viosng.confsql.semantic.model.queries.Query;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
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

    @NotNull
    @Override
    public Query convertFromXML(@NotNull XMLQuery xmlElement) {
        return null;
    }

    @Override
    public void configure(@NotNull XStream xStream) {
        xStream.processAnnotations(new Class[]{XMLQuery.class, XMLParameter.class});
        XMLExpressionConverter.getInstance().configure(xStream);
    }
}
