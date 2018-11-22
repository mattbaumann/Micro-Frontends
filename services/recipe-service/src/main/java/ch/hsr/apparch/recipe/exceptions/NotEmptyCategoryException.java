package ch.hsr.apparch.recipe.exceptions;

import ch.hsr.apparch.recipe.model.Category;

import java.text.MessageFormat;

public final class NotEmptyCategoryException extends RuntimeException {

    private static final long serialVersionUID = 4165456462837416107L;
    private static final String CATEGORY_NOT_EMPTY_MESSAGE = "Category ''{0}'' with ID ''{1}'' is not empty";

    public NotEmptyCategoryException() {

    }

    public NotEmptyCategoryException(String message) {
        super(message);
    }

    public NotEmptyCategoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEmptyCategoryException(Throwable cause) {
        super(cause);
    }

    private static NotEmptyCategoryException format(String message, Object... params) {
        return new NotEmptyCategoryException(MessageFormat.format(message, params));
    }

    public static NotEmptyCategoryException withCategoryNotEmptyMessage(Category category) {
        return format(CATEGORY_NOT_EMPTY_MESSAGE, category.getName(), category.getId());
    }
}
