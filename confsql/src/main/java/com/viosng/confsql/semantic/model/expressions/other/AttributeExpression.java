package com.viosng.confsql.semantic.model.expressions.other;

import com.viosng.confsql.semantic.model.expressions.ArithmeticExpression;
import com.viosng.confsql.semantic.model.expressions.PredicateExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 15:33
 */
public class AttributeExpression extends AbstractAttributeExpression implements ArithmeticExpression, PredicateExpression {
    
    public AttributeExpression(@NotNull String objectReference, @NotNull String value) {
        super(objectReference, value);
    }

    @NotNull
    @Override
    public Type type() {
        return Type.ATTRIBUTE;
    }
}
