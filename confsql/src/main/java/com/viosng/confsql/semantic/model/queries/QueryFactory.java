package com.viosng.confsql.semantic.model.queries;

import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 29.12.2014
 * Time: 2:41
 */
public class QueryFactory {

    private QueryFactory() {
    }

    public static Query filter(@NotNull Query base, 
                               @NotNull List<Expression> argumentExpressions, 
                               @NotNull List<Parameter> parameters, 
                               @NotNull List<Expression> schemaAttributes) {
        return new DefaultQuery(parameters, schemaAttributes, Arrays.asList(base), argumentExpressions) {
            @NotNull
            @Override
            protected Context createContext() {
                return new Context() {
                    @Override
                    public boolean hasReference(@NotNull String objectReference) {
                        return false;
                    }

                    @Override
                    public boolean hasAttribute(@NotNull String objectReference, @NotNull String attribute) {
                        return false;
                    }
                };
            }
        }

    }
}
