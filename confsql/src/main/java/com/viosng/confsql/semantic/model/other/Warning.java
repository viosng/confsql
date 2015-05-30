package com.viosng.confsql.semantic.model.other;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 17.03.2015
 * Time: 23:32
 */
@Data
@EqualsAndHashCode(exclude={"id"})
@ToString(exclude = "id")
public class Warning {
    private static final AtomicLong COUNTER = new AtomicLong();

    public static void flushCounter() {
        COUNTER.set(0);
    }

    private final long id = COUNTER.getAndIncrement();

    private final String message;
}
