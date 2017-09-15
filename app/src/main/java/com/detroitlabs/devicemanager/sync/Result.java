package com.detroitlabs.devicemanager.sync;

// TODO: 8/29/17 replace all the AsyncTask and AsyncTaskSequence which has boolean response to this type
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
