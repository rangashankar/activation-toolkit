package com.example.activation.core;

public final class ActivationResult {
    public enum Status {
        SUCCESS,
        FAILED,
        RETRY_LATER
    }

    private final Status status;
    private final String message;

    private ActivationResult(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ActivationResult success(String message) {
        return new ActivationResult(Status.SUCCESS, message);
    }

    public static ActivationResult failed(String message) {
        return new ActivationResult(Status.FAILED, message);
    }

    public static ActivationResult retryLater(String message) {
        return new ActivationResult(Status.RETRY_LATER, message);
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
