package com.viosng.confsql.semantic.model.algebraold;

import com.viosng.confsql.semantic.model.algebraold.expressions.PredicateExpression;
import com.viosng.confsql.semantic.model.algebraold.expressions.binary.BinaryArithmeticExpressionFactory;
import com.viosng.confsql.semantic.model.algebraold.expressions.binary.BinaryExpression;
import com.viosng.confsql.semantic.model.algebraold.expressions.binary.BinaryPredicateExpressionFactory;
import com.viosng.confsql.semantic.model.algebraold.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.algebraold.expressions.unary.UnaryArithmeticExpressionFactory;
import com.viosng.confsql.semantic.model.algebraold.expressions.unary.UnaryExpression;
import com.viosng.confsql.semantic.model.algebraold.expressions.unary.UnaryPredicateExpressionFactory;
import com.viosng.confsql.semantic.model.other.Context;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 08.01.2015
 * Time: 22:01
 */
public class ExpressionContextTest {
    @Test
    public void testBinaryExpressions() throws Exception {
        final String object = "object";
        Context context = new Context() {
            @Override
            public boolean hasReference(@NotNull String objectReference) {
                return objectReference.equals(object);
            }

            @Override
            public boolean hasAttribute(@NotNull String objectReference, @NotNull String attribute) {
                return objectReference.equals(object) && attribute.length() == 1;
            }
        };
        
        // all binary expressions has the default implementation of verification that's why we need to check only one type.
        BinaryExpression expression = BinaryArithmeticExpressionFactory.plus(ValueExpressionFactory.constant("a"), 
                ValueExpressionFactory.constant("b"));
        assertTrue(expression.verify(context).isOk());
        
        expression = BinaryArithmeticExpressionFactory.minus(ValueExpressionFactory.attribute(object, "b"),
                ValueExpressionFactory.attribute(object, "c"));
        assertTrue(expression.verify(context).isOk());
        
        expression = BinaryArithmeticExpressionFactory.minus(ValueExpressionFactory.attribute(object, "b"),
                ValueExpressionFactory.attribute(object, "cd"));
        assertFalse(expression.verify(context).isOk());
    }

    @Test
    public void testBinaryPredicatesPredicates() throws Exception {
        PredicateExpression predicate = BinaryPredicateExpressionFactory.and(ValueExpressionFactory.constant("constant"),
                BinaryPredicateExpressionFactory.or(ValueExpressionFactory.constant("one"), ValueExpressionFactory.constant("two")));
        assertFalse(predicate.verify(mock(Context.class)).isOk());
        predicate = BinaryPredicateExpressionFactory.and(ValueExpressionFactory.constant("True"),
                ValueExpressionFactory.functionCall("function", Arrays.asList(ValueExpressionFactory.constant("c"), 
                        ValueExpressionFactory.constant("d"))));
        assertTrue(predicate.verify(mock(Context.class)).isOk());
    }

    @Test
    public void testUnaryExpressions() throws Exception {
        final String object = "object";
        Context context = new Context() {
            @Override
            public boolean hasReference(@NotNull String objectReference) {
                return objectReference.equals(object);
            }

            @Override
            public boolean hasAttribute(@NotNull String objectReference, @NotNull String attribute) {
                return objectReference.equals(object) && attribute.length() == 1;
            }
        };

        // all binary expressions has the default implementation of verification that's why we need to check only one type.
        UnaryExpression expression = UnaryArithmeticExpressionFactory.minus(ValueExpressionFactory.constant("a"));
        assertTrue(expression.verify(context).isOk());
        
        expression = UnaryArithmeticExpressionFactory.minus(ValueExpressionFactory.attribute(object, "b"));
        assertTrue(expression.verify(context).isOk());
        
        expression = UnaryArithmeticExpressionFactory.minus(ValueExpressionFactory.attribute(object, "cd"));
        assertFalse(expression.verify(context).isOk());
        
        expression = UnaryPredicateExpressionFactory.not(ValueExpressionFactory.constant("TRUE"));
        assertTrue(expression.verify(context).isOk());

        expression = UnaryPredicateExpressionFactory.not(ValueExpressionFactory.constant("HELLO"));
        assertFalse(expression.verify(context).isOk());
    }
}
