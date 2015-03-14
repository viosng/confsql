package com.viosng.confsql.semantic.model.other;

import org.antlr.v4.runtime.misc.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 13.03.2015
 * Time: 22:10
 */
public class Verifier implements Consumer<Verifier> {

    @NotNull
    private final Map<String, Set<String>> context = new HashMap<>();

    @NotNull
    private final List<String> warnings = new ArrayList<>();

    @NotNull
    public Verifier warning(@Nullable String message) {
        warnings.add(message);
        return this;
    }

    @NotNull
    public Verifier attribute(@NotNull String object, @NotNull String attribute) {
        Set<String> attributes = context.get(object);
        if (attributes == null) {
            attributes = new HashSet<>();
            context.put(object, attributes);
        }
        attributes.add(attribute);
        return this;
    }

    @NotNull
    public Verifier clearContext() {
        context.clear();
        return this;
    }

    public boolean hasReference(@NotNull String objectReference){
        return context.containsKey(objectReference);
    }

    public boolean hasAttribute(@NotNull String objectReference, @NotNull String attribute){
        Set<String> attributes;
        return (attributes = context.get(objectReference)) != null && attributes.contains(attribute);
    }

    @NotNull
    public Verifier mergeWarnings(@NotNull Verifier verifier) {
        warnings.addAll(verifier.warnings);
        return this;
    }

    @NotNull
    public Verifier mergeContext(@NotNull Verifier verifier) {
        context.putAll(verifier.context);
        return this;
    }

    @NotNull
    public Set<String> getAttributes(@NotNull String object) {
        return hasReference(object) ? context.get(object) : Collections.<String>emptySet();
    }

    @NotNull
    public Set<Pair<String, String>> getAllAttributes() {
        return context.entrySet().stream().flatMap(
                entry -> entry.getValue().stream().map(
                        attribute -> new Pair<>(entry.getKey(), attribute))).collect(Collectors.toSet());
    }




    @Override
    public void accept(Verifier verifier) {
        if (verifier != null) {
            warnings.addAll(verifier.warnings);
            context.putAll(verifier.context);
        }
    }
}
