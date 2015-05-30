package com.viosng.confsql.semantic.model.other;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 17.03.2015
 * Time: 23:26
 */
public interface Context {
    boolean hasObject(@NotNull List<String> hierarchy);

    Context EMPTY_CONTEXT = hierarchy -> false;
}
