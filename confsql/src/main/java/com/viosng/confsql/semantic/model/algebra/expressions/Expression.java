package com.viosng.confsql.semantic.model.algebra.expressions;

import com.viosng.confsql.semantic.model.ModelElement;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 2:12
 */
public interface Expression extends ModelElement {

    @NotNull
    public ArithmeticType type();

    /**
     * @param arithmeticType the expression type to find.
     * @return the instance with specific {@code type} expression sub tree or {@code null} if it won't be found.
     */
    public Expression findExpressionByType(ArithmeticType arithmeticType);

    @NotNull
    public Notification verify(@NotNull Context context);
}