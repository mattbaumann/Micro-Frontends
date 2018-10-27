package ch.hsr.apparch.recipe.exceptions;

import java.text.MessageFormat;
import java.util.function.Supplier;

public final class ResourceNotFoundException extends RuntimeException {


    private static final String RECORD_NOT_FOUND = "''{0}'' with id ''{1}'' not found";

    private static final int POSITION = 0;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    private static ResourceNotFoundException format(final String message, final Object... params) {
        return new ResourceNotFoundException(MessageFormat.format(message, params));
    }

    public static Supplier<ResourceNotFoundException> withRecordNotFoundMessage(final String record_type, final long id) {
        return () -> format(RECORD_NOT_FOUND, record_type, id);
    }

    public static Supplier<ResourceNotFoundException> withRecordNotFoundMessage(final Class<?> record_type, final long id) {
        return () -> format(RECORD_NOT_FOUND, record_type.getName(), id);
    }
}
