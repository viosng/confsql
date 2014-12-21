package com.viosng.confsql.semantic.model.schema;

import com.viosng.confsql.semantic.model.ModelElement;
import com.viosng.confsql.semantic.model.expressions.Expression;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 2:43
 */
public interface Schema extends ModelElement{
    public List<Expression> getAttributes();
}
