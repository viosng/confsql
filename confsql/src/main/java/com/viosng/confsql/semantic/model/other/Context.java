package com.viosng.confsql.semantic.model.other;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 29.12.2014
 * Time: 3:38
 */
public class Context {

    private static class ObjectStructureNode {

        @NotNull
        private String name;

        @NotNull
        private final Map<String, ObjectStructureNode> children = new HashMap<>();

        public ObjectStructureNode(@NotNull String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "ObjectStructureNode{" +
                    "name='" + name + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    @NotNull
    private final List<String> warnings = new ArrayList<>();

    @NotNull
    private ObjectStructureNode root;

    public Context(@NotNull String root) {
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
            if (next == null)
                return cur.children.size() == 0;
            cur = next;
        }
        return true;
    }

    @NotNull
    public Context warning(@Nullable String message) {
        if (message != null) warnings.add(message);
        return this;
    }

    @NotNull
    public Context mergeWarnings(@NotNull Context context) {
        warnings.addAll(context.warnings);
        return this;
    }

    public void mergeContext(@NotNull Context context, @NotNull List<String> path, @NotNull String id) {
        ObjectStructureNode cur = context.root;
        for (String s : path) {
            ObjectStructureNode next = cur.children.get(s);
            if (next == null)
                if (cur.children.size() == 0) {
                    if (root.children.containsKey(id)) {
                        throw new IllegalArgumentException("Duplicate node id");
                    }
                    addObject(Arrays.asList(root.name, id));
                    return;
                } else {
                    throw new IllegalArgumentException();
                }
            cur = next;
        }
        if (root.children.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate node id");
        }
        root.children.put(id, cur);
    }

    public boolean isOk() {
        return warnings.isEmpty();
    }

    @Override
    public String toString() {
        return "Context{" +
                "warnings=" + warnings +
                ", root=" + root +
                '}';
    }
}
