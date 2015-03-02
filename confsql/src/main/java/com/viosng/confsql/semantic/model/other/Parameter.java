package com.viosng.confsql.semantic.model.other;

import com.viosng.confsql.semantic.model.ModelElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 2:08
 */
public class Parameter {
    @NotNull
    private final String name;
    
    @NotNull
    private final ModelElement value;

    public Parameter(@NotNull String name, @NotNull ModelElement value) {
        if (name.length() == 0) throw new IllegalArgumentException("Empty parameter sourceName");
        this.name = name;
        this.value = value;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public ModelElement getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Parameter parameter = (Parameter) o;
        return name.equals(parameter.name) && value.equals(parameter.value);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "sourceName='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
