package com.viosng.confsql.semantic.model.queries;

import com.viosng.confsql.semantic.model.ModelElement;
import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 2:04
 */
public interface Query extends ModelElement {
    
    @NotNull
    public List<Parameter> getParameters();

    @NotNull
    public List<Expression> getSchemaAttributes();

    @NotNull
    public List<Query> getSubQueries();

    @NotNull
    public List<Expression> getArgumentExpressions();

    @NotNull
    public Context getContext();
    
    @NotNull
    public Notification verify();
}
