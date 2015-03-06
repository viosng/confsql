package com.viosng.confsql.semantic.model.algebraold;

import com.viosng.confsql.semantic.model.algebraold.expressions.ArithmeticExpression;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebraold.expressions.PredicateExpression;
import com.viosng.confsql.semantic.model.algebraold.expressions.binary.BinaryArithmeticExpressionFactory;
import com.viosng.confsql.semantic.model.algebraold.expressions.binary.BinaryPredicateExpressionFactory;
import com.viosng.confsql.semantic.model.algebraold.expressions.other.ValueExpressionFactory;
import com.viosng.confsql.semantic.model.algebraold.expressions.unary.UnaryArithmeticExpressionFactory;
import com.viosng.confsql.semantic.model.algebraold.expressions.unary.UnaryPredicateExpressionFactory;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
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
                {BinaryArithmeticExpressionFactory.plus(arithmeticMock(), arithmeticMock()), "+", ArithmeticType.PLUS},
                {BinaryArithmeticExpressionFactory.minus(arithmeticMock(), arithmeticMock()), "-", ArithmeticType.MINUS},
                {BinaryArithmeticExpressionFactory.multiplication(arithmeticMock(), arithmeticMock()), "*", ArithmeticType.MULTIPLY},
                {BinaryArithmeticExpressionFactory.division(arithmeticMock(), arithmeticMock()), "/", ArithmeticType.DIVIDE},
                {BinaryArithmeticExpressionFactory.power(arithmeticMock(), arithmeticMock()), "^", ArithmeticType.POWER},
                
                {BinaryPredicateExpressionFactory.greater(arithmeticMock(), arithmeticMock()), ">", ArithmeticType.GT},
                {BinaryPredicateExpressionFactory.greaterOrEqual(arithmeticMock(), arithmeticMock()), ">=", ArithmeticType.GE},
                {BinaryPredicateExpressionFactory.less(arithmeticMock(), arithmeticMock()), "<", ArithmeticType.LT},
                {BinaryPredicateExpressionFactory.lessOrEqual(arithmeticMock(), arithmeticMock()), "<=", ArithmeticType.LE},
                {BinaryPredicateExpressionFactory.equal(arithmeticMock(), arithmeticMock()), "==", ArithmeticType.EQUAL},
                
                {BinaryPredicateExpressionFactory.or(predicateMock(), predicateMock()), "or", ArithmeticType.OR},
                {BinaryPredicateExpressionFactory.and(predicateMock(), predicateMock()), "and", ArithmeticType.AND},
                
                {UnaryArithmeticExpressionFactory.minus(arithmeticMock()), "-", ArithmeticType.MINUS},

                {UnaryPredicateExpressionFactory.not(predicateMock()), "not", ArithmeticType.NOT},
                
                {ValueExpressionFactory.constant("constant"), "constant", ArithmeticType.CONSTANT},
                {ValueExpressionFactory.functionCall("function", Collections.emptyList()), "function", ArithmeticType.FUNCTION_CALL},
                {ValueExpressionFactory.attribute("object", "attribute"), "object.attribute", ArithmeticType.ATTRIBUTE},
                {ValueExpressionFactory.group("object", "group", Collections.emptyList()), "object.group", ArithmeticType.GROUP},
        };
    }

    @Theory
    public void testInterface(final Object... testData) {
        Expression expression = (Expression)testData[0];
        assertEquals(expression.type(), testData[2]);
    }

    @DataPoints
    public static Object[][] data() {
        return testData;
    }


}
