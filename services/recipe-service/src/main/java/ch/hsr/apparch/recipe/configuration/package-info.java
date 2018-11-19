/**
 * Modifications to Spring Framework
 * <p>
 * {@link ch.hsr.apparch.recipe.configuration.RequestLoggingConfiguration} injects a request logger in spring WebMVC,
 * when activating the Log4J2 logger, the filter will output all requested paths.
 * <p>
 * {@link ch.hsr.apparch.recipe.configuration.WebMVCConfiguration} is used by
 * {@link ch.hsr.apparch.recipe.controller.PortalCompositionController} to add the
 * {@link org.thymeleaf.templateresolver.UrlTemplateResolver} to _Thymeleaf_. The add-in allows Thymeleaf to resolve
 * _fragments_ from remote hosts to form a portal site.
 */
package ch.hsr.apparch.recipe.configuration;