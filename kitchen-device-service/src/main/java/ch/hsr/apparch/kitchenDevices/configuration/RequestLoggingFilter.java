package ch.hsr.apparch.kitchenDevices.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

public final class RequestLoggingFilter extends AbstractRequestLoggingFilter {

    private static final Logger LOGGER = LogManager.getLogger("Request-Logging");

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return LOGGER.isInfoEnabled();
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        LOGGER.info(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        LOGGER.info(message);
    }
}
