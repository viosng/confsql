package com.viosng.confsql.semantic.model.other;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.viosng.confsql.semantic.model.algebra.Expression;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 29.12.2014
 * Time: 3:38
 */
public class QueryContext extends Notification implements Context {

    private final static Set<String> RESERVED_FIELDS = ImmutableSet.of("score", "SCORE", "ID", "id");

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
    private final ObjectStructureNode root;

    public QueryContext(@NotNull String root) {
        super();
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

    @Override
    public boolean hasObject(@NotNull List<String> hierarchy) {
        ObjectStructureNode cur = root;
        if (hierarchy.get(0).equals(root.name)) {
            hierarchy = hierarchy.subList(1, hierarchy.size());
        }
        for (String s : hierarchy) {
            ObjectStructureNode next = cur.children.get(s);
            if (next == null)
                return RESERVED_FIELDS.contains(s) || cur.children.size() == 0;
            cur = next;
        }
        return true;
    }

    public void mergeContext(@NotNull QueryContext context, @NotNull List<String> contextPath, @NotNull List<String> newPath) {
        ObjectStructureNode cur = context.root;
        if (contextPath.get(0).equals(context.root.name)) {
            contextPath = contextPath.subList(1, contextPath.size());
        }
        for (String s : contextPath) {
            ObjectStructureNode next = cur.children.get(s);
            if (next == null) {
                if (RESERVED_FIELDS.contains(s) || cur.children.size() == 0) addObject(newPath);
                return;
            }
            cur = next;
        }
        cur.name = newPath.get(newPath.size() - 1);
        addObject(newPath, cur);
    }

    public void mergeContextsByLevel(Iterable<QueryContext> contexts) {
        for (QueryContext context : contexts) {
            mergeNodes(root, context.root);
        }
    }

    @NotNull
    public static QueryContext joinContexts(Iterable<QueryContext> contexts) {
        QueryContext newContext = new QueryContext("");
        int count = 0;
        for (QueryContext context : contexts) {
            if (context.root.name.equals(Expression.UNDEFINED_ID)) {
                count += context.root.children.size();
                newContext.root.children.putAll(context.root.children);
            } else {
                count++;
                newContext.root.children.put(context.root.name, context.root);
            }
        }
        if (count != newContext.root.children.size()) {
            newContext.addWarning("There are duplicate ids in join clause");
        }
        return newContext;
    }

    public void unNestObject(@NotNull List<String> hierarchy) {
        ObjectStructureNode cur = root, prev = root;
        if (hierarchy.get(0).equals(root.name)) {
            hierarchy = hierarchy.subList(1, hierarchy.size());
        }
        for (String s : hierarchy) {
            ObjectStructureNode next = cur.children.get(s);
            if (next == null) {
                // if children size == 0 then it means that we don't know anything about nested hierarchy,
                // than we can't extend it by unnest and should leave empty
                if (cur.children.size() != 0) {
                    addWarning("There is no such attribute (" + Joiner.on('.').join(hierarchy) + ")");
                }
                return;
            }
            prev = cur;
            cur = next;
        }
        if (prev == cur) {
            throw new IllegalArgumentException();
        }
        if (cur.children.isEmpty()) {
            prev.children.clear();
        } else {
            int count = prev.children.size() - 1 + cur.children.size();
            prev.children.remove(cur.name);
            prev.children.putAll(cur.children);
            if (count != prev.children.size()) {
                addWarning("There are duplicate attribute ids in hierarchy");
            }
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

    public void clear() {
        root.children.clear();
        warnings.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QueryContext)) return false;

        QueryContext context = (QueryContext) o;

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
