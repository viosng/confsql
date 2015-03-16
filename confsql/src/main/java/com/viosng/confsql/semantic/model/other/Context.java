package com.viosng.confsql.semantic.model.other;

import com.google.common.base.Joiner;
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
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ObjectStructureNode)) return false;

            ObjectStructureNode that = (ObjectStructureNode) o;

            return children.equals(that.children) && name.equals(that.name);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + children.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "ObjectStructureNode{" +
                    "\nname='" + name + '\'' +
                    ", \nchildren=" + Joiner.on(',').withKeyValueSeparator("=").join(children) +
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
        addObject(hierarchy, null);
    }

    private void addObject(List<String> hierarchy, ObjectStructureNode newNode) {
        ObjectStructureNode cur = root, prev = root;
        if (hierarchy.get(0).equals(root.name)) {
            hierarchy = hierarchy.subList(1, hierarchy.size());
        }
        for (String s : hierarchy) {
            ObjectStructureNode next = cur.children.get(s);
            if (next == null) {
                next = new ObjectStructureNode(s);
                cur.children.put(s, next);
            }
            prev = cur;
            cur = next;
        }
        if (newNode != null) {
            prev.children.put(hierarchy.get(hierarchy.size() - 1), newNode);
        }
    }

    public boolean hasObject(List<String> hierarchy) {
        ObjectStructureNode cur = root;
        if (hierarchy.get(0).equals(root.name)) {
            hierarchy = hierarchy.subList(1, hierarchy.size());
        }
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

    public void mergeContext(@NotNull Context context, @NotNull List<String> contextPath, @NotNull List<String> newPath) {
        ObjectStructureNode cur = context.root;
        if (contextPath.get(0).equals(context.root.name)) {
            contextPath = contextPath.subList(1, contextPath.size());
        }
        for (String s : contextPath) {
            ObjectStructureNode next = cur.children.get(s);
            if (next == null) {
                if (cur.children.size() == 0) addObject(newPath);
                return;
            }
            cur = next;
        }
        cur.name = newPath.get(newPath.size() - 1);
        addObject(newPath, cur);
    }

    public void mergeContextsByLevel(Iterable<Context> contexts) {
        for (Context context : contexts) {
            mergeNodes(root, context.root);
        }
    }

    private void mergeNodes(ObjectStructureNode mergeTo, ObjectStructureNode mergeFrom) {
        for (Map.Entry<String, ObjectStructureNode> entry : mergeFrom.children.entrySet()) {
            ObjectStructureNode node = mergeTo.children.get(entry.getKey());
            if (node != null) {
                mergeNodes(node, entry.getValue());
            } else {
                mergeTo.children.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public boolean isOk() {
        return warnings.isEmpty();
    }

    public void clear() {
        root.children.clear();
        warnings.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Context)) return false;

        Context context = (Context) o;

        return root.equals(context.root) && warnings.equals(context.warnings);
    }

    @Override
    public int hashCode() {
        int result = warnings.hashCode();
        result = 31 * result + root.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Context{" +
                "warnings=" + warnings +
                ", root=" + root +
                '}';
    }
}
