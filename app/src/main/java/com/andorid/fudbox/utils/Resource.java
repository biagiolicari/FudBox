package com.andorid.fudbox.utils;

import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nullable;

public class Resource<T> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable public final String message;

    private Resource(@NonNull Status status, @Nullable T data,
                     @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data, @Nullable String msg) {
        return new Resource<>(Status.SUCCESS, data, msg);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data, String msg) {
        return new Resource<>(Status.LOADING, data, msg);
    }


    public enum Status { SUCCESS, ERROR, LOADING }
}