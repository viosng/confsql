package com.viosng.confsql.semantic.model.other;

import com.viosng.confsql.semantic.model.expressions.Expression;
import com.viosng.confsql.semantic.model.queries.Query;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 29.12.2014
 * Time: 3:38
 */
public class DefaultContext implements Context {
    
    private Map<String, Set<String>> objectAttributes;

    public DefaultContext(@NotNull List<Query> subQueries) {
        if (!subQueries.isEmpty()) {
            objectAttributes = new HashMap<>();
            for (Query subQuery : subQueries) {
                Set<String> attributes = new HashSet<>(subQuery.getSchemaAttributes().size());
                attributes.addAll(subQuery.getSchemaAttributes().stream().map(Expression::id).collect(Collectors.toList()));
                attributes.add("score"); // object score is always available
                objectAttributes.put(subQuery.id(), attributes);
            }
        }
    }

    @Override
    public boolean hasReference(@NotNull String objectReference) {
        return objectAttributes != null && objectAttributes.containsKey(objectReference);
    }

    @Override
    public boolean hasAttribute(@NotNull String objectReference, @NotNull String attribute) {
        if (objectAttributes == null) return true;
        Set<String> object = objectAttributes.get(objectReference);
        return object != null && (object.size() == 1 || object.contains(attribute));
    }
}
