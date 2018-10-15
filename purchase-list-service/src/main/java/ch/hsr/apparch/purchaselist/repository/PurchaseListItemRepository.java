package ch.hsr.apparch.purchaselist.repository;

import ch.hsr.apparch.purchaselist.model.PurchaseListItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PurchaseListItemRepository extends PagingAndSortingRepository<PurchaseListItem, Long> {

    /**
     * Removes the object with the defined id.
     * <p>
     * ATTENTION: Do not remove this function, while the backend provides the {@link #existsById(Object)} method,
     * the Spring-data-rest frontend always invokes a function called "delete" and does not care about the backend func.
     *
     * @param id The id to delete, must be defined in the database or exception is thrown
     */
    default void delete(long id) {
        deleteById(id);
    }


}
