package com.viosng.confsql.semantic.model.sql;

import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import org.jetbrains.annotations.NotNull;

public interface SQLSource extends SingleSQLQuery {
    @NotNull
    public String name();

    @NotNull
    @Override
    default Notification verify() {
        return new Notification();
    }

    @NotNull
    @Override
    default Context getContext() {
        return new Context() {
            @Override
            public boolean hasReference(@NotNull String objectReference) {
                return true;
            }

            @Override
            public boolean hasAttribute(@NotNull String objectReference, @NotNull String attribute) {
                return true;
            }
        };
    }
}
