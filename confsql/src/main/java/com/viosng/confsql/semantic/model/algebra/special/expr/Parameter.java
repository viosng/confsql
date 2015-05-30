package com.viosng.confsql.semantic.model.algebra.special.expr;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import lombok.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 2:08
 */

@Data
@ToString(exclude="verificationFlag")
@EqualsAndHashCode(exclude={"verificationFlag"})
public class Parameter implements Expression {
    
    @NotNull
    private final String id;
    
    @NotNull
    private final Expression value;

    @Setter(AccessLevel.PRIVATE)
    private volatile boolean verificationFlag = true;

    public Parameter(@NotNull String id, @NotNull Expression value) {
        if (id.length() == 0) throw new IllegalArgumentException("Empty parameter sourceName");
        this.id = id;
        this.value = value;
    }

    @NotNull
    @Override
    public String id() {
        return id;
    }

    @NotNull
    @Override
    public ArithmeticType type() {
        return ArithmeticType.PARAMETER;
    }

    @NotNull
    @Override
    public Notification verify(Context context) {
        return verificationFlag ? value.verify(context) : new Notification();
    }

    public void disableVerification(){
        verificationFlag = false;
    }
}
