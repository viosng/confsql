package com.viosng.confsql.semantic.model.thrift;

import com.viosng.confsql.semantic.model.ExpressionConverter;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.ExpressionImpl;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.special.expr.CaseExpression;
import com.viosng.confsql.semantic.model.algebra.special.expr.OrderByArgExpression;
import com.viosng.confsql.semantic.model.algebra.special.expr.Parameter;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpression;
import com.viosng.confsql.semantic.model.sql.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TJSONProtocol;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class ThriftExpressionConverter implements ExpressionConverter<ThriftExpression> {

    private ThriftExpressionConverter() {}

    private static class Holder {
        private final static ThriftExpressionConverter INSTANCE = new ThriftExpressionConverter();
    }

    public static ThriftExpressionConverter getInstance() {
        return Holder.INSTANCE;
    }

    @NotNull
    public ThriftExpression convert(@NotNull Expression expression) {
        ThriftExpression thriftExpression = convertExpression(expression);
        if (thriftExpression != null) return thriftExpression;
        return convertQuery((Query)expression);
    }

    private ThriftExpression convertExpression(@NotNull Expression expression) {
        ThriftExpression thriftExpression = new ThriftExpression();
        thriftExpression.id = expression.id();
        thriftExpression.type = ThriftExpressionType.valueOf(expression.type().name());

        if (expression instanceof ExpressionImpl) {
            ExpressionImpl expressionImpl = (ExpressionImpl) expression;
            thriftExpression.arguments = expressionImpl.getArguments().stream().map(this::convert).collect(Collectors.toList());
            return thriftExpression;
        }
        switch (expression.type()) {
            case PARAMETER:
                thriftExpression.addToArguments(convert(((Parameter)expression).getValue()));
                break;
            case CONSTANT:
                thriftExpression.value = ((ValueExpression.ConstantExpression)expression).getValue();
                break;
            case ATTRIBUTE:
                thriftExpression.value = expression.toString();
                break;
            case FUNCTION_CALL:
                thriftExpression.value = ((ValueExpression.FunctionCallExpression)expression).getValue();
                thriftExpression.arguments = expression.getArguments().stream().map(this::convert).collect(Collectors.toList());
                if (thriftExpression.arguments.isEmpty()) thriftExpression.arguments = null;

                thriftExpression.parameters = ((ValueExpression.FunctionCallExpression)expression).getParameters()
                        .stream().map(this::convert).collect(Collectors.toList());
                if (thriftExpression.parameters.isEmpty()) thriftExpression.parameters = null;
                break;
            case ORDER:
                thriftExpression.addToArguments(convert(((OrderByArgExpression)expression).getArgument()));
                thriftExpression.orderType = ((OrderByArgExpression)expression).getOrderType();
                break;
            case CASE:
                thriftExpression.arguments = expression.getArguments().stream().map(
                        this::convert).collect(Collectors.toList());
                if (thriftExpression.arguments.isEmpty()) thriftExpression.arguments = null;

                thriftExpression.parameters = ((CaseExpression)expression).getParameters()
                        .stream().map(this::convert).collect(Collectors.toList());
                break;
            default: return null;
        }
        return thriftExpression;
    }

    @NotNull
    private ThriftExpression convertQuery(@NotNull Query query) {
        if (query.queryType() == Query.QueryType.FICTIVE) {
            throw new UnsupportedOperationException("Fictive query");
        }
        ThriftExpression thriftExpression = new ThriftExpression();
        thriftExpression.id = query.id();
        thriftExpression.type = ThriftExpressionType.valueOf(query.type().name());
        thriftExpression.queryType = ThriftQueryType.valueOf(query.queryType().name());
        if (!query.getParameters().isEmpty()) {
            thriftExpression.parameters = query.getParameters().stream().map(this::convert).collect(Collectors.toList());
        }
        if (!query.getSubQueries().isEmpty()) {
            thriftExpression.arguments = query.getSubQueries().stream().map(this::convert).collect(Collectors.toList());
        }
        if (!query.getRequiredSchemaAttributes().isEmpty()) {
            thriftExpression.schema = query.getRequiredSchemaAttributes().stream().map(this::convert).collect(Collectors.toList());
        }
        return thriftExpression;
    }

    private final static ConfSQLVisitor<SQLExpression> visitor = new ConfSQLVisitorImpl();

    @NotNull
    @Override
    public ThriftExpression convert(@NotNull String query) {
        return convert(
                visitor.visit(
                        new ConfSQLParser(
                                new CommonTokenStream(
                                        new ConfSQLLexer(
                                                new ANTLRInputStream(query)))).stat()).convert());
    }

    public String toJSONProtocolString(@NotNull String query){
        try {
            return new TSerializer(new TJSONProtocol.Factory()).toString(convert(query));
        } catch (TException e) {
            throw new IllegalArgumentException("TException", e);
        }
    }
}
