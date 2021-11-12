package com.Exceptions;

public abstract class ArtException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }
}
