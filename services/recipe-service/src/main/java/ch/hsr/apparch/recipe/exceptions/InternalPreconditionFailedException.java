package ch.hsr.apparch.recipe.exceptions;

import ch.hsr.apparch.recipe.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

/**
 * Internal precondition failed
 * <p>
 * Requests are tested against preconditions, before database queries are executed. When a such precondition fails, the
 * server will fire {@linkplain InternalPreconditionFailedException}.
 */
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public final class InternalPreconditionFailedException extends WebServiceException {

    private static final long serialVersionUID = 6262395849825016266L;

    private static final String CATEGORY_NOT_EMPTY_MESSAGE = "Category ''{0}'' with ID ''{1}'' is not empty";

    private static final ErrorHttpStatus STATUS = new ErrorHttpStatus(520, "Internal precondition failed");

    private InternalPreconditionFailedException(String message) {
        super(message, STATUS);
    }

    private static InternalPreconditionFailedException format(Object... params) {
        return new InternalPreconditionFailedException(MessageFormat.format(CATEGORY_NOT_EMPTY_MESSAGE, params));
    }

    public static InternalPreconditionFailedException withCategoryNotEmptyMessage(Category category) {
        return format(category.getName(), category.getId());
    }
}
