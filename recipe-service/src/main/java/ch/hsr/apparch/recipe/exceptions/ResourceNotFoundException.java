package ch.hsr.apparch.recipe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;
import java.util.function.Supplier;

/**
 * Database Record not found
 * <p>
 * This exception is thrown when a database record with a specific ID was not found.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Resource is not available.")
public final class ResourceNotFoundException extends WebServiceException {


    private static final String RECORD_NOT_FOUND = "''{0}'' with id ''{1}'' not found";

    private static final ErrorHttpStatus STATUS = new ErrorHttpStatus(HttpStatus.NOT_FOUND);

    private ResourceNotFoundException(String message) {
        super(message, STATUS);
    }

    private static ResourceNotFoundException format(final Object... params) {
        return new ResourceNotFoundException(MessageFormat.format(ResourceNotFoundException.RECORD_NOT_FOUND, params));
    }

    public static Supplier<ResourceNotFoundException> withRecordNotFoundMessage(final String record_type, final long id) {
        return () -> format(record_type, id);
    }

    public static Supplier<ResourceNotFoundException> withRecordNotFoundMessage(final Class<?> record_type, final long id) {
        return () -> format(record_type.getName(), id);
    }
}
