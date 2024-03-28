package org.sportradar.validator;

public class ValidationUtils {

    private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "Argument cannot be null";

    public static void requireNonNull(Object... objects) throws IllegalArgumentException {
        for (Object obj : objects) {
            if (obj == null) {
                throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
            }
        }
    }

}
