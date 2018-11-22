package ch.hsr.apparch.kitchenDevices.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

@Log4j2
public final class RequestLoggingFilter extends AbstractRequestLoggingFilter {

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
