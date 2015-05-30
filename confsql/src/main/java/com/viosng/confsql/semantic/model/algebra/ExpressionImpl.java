package com.viosng.confsql.semantic.model.algebra;

import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import com.viosng.confsql.semantic.model.other.NotificationCollector;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 06.03.2015
 * Time: 18:16
 */
@Data
public class ExpressionImpl implements Expression {

    @NotNull
    private String id = UNDEFINED_ID;

    @NotNull
    private final ArithmeticType type;

    @NotNull
    private final List<Expression> arguments;

    public ExpressionImpl(@NotNull ArithmeticType type, Expression... arguments) {
        this.type = type;
        this.arguments = Arrays.asList(arguments);
    }

    public void setId(@Nullable String id) {
        this.id = id == null ? this.id : id;
    }

    @NotNull
    @Override
    public ArithmeticType type() {
        return type;
    }

    @NotNull
    @Override
    public Notification verify(Context context) {
        return arguments.stream()
                .filter(a -> a != null)
                .map(p -> p.verify(context))
                .collect(new NotificationCollector());
    }

    @NotNull
    @Override
    public String id() {
        return id;
    }

}
