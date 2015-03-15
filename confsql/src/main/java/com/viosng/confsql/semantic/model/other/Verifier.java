package com.viosng.confsql.semantic.model.other;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 13.03.2015
 * Time: 22:10
 */
public class Verifier{

    private static class ObjectStructureNode {

        @NotNull
        private String name;

        @NotNull
        private final Map<String, ObjectStructureNode> children = new HashMap<>();

        public ObjectStructureNode(@NotNull String name) {
            this.name = name;
        }
    }

    @NotNull
    private final List<String> warnings = new ArrayList<>();

    @NotNull
    private ObjectStructureNode root;

    public Verifier(@NotNull String root) {
        this.root = new ObjectStructureNode(root);
    }

    public void addObject(List<String> hierarchy) {
        ObjectStructureNode cur = root;
        for (String s : hierarchy) {
            ObjectStructureNode next = cur.children.get(s);
            if (next == null) {
                next = new ObjectStructureNode(s);
                cur.children.put(s, next);
            }
            cur = next;
        }
    }

    public boolean hasObject(List<String> hierarchy) {
        ObjectStructureNode cur = root;
        for (String s : hierarchy) {
            ObjectStructureNode next = cur.children.get(s);
            if (next == null) {
                return false;
            }
            cur = next;
        }
        return true;
    }

    @NotNull
    public Verifier warning(@Nullable String message) {
        if (message != null) warnings.add(message);
        return this;
    }

    @NotNull
    public Verifier mergeWarnings(@NotNull Verifier verifier) {
        warnings.addAll(verifier.warnings);
        return this;
    }

    public boolean isOk() {
        return warnings.isEmpty();
    }


    @Override
    public String toString() {
        return "Verifier{" +
                "objectStructure=" + root +
                ", warnings=" + warnings +
                '}';
    }
}
