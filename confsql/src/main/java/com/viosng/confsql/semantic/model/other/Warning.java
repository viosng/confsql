package com.viosng.confsql.semantic.model.other;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 17.03.2015
 * Time: 23:32
 */
public class Warning {
    private static final AtomicLong COUNTER = new AtomicLong();

    public static void flushCounter() {
        COUNTER.set(0);
    }

    private final long id;

    @NotNull
    private final String message;

    public Warning(@NotNull String message) {
        this.message = message;
        id = COUNTER.getAndIncrement();
    }

    @NotNull
    public String getMessage() {
        return message;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Warning)) return false;

        Warning warning = (Warning) o;

        return id == warning.id && message.equals(warning.message);
    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Warning{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
