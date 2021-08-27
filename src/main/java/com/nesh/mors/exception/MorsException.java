package com.nesh.mors.exception;

public class MorsException extends NeshException {

    public MorsException(String pattern, Object... args) {
        super(pattern, args);
    }

    public MorsException(Throwable cause, String pattern, Object... args) {
        super(cause, pattern, args);
    }
}
