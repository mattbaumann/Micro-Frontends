package ch.hsr.apparch.recipe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

/**
 * Not implemented controller function
 * <p>
 * Included for testing controller paths without the need to implement it before.
 */
@ResponseStatus(code = HttpStatus.NOT_IMPLEMENTED, reason = "Endpoint is not implemented")
public class NotImplementedException extends WebServiceException {

    private static final String CATEGORY_NOT_EMPTY_MESSAGE = "Path ''{0}'' is not defined for method ''{1}''";

    private static final ErrorHttpStatus STATUS = new ErrorHttpStatus(HttpStatus.NOT_IMPLEMENTED);

    private NotImplementedException(String message) {
        super(message, STATUS);
    }

    private static NotImplementedException format(Object... params) {
        return new NotImplementedException(MessageFormat.format(CATEGORY_NOT_EMPTY_MESSAGE, params));
    }

    public static NotImplementedException withRequest(HttpServletRequest request) {
        return format(request.getServletPath(), request.getMethod());
    }
}
