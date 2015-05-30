package com.viosng.confsql.semantic.model.other;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 17.03.2015
 * Time: 23:39
 */
public class Notification{

    @NotNull
    protected final List<Warning> warnings = new ArrayList<>();

    @NotNull
    public Notification addWarning(@NotNull String message) {
        warnings.add(new Warning(message));
        return this;
    }

    @NotNull
    public Notification addWarnings(@NotNull Notification notification) {
        warnings.addAll(notification.getWarnings());
        return this;
    }

    public boolean isOk() {
        return warnings.isEmpty();
    }

    @NotNull
    public List<Warning> getWarnings() {
        return warnings;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "warnings=" + warnings +
                '}';
    }
}
