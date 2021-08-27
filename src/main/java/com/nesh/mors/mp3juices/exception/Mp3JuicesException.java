package com.nesh.mors.mp3juices.exception;

import com.nesh.mors.exception.MorsException;

public class Mp3JuicesException extends MorsException {
    public static void main(String[] args) {
        System.out.println(new Mp3JuicesException("Failed to load response from {}, args was: ", "http://url", "value1", 2, null, false));
    }
    /**
     * Example usage:
     * new Mp3JuicesException("Failed to load response from {}, args was: ", url, arg1, arg2")
     * @param pattern
     * @param args
     */
    public Mp3JuicesException(String pattern, Object ... args) {
        super(pattern, args);
    }

    public Mp3JuicesException(Throwable cause, String pattern, Object ... args) {
        super(cause, pattern, args);
    }
}
