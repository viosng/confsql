package com.viosng.confsql.semantic.model.expressions.other;

import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 15:33
 */
public abstract class AbstractAttributeExpression extends AbstractValueExpression {

    @NotNull
    private String objectReference;

    protected AbstractAttributeExpression(@NotNull String objectReference, @NotNull String value) {
        super(value);
        this.objectReference = objectReference;
    }

    @NotNull
    @Override
    public String getName() {
        return objectReference + "." + super.getName();
    }

    @NotNull
    @Override
    public Notification verify(@NotNull Context context) {
        Notification notification = new Notification();
        if (!context.hasReference(objectReference)) {
            notification.error("Context doesn't know about \"" + objectReference + "\" reference");
        }
        else if (!context.hasAttribute(objectReference, value)) {
            notification.error("Object reference \"" + objectReference + "\" hasn't attribute \"" + value + "\"");
        }
        return notification;
    }

    @Override
    public String toString() {
        return getName();
    }
}
