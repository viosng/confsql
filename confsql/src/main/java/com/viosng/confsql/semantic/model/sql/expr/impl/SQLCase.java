package com.viosng.confsql.semantic.model.sql.expr.impl;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.special.expr.CaseExpression;
import com.viosng.confsql.semantic.model.algebra.special.expr.Parameter;
import com.viosng.confsql.semantic.model.sql.SQLExpression;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 28.02.2015
 * Time: 13:38
 */

@Data
public class SQLCase implements SQLExpression{

    @Data
    public static class SQLWhenThenClause implements SQLExpression {
        
        @NotNull
        private final SQLExpression when, then;
    }
    
    @Nullable
    private final SQLExpression expression, elseExpression;
    
    @NotNull
    private final List<SQLWhenThenClause> whenThenClauses;

    public SQLCase(@Nullable SQLExpression expression, @NotNull List<SQLWhenThenClause> whenThenClauses, @Nullable SQLExpression elseExpression) {
        this.expression = expression;
        this.elseExpression = elseExpression;
        this.whenThenClauses = whenThenClauses;
    }

    @NotNull
    @Override
    public Expression convert() {
        AtomicInteger index = new AtomicInteger();
        List<Parameter> parameters = whenThenClauses.stream().flatMap(
                w -> Stream.of(
                        new Parameter("whenExpression" + index, w.getWhen().convert()),
                        new Parameter("thenExpression" + index.getAndIncrement(), w.getThen().convert())))
                .collect(Collectors.toList());
        if (elseExpression != null) {
            parameters.add(new Parameter("elseExpression", elseExpression.convert()));
        }
        return new CaseExpression(expression != null ? expression.convert() : null, parameters);
    }

}
