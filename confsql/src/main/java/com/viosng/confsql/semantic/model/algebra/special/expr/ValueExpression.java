package com.viosng.confsql.semantic.model.algebra.special.expr;

import com.viosng.confsql.semantic.model.algebraold.expressions.ArithmeticExpression;
import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebraold.expressions.PredicateExpression;
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
    public String getValue();

    @Override
    default Expression findExpressionByType(ArithmeticType arithmeticType) {
        return arithmeticType == type() ? this : null;
    }
    
    public interface ConstantExpression extends ValueExpression, ArithmeticExpression, PredicateExpression {
        @NotNull
        @Override
        public default ArithmeticType type() { return ArithmeticType.CONSTANT; }
    }
    
    public interface FunctionCallExpression extends ValueExpression, ArithmeticExpression, PredicateExpression {

        @NotNull
        @Override
        public default ArithmeticType type() { return ArithmeticType.FUNCTION_CALL; }
        
        @NotNull
        public List<Expression> getArguments();
        
    }
    
    public interface AttributeExpression extends ValueExpression, ArithmeticExpression, PredicateExpression {
        
        @NotNull
        public String getObjectReference();

        @NotNull
        @Override
        public default ArithmeticType type() { return ArithmeticType.ATTRIBUTE; }

    }
    
    public interface GroupExpression extends AttributeExpression {
        
        @NotNull
        @Override
        public default ArithmeticType type() { return ArithmeticType.GROUP; }

        @NotNull
        public List<Expression> getGroupedAttributes();

    }
}
