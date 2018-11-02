package ch.hsr.apparch.recipe.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Base class of all web service related exceptions.
 * <p>
 * The _WebServiceException_ should be an _unchecked exception_ to write nicer request handlers.
 * Therefore, make sure to write easily seeable throws statements.
 * <p>
 * This decision will reduce the validation skills of the compiler, but saves us from having controller methods with
 * a lot of different exception declarations.
 */
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "General Error")
public class WebServiceException extends RuntimeException {

    private static final ErrorHttpStatus DEFAULT_STATUS = new ErrorHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);

    @Getter
    private final ErrorHttpStatus errorStatus;

    WebServiceException(String message, ErrorHttpStatus errorStatus) {
        super(message);
        this.errorStatus = errorStatus;
    }
}
