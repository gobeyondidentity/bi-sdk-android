package com.beyondidentity.embedded.sdk.utils;

import androidx.annotation.Nullable;

/**
 * {@link kotlin.Result} doesn't really work in Java. Use {@link JavaUtils#toJavaResult}
 * to convert it to {@link JavaResult} that provides similar features as {@link kotlin.Result}.
 */
public class JavaResult<T> {
    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;

    private final int type;
    private final T data;
    private final Throwable error;

    private JavaResult(int type, @Nullable T d, @Nullable Throwable e) {
        this.type = type;
        this.data = d;
        this.error = e;
    }

    public static <T> JavaResult<T> success(T d) {
        return new JavaResult<>(SUCCESS, d, null);
    }

    public static <T> JavaResult<T> failure(Throwable e) {
        return new JavaResult<>(FAILURE, null, e);
    }

    public int getType() {
        return type;
    }

    public T getData() {
        return data;
    }

    public Throwable getError() {
        return error;
    }
}
