package com.viosng.confsql.semantic.model.other;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 29.12.2014
 * Time: 3:38
 */
public class DefaultContext implements Context {
    
    private Map<String, Set<String>> objectAttributes;

    public DefaultContext() {}

    @Override
    public boolean hasReference(@NotNull String objectReference) {
        return false;
    }

    @Override
    public boolean hasAttribute(@NotNull String objectReference, @NotNull String attribute) {
        return false;
    }
}
