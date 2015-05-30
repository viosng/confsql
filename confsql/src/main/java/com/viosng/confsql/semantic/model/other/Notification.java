package com.viosng.confsql.semantic.model.other;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 17.03.2015
 * Time: 23:39
 */

@Data
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
}
