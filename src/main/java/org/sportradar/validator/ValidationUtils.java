package org.sportradar.validator;

public class ValidationUtils {

    private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "Argument cannot be null";

    /**
     * Ensures that none of the provided objects are null.
     *
     * @param objects An array of objects to be validated for non-nullity.
     * @throws IllegalArgumentException if any object in the {@code objects} array is {@code null}.
     *                                  The exception message is a standard message indicating that
     *                                  null values are not allowed.
     */
    public static void requireNonNull(Object... objects) throws IllegalArgumentException {
        for (Object obj : objects) {
            if (obj == null) {
                throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
            }
        }
    }

}
