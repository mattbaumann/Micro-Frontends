/**
 * Presentation layer of the recipe service application.
 * <p>
 * These classes render pages for listing, editing and deleting the database records.
 * {@link ch.hsr.apparch.recipe.controller.CategoryController}, {@link ch.hsr.apparch.recipe.controller.RecipeController} are
 * normal CRUD (create, remove, update, delete) controllers to modify the database data.
 * <p>
 * {@link ch.hsr.apparch.recipe.controller.ErrorPageController} displays an error page for exceptions thrown in the
 * processing of a request.
 * <p>
 * {@link ch.hsr.apparch.recipe.controller.HomeController} displays the homepage of the controller.
 * <p>
 * {@link ch.hsr.apparch.recipe.controller.PortalCompositionController} Allows the composition of the
 * purchase-list and kitchen-device services into a common portal page. For more information see the
 * informal rich pictures in the architecture documentation.
 */
package ch.hsr.apparch.recipe.controller;