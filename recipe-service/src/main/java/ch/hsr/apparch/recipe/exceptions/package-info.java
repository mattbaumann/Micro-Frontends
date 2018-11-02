/**
 * This package contains exception types thrown in the application.
 * <p>
 * Throwing an exception in Spring allows a controlled cancellation of the transaction. The cancellation will prevent
 * the corruption of data and show the user an error page with helpful information to understand the error condition.
 * <p>
 * The _Exception_ type is always constructed by the static construction functions in each class. Each error condition
 * needs a construction function. This allows to capsule the error information code to this package alone.
 * <p>
 * Because the error presentation code for all _Exception_ types is the same. That's why all _Exception_ classes extend
 * from {@link ch.hsr.apparch.recipe.exceptions.WebServiceException} and are handled the same way in the presentation
 * layer. Still each error condition must have an individual exception class to format the error message and provide
 * identity of the error.
 * <p>
 * {@link ch.hsr.apparch.recipe.exceptions.ErrorHttpStatus} allows the _Exception_ types to define custom _HTTP codes_.
 */
package ch.hsr.apparch.recipe.exceptions;