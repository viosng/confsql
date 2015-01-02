package com.viosng.confsql.semantic.model.expressions.other;

import com.viosng.confsql.semantic.model.expressions.ArithmeticExpression;
import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.expressions.PredicateExpression;
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
    default Expression getExpression(Type type) {
        return type == type() ? this : null;
    }
    
    public interface ConstantExpression extends ValueExpression, ArithmeticExpression, PredicateExpression {
        @NotNull
        @Override
        public default Type type() { return Type.CONSTANT; }
    }
    
    public interface FunctionCallExpression extends ValueExpression, ArithmeticExpression, PredicateExpression {

        @NotNull
        @Override
        public default Type type() { return Type.FUNCTION_CALL; }
        
        @NotNull
        public List<Expression> getArguments();
        
    }
    
    public interface AttributeExpression extends ValueExpression, ArithmeticExpression, PredicateExpression {
        
        @NotNull
        public String getObjectReference();

        @NotNull
        @Override
        public default Type type() { return Type.ATTRIBUTE; }

    }
    
    public interface GroupExpression extends AttributeExpression {
        
        @NotNull
        @Override
        public default Type type() { return Type.GROUP; }

        @NotNull
        public List<Expression> getGroupedAttributes();

    }
}
