package com.nesh.mors.exception;

public class NeshException extends RuntimeException {
    /**
     * Example usage:
     * new Mp3JuicesException("Failed to load response from {}, args was: ", url, arg1, arg2")
     * @param pattern
     * @param args
     */
    public NeshException(String pattern, Object ... args) {
        super(concatStrings(pattern, args));
    }

    public NeshException(Throwable cause, String pattern, Object ... args) {
        super(concatStrings(pattern, args), cause);
    }


    protected static String concatStrings(String pattern, Object ... args) {
        int i = 0;
        for (; pattern.contains("{}") ; i++) {
            pattern = pattern.replaceFirst("\\{}", escapeNullAndWrapQuote(args[i]));
        }

        // simply append other values comma-separated
        for (; i < args.length; i++) {
            if (i < args.length - 1) {
                pattern = pattern + escapeNullAndWrapQuote(args[i]) + ", ";
            } else {
                pattern = pattern + escapeNullAndWrapQuote(args[i]);
            }
        }
        return pattern;
    }

    protected static String escapeNullAndWrapQuote(Object value) {
        return value == null ? "'null'" : '\'' + value.toString() + '\'';
    }
}
