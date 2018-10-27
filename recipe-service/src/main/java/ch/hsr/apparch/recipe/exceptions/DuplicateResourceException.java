package ch.hsr.apparch.recipe.exceptions;

import java.text.MessageFormat;

public final class DuplicateResourceException extends RuntimeException {

    private static final String DUPLICATE_RECORD_MESSAGE = "''{0}'' with Name ''{1}'' was a duplicate insert.";

    public DuplicateResourceException() {
        super();
    }

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateResourceException(Throwable cause) {
        super(cause);
    }

    protected DuplicateResourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    private static DuplicateResourceException format(String message, Object... params) {
        return new DuplicateResourceException(MessageFormat.format(message, params));
    }

    public static DuplicateResourceException withDuplicateMessage(Class<?> class_name, String id) {
        return format(DUPLICATE_RECORD_MESSAGE, class_name.getName(), id);
    }
}
