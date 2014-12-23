package com.viosng.confsql.semantic.model.expressions;

import com.viosng.confsql.semantic.model.expressions.binary.BinaryArithmeticExpressionFactory;
import com.viosng.confsql.semantic.model.expressions.binary.BinaryPredicateExpressionFactory;
import com.viosng.confsql.semantic.model.expressions.other.AttributeExpression;
import com.viosng.confsql.semantic.model.expressions.other.ConstantExpression;
import com.viosng.confsql.semantic.model.expressions.other.FunctionCallExpression;
import com.viosng.confsql.semantic.model.expressions.other.GroupAttributeExpression;
import com.viosng.confsql.semantic.model.expressions.unary.UnaryArithmeticExpressionFactory;
import com.viosng.confsql.semantic.model.expressions.unary.UnaryPredicateExpressionFactory;
import com.viosng.confsql.semantic.model.other.Context;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 23.12.2014
 * Time: 20:07
 */

@RunWith(Theories.class)
public class ExpressionTest {

    public static Object[][] testData;

    private static ArithmeticExpression arithmeticMock() {
        return mock(ArithmeticExpression.class);
    }

    private static PredicateExpression predicateMock() {
        return mock(PredicateExpression.class);
    }

    @BeforeClass
    public static void generateData(){
        testData = new Object[][] {
                {BinaryArithmeticExpressionFactory.plus(arithmeticMock(), arithmeticMock()), "+", Expression.Type.PLUS},
                {BinaryArithmeticExpressionFactory.minus(arithmeticMock(), arithmeticMock()), "-", Expression.Type.MINUS},
                {BinaryArithmeticExpressionFactory.multiplication(arithmeticMock(), arithmeticMock()), "*", Expression.Type.MULTIPLICATION},
                {BinaryArithmeticExpressionFactory.division(arithmeticMock(), arithmeticMock()), "/", Expression.Type.DIVISION},
                {BinaryArithmeticExpressionFactory.power(arithmeticMock(), arithmeticMock()), "^", Expression.Type.POWER},
                
                {BinaryPredicateExpressionFactory.greater(arithmeticMock(), arithmeticMock()), ">", Expression.Type.GREATER},
                {BinaryPredicateExpressionFactory.greaterOrEqual(arithmeticMock(), arithmeticMock()), ">=", Expression.Type.GREATER_OR_EQUAL},
                {BinaryPredicateExpressionFactory.less(arithmeticMock(), arithmeticMock()), "<", Expression.Type.LESS},
                {BinaryPredicateExpressionFactory.lessOrEqual(arithmeticMock(), arithmeticMock()), "<=", Expression.Type.LESS_OR_EQUAL},
                {BinaryPredicateExpressionFactory.equal(arithmeticMock(), arithmeticMock()), "==", Expression.Type.EQUAL},
                
                {BinaryPredicateExpressionFactory.or(predicateMock(), predicateMock()), "or", Expression.Type.OR},
                {BinaryPredicateExpressionFactory.and(predicateMock(), predicateMock()), "and", Expression.Type.AND},
                
                {UnaryArithmeticExpressionFactory.minus(arithmeticMock()), "-", Expression.Type.UNARY_MINUS},

                {UnaryPredicateExpressionFactory.not(predicateMock()), "not", Expression.Type.NOT},
                
                {new ConstantExpression("constant"), "constant", Expression.Type.CONSTANT},
                {new FunctionCallExpression("function"), "function", Expression.Type.FUNCTION_CALL},
                {new AttributeExpression("object", "attribute"), "object.attribute", Expression.Type.ATTRIBUTE},
                {new GroupAttributeExpression("object", "group"), "object.group", Expression.Type.GROUP},
        };
    }

    @Theory
    public void testInterface(final Object... testData) {
        Expression expression = (Expression)testData[0];
        assertEquals(expression.getName(), testData[1]);
        assertEquals(expression.type(), testData[2]);
    }

    @DataPoints
    public static Object[][] data() {
        return testData;
    }

    @Test
    public void testPredicates() throws Exception {
        PredicateExpression predicate = BinaryPredicateExpressionFactory.and(new ConstantExpression("constant"), 
                BinaryPredicateExpressionFactory.or(new ConstantExpression("one"), new ConstantExpression("two")));
        assertFalse(predicate.verify(mock(Context.class)).isOk());
        System.out.println(predicate.verify(mock(Context.class)));
        predicate = BinaryPredicateExpressionFactory.and(new ConstantExpression("True"), new FunctionCallExpression("function"));
        assertTrue(predicate.verify(mock(Context.class)).isOk());
    }
}
