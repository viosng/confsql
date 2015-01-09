package com.viosng.confsql.semantic.model.other;

import com.google.common.base.Joiner;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 11:29
 */
public class Notification implements Consumer<Notification>{
    @NotNull
    private final List<String> errors = new ArrayList<>();

    public Notification() {
    }

    public Notification addNotification(@NotNull Notification notification) {
        errors.addAll(notification.errors);
        return this;
    }
    
    public void error(@NotNull String message) {
        errors.add(message);
    }

    public boolean isOk() { return errors.isEmpty(); }

    @Override
    public String toString() {
        return isOk() ? "No errors were found" : "Errors : {" + Joiner.on(";\n").join(errors) + "}";
    }

    @Override
    public void accept(Notification notification) {
        addNotification(notification);
    }

    @NotNull
    public List<String> getErrors() {
        return errors;
    }
}
