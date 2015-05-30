package com.viosng.confsql.semantic.model.algebra.special.expr;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 24.12.2014
 * Time: 19:07
 */
public interface ValueExpression extends Expression {
    
    @NotNull
    String getValue();
    
    interface ConstantExpression extends ValueExpression{
        @NotNull
        @Override
        default ArithmeticType type() { return ArithmeticType.CONSTANT; }
    }
    
    interface FunctionCallExpression extends ValueExpression{

        @NotNull
        @Override
        default ArithmeticType type() { return ArithmeticType.FUNCTION_CALL; }

        @NotNull
        List<Parameter> getParameters();
        
    }
    
    interface AttributeExpression extends ValueExpression{
        
        @NotNull
        List<String> getObject();

        @NotNull
        @Override
        default ArithmeticType type() { return ArithmeticType.ATTRIBUTE; }
    }
}
