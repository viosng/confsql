package com.viosng.confsql.semantic.model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 2:08
 */
public class Parameter {
    protected Logger log = LoggerFactory.getLogger(getClass());
    
    @NotNull
    private String name, value;

    public Parameter(@NotNull String name, @NotNull String value) {
        if (name.length() == 0) throw new IllegalArgumentException("Empty parameter name");
        log.info("New parameter is created : name = {}, value = {}", name, value);
        this.name = name;
        this.value = value;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getValue() {
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
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
