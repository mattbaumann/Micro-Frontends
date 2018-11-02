package ch.hsr.apparch.recipe.exceptions;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

/**
 * Represents a reponse status code
 * <p>
 * This class was introduced because the {@link HttpStatus} does not support custom defined status codes. Still, it is
 * a good tone to have custom codes for the REST API. This class allows to define codes different then the
 * https://tools.ietf.org/html/rfc7231[RFC 7231] status code. In fact this class allows all codes, but it's good tone
 * only to define status codes between 520 and 599.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ErrorHttpStatus {

    private final int statusValue;

    private final String statusMessage;

    ErrorHttpStatus(HttpStatus statusValue) {
        this.statusValue = statusValue.value();
        this.statusMessage = statusValue.getReasonPhrase();
    }

    @Nullable
    public HttpStatus toHttpStatus() {
        return HttpStatus.resolve(statusValue);
    }
}
