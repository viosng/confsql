package com.viosng.confsql.semantic.model.schema.impl;

import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import com.viosng.confsql.semantic.model.schema.Schema;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 2:44
 */
public class SchemaImpl implements Schema {
    @Override
    public List<Expression> getAttributes() {
        return null;
    }

    @NotNull
    @Override
    public Notification verify(@NotNull Context context) {
        return null; // todo
    }
}
