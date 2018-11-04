package ch.hsr.apparch.recipe.controller;

import ch.hsr.apparch.recipe.exceptions.WebServiceException;
import lombok.extern.log4j.Log4j2;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * Renders an error page for {@link WebServiceException} types
 * <p>
 * Renders an dynamic error page which explains the error occured in the controller. This functionality is cross-cutting
 * and available for all controllers per default. Controllers can override this page by defining their own
 * {@link ExceptionHandler} locally.
 */
@Log4j2
@ControllerAdvice
public class ErrorPageController {

    private static final String STATUS_KEY = "status";
    private static final String MESSAGE_KEY = "message";
    private static final String TYPE_KEY = "type";
    private static final String STACK_TRACE = "stacktrace";

    @ExceptionHandler(WebServiceException.class)
    public String showNotEmptyCategoryError(WebServiceException exception, Model model, HttpServletResponse response) {
        LOGGER.entry(exception);

        LOGGER.error("Generating error message for WebServiceException");
        model.addAttribute(STATUS_KEY, exception.getErrorStatus())
                .addAttribute(MESSAGE_KEY, exception.getMessage())
                .addAttribute(TYPE_KEY, exception.getClass().getName())
                .addAttribute(STACK_TRACE, exception.getStackTrace());

        response.setStatus(exception.getErrorStatus().getStatusValue());

        return LOGGER.traceExit("error/exceptionThrown");
    }
}
