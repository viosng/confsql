package com.viosng.confsql.semantic.model.other;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Created by vio-note on 24.05.2015.
 * Date: 24.05.2015
 */

@Data
public class CacheableValue<T> {

    @NotNull
    private final AtomicReference<T> value = new AtomicReference<>(null);;

    public T get() {
        return value.get();
    }

    public T get(Supplier<T> supplier) {
        return value.getAndSet(supplier.get());
    }

    public T orElseGet(@NotNull Supplier<T> supplier) {
        T curVal;
        do {
            curVal = value.get();
            if (curVal != null) {
                return curVal;
            }
        } while (!value.compareAndSet(null, curVal = supplier.get()));
        return curVal;
    }

    @Override
    public int hashCode() {
        return value.get().hashCode();
    }
}
