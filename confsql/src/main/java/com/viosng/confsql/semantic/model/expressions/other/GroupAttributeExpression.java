package com.viosng.confsql.semantic.model.expressions.other;

import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 15:44
 */
public class GroupAttributeExpression extends AbstractAttributeExpression{
    public GroupAttributeExpression(@NotNull String objectReference, @NotNull String value) {
        super(objectReference, value);
    }

    @NotNull
    @Override
    public Type type() {
        return Type.GROUP;
    }
}
