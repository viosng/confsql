package com.viosng.confsql.semantic.model.sql;


import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

public interface SQLQuery {
    @NotNull
    public Context getContext();

    @NotNull
    public Notification verify();
}
