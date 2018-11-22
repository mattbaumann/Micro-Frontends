package ch.hsr.apparch.recipe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

/**
 * Exception thrown at duplicate insert.
 * <p>
 * When an insert was duplicate, the exception terminates the processing early.
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Resource was a duplicate insert.")
public final class DuplicateResourceException extends WebServiceException {

    private static final long serialVersionUID = 470051316962575212L;

    private static final String DUPLICATE_RECORD_MESSAGE = "''{0}'' with Name ''{1}'' was a duplicate insert.";

    private static final ErrorHttpStatus STATUS = new ErrorHttpStatus(HttpStatus.CONFLICT);

    private DuplicateResourceException(String message) {
        super(message, STATUS);
    }

    private static DuplicateResourceException format(Object... params) {
        return new DuplicateResourceException(MessageFormat.format(DuplicateResourceException.DUPLICATE_RECORD_MESSAGE, params));
    }

    public static DuplicateResourceException withDuplicateMessage(Class<?> class_name, String id) {
        return format(class_name.getName(), id);
    }
}
