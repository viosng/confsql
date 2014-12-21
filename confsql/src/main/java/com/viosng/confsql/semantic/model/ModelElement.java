package com.viosng.confsql.semantic.model;

import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 11:40
 */
public interface ModelElement {

    @NotNull
    public Notification verify(@NotNull Context context);
}
