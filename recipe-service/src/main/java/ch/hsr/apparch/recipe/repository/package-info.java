/**
 * Database <i>Repository</i> for the category <i>entity</i>.
 *
 * <p>
 * Manages the persistence of the category <i>entity</i>. While the class is automatically defined by Spring, the
 * interface describes the <i>persistence layer</i> proved by Spring.
 * </p>
 * <p>
 * While the interface seems small, the Spring backend does the following tasks with the <i>entity</i> in the
 * saving path:
 * <ul>
 * <li>Checking the data validation rules of the entity</li>
 * <li>Object to database record mapping, so called O-R Mapping</li>
 * <li>Saving the record in the database</li>
 * </ul>
 * <p>
 * By defining own functions with specific function names, Spring will define database queries, which are executed
 * at each function call.
 * </p>
 */

package ch.hsr.apparch.recipe.repository;