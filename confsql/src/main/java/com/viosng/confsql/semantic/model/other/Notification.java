package com.viosng.confsql.semantic.model.other;

import com.google.common.base.Joiner;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 11:29
 */
public class Notification {
    @NotNull
    private List<String> errors = new LinkedList<>();

    public Notification() {
    }
    
    public void error(@NotNull String message) {
        errors.add(message);
    }
    
    public void addNotification(@NotNull Notification notification) {
        errors.addAll(notification.errors);
    }
    
    public boolean isOk() { return errors.isEmpty(); };

    @Override
    public String toString() {
        return isOk() ? "No errors are found" : "Errors : {" + Joiner.on(";\n").join(errors) + "}";
    }
}
