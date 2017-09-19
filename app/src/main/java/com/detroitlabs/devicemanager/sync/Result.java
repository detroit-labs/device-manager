package com.detroitlabs.devicemanager.sync;

public class Result {
    public final Exception exception;

    protected Result(Exception exception) {
        this.exception = exception;
    }

    public static Result success() {
        return new Result(null);
    }

    public static Result failure(Exception exception) {
        return new Result(exception);
    }

    public boolean isSuccess() {
        return exception == null;
    }
}
