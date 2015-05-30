package com.viosng.confsql.semantic.model.other;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 18.03.2015
 * Time: 0:10
 */
public class SuperContext implements Context {

    @NotNull
    private final List<Context> contexts;

    public SuperContext(@NotNull List<Context> contexts) {
        this.contexts = contexts;
    }

    @Override
    public boolean hasObject(@NotNull List<String> hierarchy) {
        return contexts.parallelStream().filter(c -> c.hasObject(hierarchy)).findAny().isPresent();
    }

    public void pause (Context c) {
        System.out.println(111);
    }
}