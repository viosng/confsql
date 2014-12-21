package com.viosng.confsql.semantic.model.other;

import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 15:08
 */
public interface Context {
    public boolean hasReference(@NotNull String objectReference);
    public boolean hasAttribute(@NotNull String objectReference, @NotNull String attribute);
}
