package com.viosng.confsql.semantic.model.thrift;

import com.viosng.confsql.semantic.model.ExpressionConverter;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.ExpressionImpl;
import com.viosng.confsql.semantic.model.algebra.queries.Query;
import com.viosng.confsql.semantic.model.algebra.special.expr.OrderByArgExpression;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpression;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
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
            case CONSTANT:
                thriftExpression.value = ((ValueExpression.ConstantExpression)expression).getValue();
                break;
            case ATTRIBUTE:
                thriftExpression.objectReference = ((ValueExpression.AttributeExpression)expression).getObjectReference();
                thriftExpression.value = ((ValueExpression.AttributeExpression)expression).getValue();
                break;
            case FUNCTION_CALL:
                thriftExpression.value = ((ValueExpression.FunctionCallExpression)expression).getValue();
                thriftExpression.arguments = expression.getArguments().stream().map(this::convert).collect(Collectors.toList());
                if (thriftExpression.arguments.isEmpty()) thriftExpression.arguments = null;

                thriftExpression.parameters = ((ValueExpression.FunctionCallExpression)expression).getParameters()
                        .stream().map(this::convertParameter).collect(Collectors.toList());
                if (thriftExpression.parameters.isEmpty()) thriftExpression.parameters = null;
                break;
            case ORDER:
                thriftExpression.arguments = Arrays.asList((((OrderByArgExpression) expression).getArgument())).stream().map(
                        this::convert).collect(Collectors.toList());
                thriftExpression.orderType = ((OrderByArgExpression)expression).getOrderType();
                break;
            default: return null;
        }
        return thriftExpression;
    }

    @NotNull
    private ThriftExpression convertQuery(@NotNull Query query) {
        ThriftExpression thriftExpression = new ThriftExpression();
        thriftExpression.id = query.id();
        thriftExpression.type = ThriftExpressionType.valueOf(query.type().name());
        if (!query.getParameters().isEmpty()) {
            thriftExpression.parameters = query.getParameters().stream().map(this::convertParameter).collect(Collectors.toList());
        }
        if (!query.getSubQueries().isEmpty() && query.getSubQueries().get(0).queryType() != Query.QueryType.FICTIVE) {
            thriftExpression.arguments = query.getSubQueries().stream().map(this::convert).collect(Collectors.toList());
        }
        if (!query.getRequiredSchemaAttributes().isEmpty()) {
            thriftExpression.schema = query.getRequiredSchemaAttributes().stream().map(this::convert).collect(Collectors.toList());
        }
        return thriftExpression;
    }

    @NotNull
    private ThriftParameter convertParameter(@NotNull Parameter parameter) {
        ThriftParameter thriftParameter = new ThriftParameter();
        thriftParameter.name = parameter.id();
        thriftParameter.value = convert(parameter.getValue());
        return thriftParameter;
    }
}
