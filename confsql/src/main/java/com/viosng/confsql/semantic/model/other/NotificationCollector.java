package com.viosng.confsql.semantic.model.other;

import com.google.common.collect.ImmutableSet;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Created by vio on 24.05.2015.
 * Date: 24.05.2015
 */
public class NotificationCollector implements Collector<Notification, Notification, Notification> {

    @Override
    public Supplier<Notification> supplier() {
        return Notification::new;
    }

    @Override
    public BiConsumer<Notification, Notification> accumulator() {
        return Notification::addWarnings;
    }

    @Override
    public BinaryOperator<Notification> combiner() {
        return (n1, n2) -> {
            Notification n3 = new Notification();
            n3.addWarnings(n1);
            n3.addWarnings(n2);
            return n3;
        };
    }

    @Override
    public Function<Notification, Notification> finisher() {
        return n -> n;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return ImmutableSet.of(Characteristics.UNORDERED, Characteristics.CONCURRENT);
    }
}
